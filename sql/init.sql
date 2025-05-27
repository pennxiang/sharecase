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