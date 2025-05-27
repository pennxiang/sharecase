package com.sharedcase.service.impl;

import com.UserRegistry;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.config.ContractConfig;
import com.sharedcase.dao.UserMapper;
import com.sharedcase.entity.User;
import com.sharedcase.service.UserService;
import com.sharedcase.util.AESUtil;
import com.sharedcase.util.FiscoUtil;
import com.sharedcase.util.HashUtil;
import org.fisco.bcos.sdk.v3.BcosSDK;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * ClassName: UserServiceImpl
 * Package: com.sharedcase.service.impl
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FiscoUtil fiscoUtil;

    @Autowired
    private ContractConfig contractConfig;

    @Override
    public User register(User user) {
        try {
            // 1. 对身份证号加密后查重
            String encryptedIdCard = AESUtil.encrypt(user.getIdCard());
            String idCardHash = HashUtil.sha256(user.getIdCard());

            // 加载注册合约
            String contractAddress = contractConfig.getAddress("userRegistry");

            BcosSDK bcosSDK = fiscoUtil.bcosSDK();
            Client client = fiscoUtil.fiscoClient(bcosSDK);
            CryptoKeyPair keyPair = fiscoUtil.cryptoKeyPair(client);
            UserRegistry userRegistry = fiscoUtil.loadContract(
                    UserRegistry.class,
                    contractAddress,
                    client,
                    keyPair
            );
            logger.info("UserRegistry address: " + contractAddress);
            logger.info("Client group: " + client.getGroup()); // 确保是 group0
            logger.info("Caller address: " + keyPair.getAddress()); // 确保和部署者一致

            // 查看是否已经注册
            logger.info("开始调用 isRegistered...");
            boolean exists = userRegistry.isRegistered(idCardHash);
            logger.info("调用成功：" + exists);
            if (exists) {
                throw new RuntimeException("该用户已经被注册");
            }

            // 2. 加密密码
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // 3. 设置加密身份证号字段，原字段设 null（防止误存明文）
            user.setIdCard(encryptedIdCard);
            user.setChainAddress(null); // 先置空，后面写入

            // 4. 写入数据库
            this.save(user);

            // 5. 上链处理（存身份证哈希值 + 加密身份证号）
            userRegistry.register(idCardHash, encryptedIdCard);
            String chainAddress = userRegistry.getRegistrant(idCardHash);

            // 6. 回写链上地址
            user.setChainAddress(chainAddress);
            this.updateById(user);

            return user;

        } catch (Exception e) {
            throw new RuntimeException("用户注册失败: " + e.getMessage(), e);
        }
    }



    @Override
    public User login(String loginId, String password) {
        User user = null;

        try {
            if (loginId == null || loginId.isBlank()) {
                throw new IllegalArgumentException("登录账号不能为空");
            }

            // 尝试按身份证查（加密后比对）
            if (loginId.length() == 18) {
                String encryptedIdCard = AESUtil.encrypt(loginId);
                user = lambdaQuery()
                        .eq(User::getIdCard, encryptedIdCard)
                        .one();
            }

            // 如果未找到，再尝试按手机号查
            if (user == null && loginId.length() == 11 && loginId.matches("\\d{11}")) {
                user = lambdaQuery()
                        .eq(User::getPhone, loginId)
                        .one();
            }

            // 校验密码
            if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
                return null; // 或抛异常 "账号或密码错误"
            }

            return user;

        } catch (Exception e) {
            throw new RuntimeException("登录失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getUserRole(String idCard) {
        User user = null;
        try {
            String encryptedIdCard = AESUtil.encrypt(idCard);
            user = lambdaQuery()
                    .eq(User::getIdCard, encryptedIdCard)
                    .one();
            return user.getRole();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateUserInfo(User user) {
        // 处理密码更新（如果前端传了新密码）
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        // 允许修改字段
        if (user.getPhone() != null) {
            user.setPhone(user.getPhone());
        }

        if (user.getWorkId() != null) {
            user.setWorkId(user.getWorkId());
        }

        // 禁止更改身份证和链上地址
        user.setIdCard(null);
        user.setChainAddress(null);

        return this.updateById(user);
    }
}
