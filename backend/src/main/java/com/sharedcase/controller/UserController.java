package com.sharedcase.controller;

import com.UserRegistry;
import com.sharedcase.DTO.UserDTO;
import com.sharedcase.entity.AjaxResult;
import com.sharedcase.entity.User;
import com.sharedcase.service.UserService;
import com.sharedcase.util.AESUtil;
import com.sharedcase.util.DesensitizeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * ClassName: UserController
 * Package: com.sharedcase.controller
 * Description:
 *      用户管理
 * @author 向鹏
 * @version 1.0
 * @create 2025/5/19 14:21
 */
@Tag(name = "用户管理", description = "用户相关操作")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "注册账号")
    @PostMapping("/register")
    public AjaxResult register(@RequestBody User user) {
        try {
            User created = userService.register(user);
            return AjaxResult.success("注册成功", created);
        } catch (Exception e) {
            return AjaxResult.error("注册失败: " + e.getMessage());
        }
    }


    @Operation(summary = "登录账号")
    @PostMapping("/login")
    public AjaxResult login(@RequestParam String loginId, @RequestParam String password) {
        User user = userService.login(loginId, password);
        if (user != null) {
            return AjaxResult.success("登录成功");
        } else {
            return AjaxResult.error("用户名或密码错误");
        }
    }

    @Operation(summary = "根据身份证获取用户信息")
    @GetMapping("/by-idCard")
    public AjaxResult findByIdCard(@RequestParam String idCard) {
        try {
            if (idCard == null || idCard.length() != 18) {
                return AjaxResult.error("身份证号格式错误");
            }
            // 1. 加密身份证号
            String encryptedIdCard = AESUtil.encrypt(idCard);
            // 2. 查询用户
            User user = userService.lambdaQuery()
                    .eq(User::getIdCard, encryptedIdCard)
                    .one();

            if (user == null) {
                return AjaxResult.error("用户不存在");
            }

            // 3. 构造脱敏返回对象
            UserDTO userDTO = new UserDTO();
            userDTO.setPhone(DesensitizeUtil.maskPhone(user.getPhone())); // 脱敏手机号
            userDTO.setIdCard(DesensitizeUtil.maskIdCard(idCard));        // 脱敏身份证
            userDTO.setName(user.getName());
            userDTO.setWorkId(user.getWorkId());

            return AjaxResult.success(userDTO);

        } catch (Exception e) {
            return AjaxResult.error("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据id查用户")
    @GetMapping("/{id}")
    public AjaxResult findById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        // 身份信息脱敏
        String idCard = DesensitizeUtil.maskIdCard(user.getIdCard());
        String phone = DesensitizeUtil.maskPhone(user.getPhone());
        // 封装返回对象
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(phone);
        userDTO.setIdCard(idCard);
        userDTO.setName(user.getName());
        userDTO.setWorkId(user.getWorkId());
        return AjaxResult.success(userDTO);
    }

    @Operation(summary = "更新用户信息（可修改密码、手机号、姓名）")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody User user) {
        try {
            boolean ok = userService.updateUserInfo(user);
            if (!ok) {
                return AjaxResult.error("更新失败");
            }
            return AjaxResult.success("更新成功");
        } catch (Exception e) {
            return AjaxResult.error("更新异常: " + e.getMessage());
        }
    }

}
