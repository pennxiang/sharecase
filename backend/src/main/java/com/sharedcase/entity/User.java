package com.sharedcase.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;


/**
 * ClassName: User
 * Package: com.sharedcase.entity
 * Description:
 *      用户表，存放在主库中
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 12:59
 */
@Data
@TableName("user")
public class User {

    /**
     * 主键，存放在表里
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 系统登录密码，加密存储
     */
    private String password;

    /**
     * 身份证号，加密存储
     */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String idCard;

    /**
     * 角色，例如 患者、医生、医保局工作人员
     */
    private String role;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 链上地址
     */
    @TableField(updateStrategy = FieldStrategy.NOT_NULL)
    private String chainAddress;

    /**
     * 工作单位id
     */
    private Long workId;
}
