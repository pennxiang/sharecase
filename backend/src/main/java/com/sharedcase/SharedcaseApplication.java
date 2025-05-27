package com.sharedcase;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.sharedcase.annotation.DataSource;
import com.sharedcase.config.IpfsConfig;
import com.sharedcase.enums.DataSourceType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.sharedcase.dao")
public class SharedcaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(SharedcaseApplication.class, args);
    }

}
