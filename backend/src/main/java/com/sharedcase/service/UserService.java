package com.sharedcase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sharedcase.entity.User;

/**
 * ClassName: UserService
 * Package: com.sharedcase.service
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:20
 */
public interface UserService extends IService<User> {

    User register(User user);

    User login(String username, String password);

    User findByIdCard(String idCard);

    User findById(Long id);
}
