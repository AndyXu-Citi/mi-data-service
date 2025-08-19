package com.marathon.controller;

import com.marathon.common.api.R;
import com.marathon.domain.entity.User;
import com.marathon.domain.vo.UserInfo;
import com.marathon.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author marathon
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户名密码登录")
    @PostMapping("/login")
    public R<String> login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    /**
     * 微信小程序登录
     */
    @Operation(summary = "微信小程序登录", description = "通过微信授权码登录")
    @PostMapping("/wx-login")
    public R<UserInfo> wxLogin(@RequestParam String code) {
        return userService.wxLogin(code);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/{userId}")
    public R<User> getUserInfo(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    /**
     * 更新用户信息
     */
    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    @PutMapping
    public R<Boolean> updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }

    /**
     * 注册用户
     */
    @Operation(summary = "注册用户", description = "注册新用户")
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "用户修改自己的密码")
    @PostMapping("/change-password")
    public R<Boolean> changePassword(@RequestParam Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return userService.changePassword(userId, oldPassword, newPassword);
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码", description = "管理员重置用户密码")
    @PostMapping("/reset-password")
    public R<Boolean> resetPassword(@RequestParam Long userId, @RequestParam String password) {
        return userService.resetPassword(userId, password);
    }

    /**
     * 获取用户列表
     */
    @Operation(summary = "获取用户列表", description = "分页获取用户列表")
    @GetMapping("/list")
    public R<?> getUserList(User user, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getUserList(user, pageNum, pageSize);
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{userId}")
    public R<Boolean> deleteUser(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    /**
     * 更新用户状态
     */
    @Operation(summary = "更新用户状态", description = "启用或停用用户")
    @PutMapping("/status")
    public R<Boolean> updateUserStatus(@RequestParam Long userId, @RequestParam Integer status) {
        return userService.updateUserStatus(userId, status);
    }
}