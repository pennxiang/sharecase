CREATE TABLE `user` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键，存放在表里',
                        `name` VARCHAR(50) NOT NULL COMMENT '姓名',
                        `password` VARCHAR(100) NOT NULL COMMENT '系统登录密码，加密存储',
                        `id_card` VARCHAR(100) NOT NULL COMMENT '身份证号，加密存储',
                        `role` VARCHAR(50) NOT NULL COMMENT '角色，例如 患者、医生、医保局工作人员',
                        `phone` VARCHAR(30) COMMENT '联系电话',
                        `chain_address` VARCHAR(100) COMMENT '链上地址',
                        `work_id` BIGINT COMMENT '工作单位id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `case_detail` (
                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                               `case_id` VARCHAR(64) NOT NULL COMMENT '病例ID',
                               `title` VARCHAR(255) COMMENT '病例标题',
                               `icd_code` VARCHAR(50) COMMENT '关联 ICD 编码',
                               `hospital_id` BIGINT COMMENT '所属医院ID',
                               `user_id` BIGINT COMMENT '用户ID（患者）',
                               `doctor_id` BIGINT COMMENT '医生ID',
                               `chief_complaint` TEXT COMMENT '病人自述症状',
                               `present_illness_history` TEXT COMMENT '现病史',
                               `past_history` TEXT COMMENT '既往史',
                               `diagnosis` TEXT COMMENT '初步诊断',
                               `doctor_advice` TEXT COMMENT '医生建议',
                               `recheck_result` TEXT COMMENT '复查结果',
                               `create_time` DATETIME COMMENT '填写时间',
                               `update_time` DATETIME COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病例主表';

CREATE TABLE `case_version` (
                                `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                                `case_id` VARCHAR(64) NOT NULL COMMENT '病例ID，如：CASE123',
                                `version_code` VARCHAR(100) COMMENT '版本号，例如：J18.9:1:240519124522',
                                `ipfs_hash` VARCHAR(255) COMMENT 'IPFS 内容哈希',
                                `summary` TEXT COMMENT '病情简要描述（链下展示用）',
                                `editor` VARCHAR(100) COMMENT '编辑人链上地址',
                                `info_attachments` JSON COMMENT '检查报告ipfs地址(多个)',
                                `create_time` DATETIME COMMENT '版本上传时间',
                                `chain_tx_hash` VARCHAR(255) COMMENT '链上交易哈希（方便追踪）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='病例版本信息表';
