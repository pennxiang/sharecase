INSERT INTO `case_detail` (
    `case_id`, `title`, `icd_code`, `hospital_id`, `user_id`, `doctor_id`,
    `chief_complaint`, `present_illness_history`, `past_history`,
    `diagnosis`, `doctor_advice`, `recheck_result`,
    `create_time`, `update_time`
) VALUES (
             'CASE123',
             '高血压二级诊断',
             'I10',
             1,
             1, -- user_id：张三
             2, -- doctor_id：李四
             '头晕、乏力三日，加重一天',
             '近期熬夜较多，精神压力大，血压波动明显',
             '既往有高血压病史，偶尔服药',
             '原发性高血压二级，风险中等',
             '建议低盐饮食，保持良好作息，按时服药',
             '血压控制稳定，建议继续现有方案',
             NOW(), NOW()
         );
INSERT INTO `case_version` (
    `case_id`, `version_code`, `ipfs_hash`, `summary`, `editor`,
    `info_attachments`, `create_time`, `chain_tx_hash`
) VALUES (
             'CASE123',
             'I10:1:240519140000',
             'QmXyzFakeIpfsHash001',
             '病人血压略高，风险可控，继续观察',
             '0xDoctorB',
             '["QmFileHash001", "QmFileHash002"]',
             NOW(),
             '0xFakeTxHash123456'
         );
