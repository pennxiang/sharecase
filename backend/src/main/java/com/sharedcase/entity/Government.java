package com.sharedcase.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * ClassName: Goverment
 * Package: com.sharedcase.entity
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 13:31
 */
@Data
public class Government {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;
}
