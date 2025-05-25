package com.sharedcase.service;

import com.CaseContract;
import com.sharedcase.DTO.CaseInfo;
import com.sharedcase.config.ContractConfig;
import com.sharedcase.entity.CaseDetail;
import com.sharedcase.util.FiscoUtil;
import com.sharedcase.util.PdfUtil;
import jakarta.annotation.PostConstruct;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: CaseDetailService
 * Package: com.sharedcase.service
 * Description:
 *      病例服务
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:20
 */
@Service
public class CaseService {

    private static final Logger logger = LoggerFactory.getLogger(CaseService.class);

    @Autowired
    private IpfsService ipfsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContractConfig  contractConfig;

    @Autowired
    private FiscoUtil fiscoUtil;

    private CaseContract caseContract;

    @PostConstruct
    public void initContract() {
        // 使用 fiscoUtil 加载 CaseContract 合约
        String contractAddress = contractConfig.getAddress("caseContract");
        BcosSDK bcosSDK = fiscoUtil.bcosSDK();
        Client client = fiscoUtil.fiscoClient(bcosSDK);
        CryptoKeyPair keyPair = fiscoUtil.cryptoKeyPair(client);
        caseContract = fiscoUtil.loadContract(CaseContract.class, contractAddress, client, keyPair);
    }

    /**
     * 创建病例
     */
    public void createCase(CaseDetail caseDetail) throws Exception {
        // 1. 设置就诊时间
        caseDetail.setVisitTime(LocalDateTime.now());

        // 2. 导出 PDF 文件
        System.out.println("构建PDF...");
        File pdf = PdfUtil.exportCaseDetailToPdf(caseDetail);

        // 3. 上传到 IPFS
        System.out.println("上传到IPFS...");
        String ipfsHash = ipfsService.upload(pdf, "/cases/" + caseDetail.getCaseId());
//        String ipfsHash = IpfsUtil.upload(pdf, "/cases/" + caseDetail.getCaseId());
        System.out.println("上传成功，CID: " + ipfsHash);
        caseDetail.setIpfsHash(ipfsHash);

        // 4. 写入区块链
        System.out.println("写入区块链...");
        TransactionReceipt transactionReceipt = caseContract.addCase(
                caseDetail.getCaseId(),
                caseDetail.getIcdCode(),
                ipfsHash,
                caseDetail.getPatientAddress()
        );
        if (!transactionReceipt.isStatusOK()) {
            logger.error("链上写入失败: " + transactionReceipt.getMessage());
            throw new RuntimeException("链上写入失败: " + transactionReceipt.getMessage());
        }
        String txHash = transactionReceipt.getTransactionHash();

        // 5. 可选：返回结果、记录 txHash 日志等
        logger.info("链上写入成功，交易哈希: " + txHash);
        System.out.println("链上写入成功，交易哈希: " + txHash);
    }

    public List<CaseInfo> getCasesByPatient(String patientAddress) throws Exception {
        int count = caseContract.getCaseCount(patientAddress).intValue();
        List<CaseInfo> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tuple5<String, String, String, String, BigInteger> record = caseContract.getCase(patientAddress, BigInteger.valueOf(i));
            CaseInfo info = new CaseInfo();
            info.setCaseId(record.getValue1());
            info.setIcdCode(record.getValue2());
            info.setIpfsHash(record.getValue3());
            info.setDoctor(record.getValue4());
            info.setVisitTime(Instant.ofEpochSecond(record.getValue5().longValue()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            result.add(info);
        }
        return result;
    }

    public List<CaseInfo> getCasesByIcd(String patientAddress, String icdCode) throws Exception {
        List<CaseInfo> all = getCasesByPatient(patientAddress);
        return all.stream()
                .filter(caseInfo -> caseInfo.getIcdCode().equals(icdCode))
                .collect(Collectors.toList());
    }

    public Map<String, Long> getIcdStats(String patientAddress, LocalDateTime from, LocalDateTime to) throws Exception {
        List<CaseInfo> all = getCasesByPatient(patientAddress);
        return all.stream()
                .filter(c -> !c.getVisitTime().isBefore(from) && !c.getVisitTime().isAfter(to))
                .collect(Collectors.groupingBy(CaseInfo::getIcdCode, Collectors.counting()));
    }
    /*
    *//**
     * 根据ipfs hash获取病例信息
     *//*
    public CaseDetail getCaseByHash(String ipfsHash) {
        try {
            // 从 IPFS 下载文件
            File pdfFile = IpfsUtil.download(ipfsHash); // 你自己的方法，返回 File

            // 解析 PDF → 构建病例对象
            CaseDetail aCaseDetail = PdfUtil.parsePdfToCaseDetail(pdfFile);

            // 删除临时文件（更推荐在 download 方法内部设置 deleteOnExit）
            if (!pdfFile.delete()) {
                logger.warn("临时文件删除失败：" + pdfFile.getAbsolutePath());
            }

            return aCaseDetail;
        } catch (Exception e) {
            logger.error("解析病例失败，CID: " + ipfsHash, e);
            throw new RuntimeException("解析病例失败，CID: " + ipfsHash, e);
        }
    }*/


}
