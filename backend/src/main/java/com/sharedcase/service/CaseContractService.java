package com.sharedcase.service;

import com.CaseContract;
import com.sharedcase.util.FiscoUtil;
import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class CaseContractService {

    private FiscoUtil fiscoUtil;
    private CryptoKeyPair cryptoKeyPair;
    private Client client;

    @Value("${contract.case}")
    private String contractAddress;

    /**
     * 写链：添加一个版本记录
     */
    public String addVersion(String caseId, String ipfsHash, String versionCode) throws Exception {
        CaseContract contract = fiscoUtil.loadContract(CaseContract.class, contractAddress, client, cryptoKeyPair);

        TransactionReceipt receipt = contract.addVersion(caseId, versionCode, ipfsHash);
        String txHash = receipt.getTransactionHash();

        if (!receipt.isStatusOK()) {
            throw new RuntimeException("链上写入失败: " + receipt.getMessage());
        }

        return txHash;
    }
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
