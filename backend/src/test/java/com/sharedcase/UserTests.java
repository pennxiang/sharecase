package com.sharedcase;

import com.sharedcase.dao.UserMapper;
import com.sharedcase.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * ClassName: UserTests
 * Package: java.com.sharedcase
 * Description:
 *
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 23:29
 */
@SpringBootTest
public class UserTests {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void insertTestUsers() {
        // 用户1 - 患者
        User user1 = new User();
        user1.setName("张三");
        user1.setPassword(passwordEncoder.encode("123456")); // 密码加密
        user1.setIdCard(passwordEncoder.encode("110101199003077891")); // 可选加密
        user1.setRole("患者");
        user1.setPhone("13800000001");
        user1.setChainAddress("0xUserA");
        user1.setWorkId(1L);
        userMapper.insert(user1);

        // 用户2 - 医生
        User user2 = new User();
        user2.setName("李四");
        user2.setPassword(passwordEncoder.encode("doctor123"));
        user2.setIdCard(passwordEncoder.encode("110101198806058752"));
        user2.setRole("医生");
        user2.setPhone("13900000002");
        user2.setChainAddress("0xDoctorB");
        user2.setWorkId(2L);
        userMapper.insert(user2);

        // 用户3 - 医保局人员
        User user3 = new User();
        user3.setName("王五");
        user3.setPassword(passwordEncoder.encode("insurance456"));
        user3.setIdCard(passwordEncoder.encode("110101197501012211"));
        user3.setRole("医保局工作人员");
        user3.setPhone("13700000003");
        user3.setChainAddress("0xInsuranceC");
        user3.setWorkId(3L);
        userMapper.insert(user3);

        System.out.println("✅ 用户测试数据插入完成！");
    }
}
