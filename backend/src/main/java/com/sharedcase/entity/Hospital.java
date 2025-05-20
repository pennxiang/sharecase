package com.sharedcase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * ClassName: Hospital
 * Package: com.sharedcase.entity
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 13:04
 */
@Data
public class Hospital {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;
}
