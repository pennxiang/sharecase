package com.sharedcase.controller;

import com.sharedcase.DTO.UserDTO;
import com.sharedcase.entity.AjaxResult;
import com.sharedcase.entity.User;
import com.sharedcase.service.UserService;
import com.sharedcase.util.DesensitizeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: UserController
 * Package: com.sharedcase.controller
 * Description:
 *
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
        User created = userService.register(user);
        return AjaxResult.success("注册成功", created);
    }

    @Operation(summary = "登录账号")
    @PostMapping("/login")
    public AjaxResult login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return AjaxResult.success("登录成功");
        } else {
            return AjaxResult.error("用户名或密码错误");
        }
    }

    @Operation(summary = "根据身份证获取用户信息")
    @GetMapping("/by-idCard")
    public AjaxResult findByIdCard(@RequestParam String idCard) {
        User user = userService.findByIdCard(idCard);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        String phone = DesensitizeUtil.maskPhone(user.getPhone());
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(phone);
        userDTO.setIdCard(idCard);
        userDTO.setName(user.getName());
        userDTO.setWorkId(user.getWorkId());
        return AjaxResult.success(userDTO);
    }

    @Operation(summary = "根据id查用户")
    @GetMapping("/{id}")
    public AjaxResult findById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return AjaxResult.error("用户不存在");
        }
        String phone = DesensitizeUtil.maskPhone(user.getPhone());
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(phone);
        userDTO.setIdCard(user.getIdCard());
        userDTO.setName(user.getName());
        userDTO.setWorkId(user.getWorkId());
        return AjaxResult.success(userDTO);
    }
}
