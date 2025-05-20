package com.sharedcase.annotation;

import com.sharedcase.enums.DataSourceType;

import java.lang.annotation.*;

/**
 * ClassName: DataSource
 * Package: com.sharedcase.annotation
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:28
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    /**
     * 切换数据源名称
     */
    public DataSourceType value() default DataSourceType.MASTER;
}