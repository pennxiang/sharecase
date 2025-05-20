package com.sharedcase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.dao.CaseVersionMapper;
import com.sharedcase.entity.CaseVersion;
import com.sharedcase.service.CaseContractService;
import com.sharedcase.service.CaseVersionService;
import com.sharedcase.util.IpfsUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

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

    private IpfsUtil ipfsUtil;

    private CaseContractService contractService;

    @Override
    public List<CaseVersion> getAllVersions(String caseId) {
        return lambdaQuery().eq(CaseVersion::getCaseId, caseId).list();
    }

    @Override
    public CaseVersion uploadVersion(String caseId, String versionCode, File file, String summary, Long uploaderId) throws Exception {
        // 1. 上传文件到 IPFS
        String ipfsHash = ipfsUtil.upload(file, file.getName());

        // TODO
        // 2. 调用合约写链
        String txHash = contractService.addVersion(caseId, versionCode, ipfsHash);

        // 3. 存库
        CaseVersion version = new CaseVersion();
        version.setCaseId(caseId);
        version.setVersionCode(versionCode);
        version.setIpfsHash(ipfsHash);
        version.setSummary(summary);
        version.setChainTxHash(txHash);
        version.setCreateTime(LocalDateTime.now());
        save(version);
        return version;
    }
}
