package com.sharedcase.DTO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: CaseInfo
 * Package: com.sharedcase.DTO
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/25 15:56
 */
@Data
public class CaseInfo {
    private String caseId;
    private String icdCode;
    private String ipfsHash;
    private String doctor; // 链上写入人地址
    private LocalDateTime visitTime;

    private String doctorAddress;
    private String patientAddress;
    private Long rawTimestamp;

    // 可选补充展示字段
    private String pdfUrl; // IPFS 网关路径
}

