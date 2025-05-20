package com.sharedcase;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.sharedcase.annotation.DataSource;
import com.sharedcase.enums.DataSourceType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sharedcase.dao")
@DS("master")
public class SharedcaseApplication {
    public static void main(String[] args) {
        System.out.println("启动配置读取成功");
        SpringApplication.run(SharedcaseApplication.class, args);
    }

}
