package com.sharedcase.dao.impl;

import com.sharedcase.annotation.DataSource;
import com.sharedcase.dao.UserMapper;
import com.sharedcase.enums.DataSourceType;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: UserMapperImpl
 * Package: com.sharedcase.dao.impl
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:26
 */
@Mapper
@DataSource(value = DataSourceType.SLAVE)
public class UserMapperImpl{
}
