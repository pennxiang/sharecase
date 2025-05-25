package com.sharedcase.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Component;

/**
 * ClassName: IpfsConfig
 * Package: com.sharedcase.config
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/25 16:40
 */
@Data
@Component
@ConfigurationProperties(prefix = "ipfs")
public class IpfsConfig {
    private String url;
    private String multiAddr;
    private String gateway;
}
