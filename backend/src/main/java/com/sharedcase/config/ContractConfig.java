package com.sharedcase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * ClassName: ContractConfig
 * Package: com.sharedcase.config
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/23 22:49
 */
@Data
@Component
@ConfigurationProperties(prefix = "contract")
public class ContractConfig {
    private Map<String, String> addresses;
}
