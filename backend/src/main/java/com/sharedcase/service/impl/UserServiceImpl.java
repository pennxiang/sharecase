package com.sharedcase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.dao.UserMapper;
import com.sharedcase.entity.AjaxResult;
import com.sharedcase.entity.User;
import com.sharedcase.service.UserService;
import com.sharedcase.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        try {
            // 1. 对身份证号加密后查重
            String encryptedIdCard = AESUtil.encrypt(user.getIdCard());
            boolean exists = lambdaQuery()
                    .eq(User::getIdCard, encryptedIdCard)
                    .exists();

            if (exists) {
                throw new RuntimeException("该用户已经被注册");
            }

            // 2. 加密密码
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            // 3. 设置加密身份证号字段，原字段设 null（防止误存明文）
            user.setIdCard(encryptedIdCard);



            // 5. 上链处理（例如存身份证哈希值 + 用户地址）
//            String userHash = HashUtil.sha256(user.getIdCard() + "_salt"); // salt 可全局配置或 per-user 随机
//            String chainAddress = caseContractService.registerUser(userHash); // 返回链上地址或 hash

//            user.setChainAddress(chainAddress);
//            this.updateById(user); // 更新链地址字段

            // 4. 写入数据库
            this.save(user);
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

        // ⚠️ 不允许更新 idCard（防止密文被覆盖）
        // ⚠️ 不允许更新角色、链上地址等敏感信息

        return this.updateById(user);
    }
}
