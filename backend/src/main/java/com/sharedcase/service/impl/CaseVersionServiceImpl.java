package com.sharedcase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.dao.CaseVersionMapper;
import com.sharedcase.entity.CaseVersion;
import com.sharedcase.entity.HotDiseaseDTO;
import com.sharedcase.entity.User;
import com.sharedcase.service.CaseContractService;
import com.sharedcase.service.CaseVersionService;
import com.sharedcase.service.UserService;
import com.sharedcase.util.IpfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: CaseVersionServiceImpl
 * Package: com.sharedcase.service.impl
 * Description:
 *      fisco链上病例版本服务类
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:26
 */
@Service
public class CaseVersionServiceImpl extends ServiceImpl<CaseVersionMapper, CaseVersion> implements CaseVersionService {

    @Autowired
    private CaseVersionMapper caseVersionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CaseContractService caseContractService;

    @Override
    public Long getTotal(String icdCode) {
        return caseVersionMapper.selectCount(
                new LambdaQueryWrapper<CaseVersion>()
                        .apply("LEFT(version_code, LOCATE(':', version_code) - 1) = {0}", icdCode)
        );
    }

    @Override
    public List<CaseVersion> getAllVersions(User user) {
        return caseVersionMapper.selectList(
                new LambdaQueryWrapper<CaseVersion>()
                        .eq(CaseVersion::getPatient, user.getChainAddress())
                        .orderByDesc(CaseVersion::getCreateTime)
        );
    }


    @Override
    public List<HotDiseaseDTO> getDiseaseStatsByDate(String startDate, String endDate, boolean ascending) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime start = LocalDateTime.parse("20" + startDate, formatter);
        LocalDateTime end = LocalDateTime.parse("20" + endDate, formatter);

        List<String> versionCodes = caseContractService.getAllVersionCodes(); // 链上调用

        // 提取 ICD + 时间 → 聚合统计
        Map<String, Long> countMap = versionCodes.stream()
                .map(code -> code.split(":"))
                .filter(parts -> parts.length >= 3)
                .filter(parts -> {
                    try {
                        LocalDateTime ts = LocalDateTime.parse("20" + parts[2], formatter);
                        return !ts.isBefore(start) && !ts.isAfter(end);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(parts -> parts[0]) // ICD 码
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 转为 DTO 并排序
        return countMap.entrySet().stream()
                .sorted((e1, e2) -> ascending
                        ? e1.getValue().compareTo(e2.getValue())
                        : e2.getValue().compareTo(e1.getValue()))
                .map(entry -> {
                    HotDiseaseDTO dto = new HotDiseaseDTO();
                    dto.setIcdCode(entry.getKey());
                    dto.setCount(entry.getValue());
                    return dto;
                })
                .toList();
    }

    /**
     * 上传病例版本
     */
    @Override
    public CaseVersion uploadVersion(String caseId, String versionCode, File file, String summary, Long uploaderId) throws Exception {
        // 1. 上传文件到 IPFS
        String ipfsHash = IpfsUtil.upload(file, "/cases");

        // 2. 获取上传医生和关联患者
        User doctor = userService.getById(uploaderId);
        if (doctor == null) {
            throw new RuntimeException("上传医生不存在");
        }

        // 3. 写入链上版本信息
        String txHash = caseContractService.addVersion(caseId, ipfsHash, versionCode);

        // 4. 持久化 CaseVersion 到数据库
        CaseVersion version = new CaseVersion();
        version.setCaseId(caseId);
        version.setVersionCode(versionCode);
        version.setIpfsHash(ipfsHash);
        version.setDoctor(doctor.getChainAddress());
        version.setPatient(doctor.getChainAddress());
        version.setCreateTime(LocalDateTime.now());
        version.setChainTxHash(txHash);

        this.save(version);
        return version;
    }

}
