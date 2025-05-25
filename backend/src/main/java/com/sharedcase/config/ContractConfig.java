package com.sharedcase.config;

import com.UserRegistry;
import com.sharedcase.util.FiscoUtil;
import lombok.Data;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
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
@ConfigurationProperties(prefix = "fisco.contract")
public class ContractConfig {

    private Map<String, String> addresses;


    /**
     * 根据合约名称获取地址
     * @param contractName 合约配置名（如 userRegistry）
     * @return 对应的链上地址
     */
    public String getAddress(String contractName) {
        String address = addresses.get(contractName);
        if (address == null) {
            throw new IllegalArgumentException("未找到合约地址配置: " + contractName);
        }
        return address;
    }
}
