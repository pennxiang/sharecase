package com.sharedcase.service.impl;

import com.CaseContract;
import com.UserRegistry;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.dao.CaseVersionMapper;
import com.sharedcase.entity.CaseVersion;
import com.sharedcase.entity.User;
import com.sharedcase.service.CaseContractService;
import com.sharedcase.util.FiscoUtil;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ClassName: CaseContractServiceImpl
 * Package: com.sharedcase.service.impl
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/23 15:35
 */
@Service
public class CaseContractServiceImpl extends ServiceImpl<CaseVersionMapper, CaseVersion> implements CaseContractService {
    @Autowired
    private FiscoUtil fiscoUtil;

    @Autowired
    private CaseVersionMapper caseVersionMapper;

    @Autowired
    private UserServiceImpl userService;

    private CaseContract caseContract;

    /**
     * 获取所有版本号
     * <p>
     * 此方法通过调用caseContract对象的getAllVersionCodes().send()方法，从区块链上获取所有的版本号
     * 由于此调用可能会抛出异常，因此使用throws Exception来声明异常
     *
     * @return 包含所有版本号的字符串列表
     * @throws Exception 如果获取过程中发生错误，抛出此异常
     */
    @Override
    public List getAllVersionCodes() throws Exception {
        return caseContract.getAllVersionCodes(); // 链上原始获取
    }

    /**
     * 根据指定的日期范围获取版本代码列表
     *
     * @param startDate 开始日期字符串，格式为yyMMddHHmmss
     * @param endDate 结束日期字符串，格式为yyMMddHHmmss
     * @return 在指定日期范围内的版本代码列表
     * @throws Exception 如果日期格式不正确或解析失败
     */
    @Override
    public List<String> getVersionCodesByDate(String startDate, String endDate) throws Exception {
        // 定义日期时间格式器，用于解析和格式化日期时间字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // 解析开始日期和结束日期字符串为LocalDateTime对象
        LocalDateTime start = LocalDateTime.parse("20" + startDate, formatter);
        LocalDateTime end = LocalDateTime.parse("20" + endDate, formatter);

        // 获取所有版本代码的列表
        List<String> allCodes = getAllVersionCodes();

        // 使用流处理，过滤出时间戳在指定范围内的版本代码
        return allCodes.stream()
                .filter(code -> {
                    // 分割版本代码字符串以获取时间戳部分
                    String[] parts = code.split(":");
                    // 如果版本代码格式不正确（即不包含时间戳信息），则忽略
                    if (parts.length < 3) return false;

                    try {
                        // 将时间戳部分解析为LocalDateTime对象
                        LocalDateTime timestamp = LocalDateTime.parse("20" + parts[2], formatter);
                        // 检查时间戳是否在指定的日期范围内
                        return !timestamp.isBefore(start) && !timestamp.isAfter(end);
                    } catch (Exception e) {
                        // 如果时间戳格式不正确，则忽略此版本代码
                        return false;
                    }
                })
                .toList();
    }


    /**
     * 获取最新版本 CID
     */
    @Override
    public String getLatestVersionHash(String caseId) throws Exception {
        CaseVersion latest = caseVersionMapper.selectLatestByCaseId(caseId);
        return latest != null ? latest.getIpfsHash() : null;
    }

    /**
     * 获取所有历史版本
     */
    @Override
    public List<CaseVersion> getAllVersions(String caseId) {
        return caseVersionMapper.selectList(
                new LambdaQueryWrapper<CaseVersion>()
                        .eq(CaseVersion::getCaseId, caseId)
                        .orderByDesc(CaseVersion::getCreateTime)
        );
    }

    /**
     * 通过 idCard 和 icd 查版本（示例逻辑：查最新）
     */
    @Override
    public String getCaseVersionHash(String idCard, String icdCode) {
        // 1. 通过加密身份证查找对应用户
        User user = userService.lambdaQuery()
                .eq(User::getIdCard, idCard)
                .select(User::getChainAddress)
                .one();

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String patientAddress = user.getChainAddress();

        // 2. 查找指定 ICD 相关的最新病例版本（精确匹配 versionCode 的开头）
        CaseVersion version = this.lambdaQuery()
                .likeRight(CaseVersion::getVersionCode, icdCode + ":") // versionCode 以 icdCode 开头
                .eq(CaseVersion::getPatient, patientAddress)
                .orderByDesc(CaseVersion::getCreateTime)
                .last("LIMIT 1")
                .one();

        return version != null ? version.getIpfsHash() : null;
    }

    /**
     * 注册用户链上身份
     */
    @Override
    public String registerUser(String userHash, String idCardEnc) throws Exception {
        Client client = fiscoUtil.fiscoClient(fiscoUtil.bcosSDK());
        CryptoKeyPair keyPair = fiscoUtil.cryptoKeyPair(client);

        UserRegistry contract = UserRegistry.load(
                fiscoUtil.contractAddress("UserRegistry"),
                client,
                keyPair
        );

        TransactionReceipt receipt = contract.register(userHash, idCardEnc);
        return receipt.getTransactionHash();
    }


    /**
     * 上传版本信息到链上
     */
    @Override
    public String addVersion(String caseId, String ipfsHash, String versionCode) throws Exception {
        Client client = fiscoUtil.fiscoClient(fiscoUtil.bcosSDK());
        CryptoKeyPair keyPair = fiscoUtil.cryptoKeyPair(client);

        CaseContract contract = CaseContract.load(
                fiscoUtil.contractAddress("CaseContract"),
                client,
                keyPair
        );

        TransactionReceipt receipt = contract.addVersion(caseId, ipfsHash, versionCode);
        if (!receipt.isStatusOK()) {
            throw new RuntimeException("链上写入失败: " + receipt.getMessage());
        }

        return receipt.getTransactionHash();
    }


}
