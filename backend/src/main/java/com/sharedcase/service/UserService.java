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

    /**
     * 注册账号
     * 注册时自动将账号存放到fisco上，并返还一个哈希地址
     * @param user
     * @return
     */
    User register(User user);

    /**
     * 登录账号
     * @param idCard
     * @param password
     * @return
     */
    User login(String idCard, String password);

    /**
     * 获取用户身份
     */
    String getUserRole(String idCard);

    /**
     * 更新信息
     */
    boolean updateUserInfo(User user);

}
