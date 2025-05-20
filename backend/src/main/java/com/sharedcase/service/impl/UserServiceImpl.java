package com.sharedcase.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sharedcase.dao.UserMapper;
import com.sharedcase.entity.User;
import com.sharedcase.service.UserService;
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
        if (lambdaQuery().eq(User::getId, user.getIdCard()).exists()) {
            throw new RuntimeException("该用户已经被注册");
        }
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        this.save(user);
        return user;
    }

    @Override
    public User login(String idCard, String password) {
        User user = lambdaQuery().eq(User::getId, idCard).one();
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return user;
    }

    @Override
    public User findByIdCard(String idCard) {
        User user = userMapper.selectById(idCard);
        return user;
    }

    @Override
    public User findById(Long id) {
     return userMapper.selectById(id);
    }
}
