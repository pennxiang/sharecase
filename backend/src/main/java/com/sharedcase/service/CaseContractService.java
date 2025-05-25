package com.sharedcase.service;

import com.CaseContract;
import com.sharedcase.entity.HotDiseaseDTO;
import com.sharedcase.exception.ChainWriteException;
import com.sharedcase.util.FiscoUtil;
import lombok.RequiredArgsConstructor;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * ClassName: CaseContractService
 * Package: com.sharedcase.service
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:23
 */

public interface CaseContractService {
/*
    private FiscoUtil fiscoUtil;

    private CaseContract caseContract;

    @Value("${contract.case}")
    private String contractAddress;*/

    /**
     * 获取所有病例版本号
     */
    List<String> getAllVersionCodes() throws Exception;

    /**
     * 获取指定日期的版本号
     */
    List<String> getVersionCodesByDate(String startDate, String endDate) throws Exception;

    /**
     * 获取某病例的最新版本 CID（链上）
     */
    String getLatestVersionHash(String caseId) throws Exception;

    /**
     * 获取某病例的所有历史版本（链上）
     */

    /**
     * 医生通过 idCard + icdCode 查询版本哈希（链上）
     */
    String getCaseVersionHash(String userId, String icdCode) throws Exception;

    /**
     * 新用户注册链上身份（例如使用 hash(idCard + salt)）
     */
    public String registerUser(String userHash, String idCardEnc) throws Exception;

    /**
     * 上传一个新病例版本（返回交易哈希或 block hash）
     */
    String addVersion(String caseId, String ipfsHash, String versionCode) throws Exception;


    /* *//**
     * 写链：添加一个版本记录
     *//*
    public String addVersion(String caseId, String ipfsHash, String versionCode) throws ChainWriteException {
        if (caseId == null || ipfsHash == null || versionCode == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        // 构建SDK
        BcosSDK sdk = fiscoUtil.bcosSDK();;
        try {
            // 获取客户端
            Client client = fiscoUtil.fiscoClient(sdk);

            CryptoKeyPair keyPair = fiscoUtil.cryptoKeyPair(client);
            // 写入内容
            TransactionReceipt receipt = caseContract.addVersion(caseId, ipfsHash, versionCode);
            // 返回写入是否成功
            String txHash = receipt.getTransactionHash();

            if (!receipt.isStatusOK()) {
                String errorMsg = "链上写入失败: " + receipt.getMessage();
                // 可选：记录日志
                // logger.error(errorMsg);
                throw new ChainWriteException(errorMsg);
            }

            // 可选：记录日志
            // logger.info("链上写入成功，交易哈希：{}", txHash);
            return txHash;
        } catch (Exception e) {
            // 可选：记录日志
            // logger.error("链上写入发生异常", e);
            throw new ChainWriteException("链上写入发生异常", e);
        } *//*finally {
            // 如果 BcosSDK 支持关闭，则在此处关闭
            if (sdk != null && sdk.isOpen()) {
                sdk.destroy();
            }
        }*//*
    }
*/

//
//    /**
//     * 查链：获取某个病例的历史版本 CID 列表
//     */
//    public List<String> getVersionHistory(String caseId) throws Exception {
//        CaseContract contract = fiscoUtil.loadContract(CaseContract.class, contractAddress, client, cryptoKeyPair);
//        Tuple1<List<String>> result = contract.getVersionHistory(caseId);
//        return result.getValue1();
//    }


}
