package com.sharedcase.util;

import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.config.exceptions.ConfigException;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * ClassName: FiscoUtil
 * Package: com.sharedcase.util
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 11:48
 */
public class FiscoUtil {

    private static final Logger logger = LoggerFactory.getLogger(FiscoUtil.class);

    @Value("${fisco.sdkConfigPath}")
    private String sdkConfigPath;

    // 注入合约地址 map
    @Value("#{${fisco.contract.addresses}}")
    private Map<String, String> contractAddresses;

    /**
     * 初始化 BcosSDK 实例
     */
    @Bean
    public BcosSDK bcosSDK() {
        try {
            logger.info("正在加载 FISCO BCOS SDK 配置：{}", sdkConfigPath);
            return BcosSDK.build(sdkConfigPath);
        } catch (Exception e) {
            logger.error("加载 SDK 配置失败：{}", e.getMessage(), e);
            throw new RuntimeException("FISCO BCOS SDK 初始化失败", e);
        }
    }

    /**
     * 获取默认 group 的 Client（默认 group0）
     */
    @Bean
    public Client fiscoClient(BcosSDK bcosSDK) {
        Client client = bcosSDK.getClient("group0");
        logger.info("已连接 FISCO BCOS 客户端，群组ID：{}", client.getGroup());
        return client;
    }

    /**
     * 获取默认的密钥对
     */
    @Bean
    public CryptoKeyPair cryptoKeyPair(Client fiscoClient) {
        CryptoKeyPair keyPair = fiscoClient.getCryptoSuite().getCryptoKeyPair();
        logger.info("获取密钥对，地址：{}", keyPair.getAddress());
        return keyPair;
    }

    /**
     * 获取合约地址
     */
    public String contractAddress(String contractName) {
        return Optional.ofNullable(contractAddresses.get(contractName))
                .orElseThrow(() -> new RuntimeException("未配置合约地址: " + contractName));
    }

    /**
     * 加载任意合约实例（泛型封装）
     */
    public <T extends Contract> T loadContract(Class<T> clazz, String address, Client client, CryptoKeyPair keyPair) {
        try {
            Method loadMethod = clazz.getMethod("load", String.class, Client.class, CryptoKeyPair.class);
            @SuppressWarnings("unchecked")
            T contract = (T) loadMethod.invoke(null, address, client, keyPair);
            return contract;
        } catch (Exception e) {
            logger.error("加载合约失败：{}", e.getMessage(), e);
            throw new RuntimeException("合约加载失败: " + clazz.getSimpleName(), e);
        }
    }
}
