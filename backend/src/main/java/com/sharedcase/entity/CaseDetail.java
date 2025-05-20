package com.sharedcase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ClassName: caseDetail
 * Package: com.sharedcase.entity
 * Description:
 *      病例基础信息（链下）
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 13:43
 */
@Data
@TableName("case_detail")
public class CaseDetail {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 病例ID */
    private String caseId;

    /** 病例标题 */
    private String title;

    /** 关联 ICD 编码 */
    private String icdCode;

    /** 所属医院ID */
    private Long hospitalId;

    /** 用户ID（患者） */
    private Long userId;

    /** 医生ID */
    private Long doctorId;

    /** 病人自述症状 */
    private String chiefComplaint;

    /** 现病史 */
    private String presentIllnessHistory;

    /** 既往史 */
    private String pastHistory;

    /** 初步诊断 */
    private String diagnosis;

    /** 医生建议 */
    private String doctorAdvice;

    /** 复查结果 */
    private String recheckResult;

    /** 填写时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
