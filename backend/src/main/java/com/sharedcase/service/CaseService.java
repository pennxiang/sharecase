package com.sharedcase.service;

import com.CaseContract;
import com.sharedcase.DTO.CaseInfo;
import com.sharedcase.DTO.IcdFrequency;
import com.sharedcase.config.ContractConfig;
import com.sharedcase.entity.CaseDetail;
import com.sharedcase.util.CaseDiffUtil;
import com.sharedcase.util.FiscoUtil;
import com.sharedcase.util.PdfUtil;
import jakarta.annotation.PostConstruct;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//        caseDetail.setVisitTime(LocalDateTime.now());

        // 2. 导出 PDF 文件
        System.out.println("构建PDF...");
        File pdf = PdfUtil.exportCaseDetailToPdf(caseDetail);

        // 3. 上传到 IPFS
        System.out.println("上传到IPFS...");
        String ipfsHash = ipfsService.upload(pdf, "/cases/" + caseDetail.getCaseId());
        System.out.println("上传成功，CID: " + ipfsHash);
        caseDetail.setIpfsHash(ipfsHash);

        // 4. 写入区块链
        System.out.println("写入区块链...");
        System.out.println("合约地址: " + caseContract.getContractAddress());

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

    public List<CaseDetail> listAll() {
        try {
            Tuple6<List<String>, List<String>, List<String>, List<String>, List<String>, List<BigInteger>> cases =
                    caseContract.getAllCases();

            List<CaseDetail> result = new ArrayList<>();

            List<String> caseIds = cases.getValue1();
            List<String> icdCodes = cases.getValue2();
            List<String> ipfsHashes = cases.getValue3();
            List<String> patients = cases.getValue4();
            List<String> doctors = cases.getValue5();
            List<BigInteger> visitTimes = cases.getValue6();



            for (int i = 0; i < caseIds.size(); i++) {
                CaseDetail detail = new CaseDetail();
                detail.setCaseId(caseIds.get(i));
                detail.setIcdCode(icdCodes.get(i));
                detail.setIpfsHash(ipfsHashes.get(i));
                detail.setPatientAddress(patients.get(i));
                detail.setDoctorAddress(doctors.get(i));

                // 时间转换
                BigInteger rawTimestamp = visitTimes.get(i);
                long ts = rawTimestamp.longValue();
                if (ts > 9999999999L) { // 超过10位，说明是毫秒，转为秒
                    ts = ts / 1000;
                }
                LocalDateTime visitTime = Instant.ofEpochSecond(ts)
                        .atZone(ZoneId.of("Asia/Shanghai"))
                        .toLocalDateTime();

                detail.setVisitTime(visitTime);
                result.add(detail);
            }

            return result;
        } catch (ContractException e) {
            throw new RuntimeException("链上获取所有病例失败: " + e.getMessage(), e);
        }
    }


    public List<CaseInfo> getCasesByPatient(String patientAddress) throws Exception {
        List<CaseInfo> result = new ArrayList<>();
        BigInteger count = caseContract.getCaseCount(patientAddress);

        for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
            Tuple5<String, String, String, String, BigInteger> tuple = caseContract.getCase(patientAddress, i);

            BigInteger rawTimestamp = tuple.getValue5();
            long ts = rawTimestamp.longValue();
            if (ts > 9999999999L) { // 超过10位，说明是毫秒，转为秒
                ts = ts / 1000;
            }
            LocalDateTime visitTime = Instant.ofEpochSecond(ts)
                    .atZone(ZoneId.of("Asia/Shanghai"))
                    .toLocalDateTime();

            // 打印日志
            System.out.println("【病例】caseId=" + tuple.getValue1()
                    + ", 原始时间戳=" + rawTimestamp
                    + ", 本地时间=" + visitTime);

            CaseInfo info = new CaseInfo();
            info.setCaseId(tuple.getValue1());
            info.setIcdCode(tuple.getValue2());
            info.setIpfsHash(tuple.getValue3());
            info.setDoctorAddress(tuple.getValue4());
            info.setPatientAddress(patientAddress);
            info.setVisitTime(visitTime);
            info.setRawTimestamp(rawTimestamp.longValue());

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
                .filter(c -> {
                    long ts = c.getRawTimestamp(); // 从 CaseInfo 拿原始时间戳
                    if (ts > 9999999999L) ts /= 1000;
                    LocalDateTime visitTime = Instant.ofEpochSecond(ts)
                            .atZone(ZoneId.of("Asia/Shanghai"))
                            .toLocalDateTime();
                    return !visitTime.isBefore(from) && !visitTime.isAfter(to);
                })
                .collect(Collectors.groupingBy(CaseInfo::getIcdCode, Collectors.counting()));
    }

    public List<CaseDetail> getCasesByPatientAndTimeRange(String patient, LocalDateTime from, LocalDateTime to) throws Exception {
        List<CaseDetail> result = new ArrayList<>();
        BigInteger count = caseContract.getCaseCount(patient);

        for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
            Tuple5<String, String, String, String, BigInteger> tuple = caseContract.getCase(patient, i);

            long ts = tuple.getValue5().longValue();
            if (ts > 9999999999L) ts /= 1000;
            LocalDateTime visitTime = Instant.ofEpochSecond(ts)
                    .atZone(ZoneId.of("Asia/Shanghai"))
                    .toLocalDateTime();

            if (!visitTime.isBefore(from) && !visitTime.isAfter(to)) {
                CaseDetail detail = new CaseDetail();
                detail.setCaseId(tuple.getValue1());
                detail.setIcdCode(tuple.getValue2());
                detail.setIpfsHash(tuple.getValue3());
                detail.setDoctorAddress(tuple.getValue4());
                detail.setPatientAddress(patient);
                detail.setVisitTime(visitTime);
                result.add(detail);
            }
        }

        return result;
    }



    public List<IcdFrequency> getICDFrequencySorted(boolean desc) {
        List<String> allPatientAddresses;
        try {
            allPatientAddresses = caseContract.getAllPatients();
        } catch (ContractException e) {
            throw new RuntimeException("获取患者地址失败: " + e.getMessage(), e);
        }

        Map<String, Long> icdMap = new HashMap<>();

        for (String patient : allPatientAddresses) {
            BigInteger count;
            try {
                count = caseContract.getCaseCount(patient);
            } catch (ContractException e) {
                throw new RuntimeException("获取患者病例数量失败: " + e.getMessage(), e);
            }

            for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
                Tuple5<String, String, String, String, BigInteger> tuple;
                try {
                    tuple = caseContract.getCase(patient, i);
                } catch (ContractException e) {
                    throw new RuntimeException("获取病例失败: " + e.getMessage(), e);
                }

                String icdCode = tuple.getValue2();
                icdMap.merge(icdCode, 1L, Long::sum);
            }
        }

        return icdMap.entrySet().stream()
                .sorted(desc
                        ? Map.Entry.<String, Long>comparingByValue().reversed()
                        : Map.Entry.comparingByValue())
                .map(e -> new IcdFrequency(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public List<CaseDetail> getCasesByTimeRange(LocalDateTime from, LocalDateTime to) throws Exception {
        List<String> allPatients = caseContract.getAllPatients();
        List<CaseDetail> result = new ArrayList<>();

        for (String patient : allPatients) {
            BigInteger count = caseContract.getCaseCount(patient);
            for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
                Tuple5<String, String, String, String, BigInteger> tuple = caseContract.getCase(patient, i);

                BigInteger rawTimestamp = tuple.getValue5();
                long ts = rawTimestamp.longValue();
                if (ts > 9999999999L) { // 判断是否为毫秒
                    ts /= 1000;
                }

                LocalDateTime visitTime = Instant.ofEpochSecond(ts)
                        .atZone(ZoneId.of("Asia/Shanghai"))
                        .toLocalDateTime();

                if (!visitTime.isBefore(from) && !visitTime.isAfter(to)) {
                    CaseDetail detail = new CaseDetail();
                    detail.setCaseId(tuple.getValue1());
                    detail.setIcdCode(tuple.getValue2());
                    detail.setIpfsHash(tuple.getValue3());
                    detail.setPatientAddress(patient);
                    detail.setDoctorAddress(tuple.getValue4());
                    detail.setVisitTime(visitTime);
                    result.add(detail);
                }
            }
        }

        return result;
    }

    public Map<String, Long> getIcdStatsByTimeRange(LocalDateTime from, LocalDateTime to) throws Exception {
        List<String> allPatients = caseContract.getAllPatients();
        Map<String, Long> icdCounter = new HashMap<>();

        for (String patient : allPatients) {
            BigInteger count = caseContract.getCaseCount(patient);
            for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
                Tuple5<String, String, String, String, BigInteger> tuple = caseContract.getCase(patient, i);
                long ts = tuple.getValue5().longValue();
                if (ts > 9999999999L) ts /= 1000;
                LocalDateTime visitTime = Instant.ofEpochSecond(ts)
                        .atZone(ZoneId.of("Asia/Shanghai"))
                        .toLocalDateTime();

                if (!visitTime.isBefore(from) && !visitTime.isAfter(to)) {
                    String icd = tuple.getValue2();
                    icdCounter.put(icd, icdCounter.getOrDefault(icd, 0L) + 1);
                }
            }
        }

        return icdCounter;
    }

    public List<CaseDetail> searchCasesFuzzy(String keyword) throws Exception {
        List<String> allPatients = caseContract.getAllPatients();
        List<CaseDetail> result = new ArrayList<>();

        for (String patient : allPatients) {
            BigInteger count = caseContract.getCaseCount(patient);
            for (BigInteger i = BigInteger.ZERO; i.compareTo(count) < 0; i = i.add(BigInteger.ONE)) {
                Tuple5<String, String, String, String, BigInteger> tuple = caseContract.getCase(patient, i);

                File pdfFile = ipfsService.download(tuple.getValue3());
                CaseDetail detail = PdfUtil.parsePdfToCaseDetail(pdfFile);
                detail.setCaseId(tuple.getValue1());
                detail.setPatientAddress(patient);
                detail.setDoctorAddress(tuple.getValue4());
                detail.setVisitTime(Instant.ofEpochSecond(tuple.getValue5().longValue())
                        .atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime());

                // 模糊匹配（title、diagnosis、chiefComplaint）
                if ((detail.getTitle() != null && detail.getTitle().contains(keyword)) ||
                        (detail.getDiagnosis() != null && detail.getDiagnosis().contains(keyword)) ||
                        (detail.getChiefComplaint() != null && detail.getChiefComplaint().contains(keyword))) {
                    result.add(detail);
                }

                pdfFile.delete();
            }
        }

        return result;
    }

    public Map<String, List<String>> compareCases(String ipfsHash1, String ipfsHash2) throws Exception {
        File pdf1 = ipfsService.download(ipfsHash1);
        File pdf2 = ipfsService.download(ipfsHash2);

        CaseDetail case1 = PdfUtil.parsePdfToCaseDetail(pdf1);
        CaseDetail case2 = PdfUtil.parsePdfToCaseDetail(pdf2);

        Map<String, List<String>> diff = CaseDiffUtil.compare(case1, case2);

        pdf1.delete();
        pdf2.delete();

        return diff;
    }

}
