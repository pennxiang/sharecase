package com.sharedcase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    /** 是否为复查 **/
    private String isRecheck;

    /** 检查报告ipfs地址 */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> infoAttachments;

    /** 填写时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
