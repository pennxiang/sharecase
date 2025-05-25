package com.sharedcase.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: caseDetail
 * Package: com.sharedcase.entity
 * Description:
 *      病例详细信息（ipfs）
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 13:43
 */
@Data
public class CaseDetail {

    // 基础链上字段
    private String caseId;          // UUID 或雪花 ID，前端生成或后端生成
    private String icdCode;         // 病种编码（如 I10）
    private String ipfsHash;        // 后端上传后赋值
    private String patientAddress;  // 链上患者地址
    private String doctorAddress;   // 当前医生地址（链上 msg.sender）

    // 内容字段（用于生成 PDF，前端填写）
    private String title;           // 病例标题
    private String chiefComplaint;  // 主诉
    private String presentIllness;  // 现病史
    private String pastHistory;     // 既往史
    private String diagnosis;       // 医生诊断
    private String doctorAdvice;    // 医嘱建议
    private String hospitalAddress;

    // 生成时间
    private LocalDateTime visitTime;
}
