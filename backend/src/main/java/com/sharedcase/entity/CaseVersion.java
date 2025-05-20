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
 * ClassName: CaseVersion
 * Package: com.sharedcase.entity
 * Description:
 *      版本记录（链上信息 + IPFS）
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 13:00
 */
@Data
public class CaseVersion {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 病例ID，如：CASE123 */
    private String caseId;

    /** 版本号，例如：J18.9:1:240519124522 */
    private String versionCode;

    /** IPFS 内容哈希 */
    private String ipfsHash;

    /** 病情简要描述（链下展示用） */
    private String summary;

    /** 编辑人链上地址 */
    private String editor;

    /** 检查报告ipfs地址(多个) */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> infoAttachments;

    /** 版本上传时间 */
    private LocalDateTime createTime;

    /** 链上交易哈希（方便追踪） */
    private String chainTxHash;
}
