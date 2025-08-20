package com.marathon.service;

import com.marathon.common.api.R;
import com.marathon.domain.entity.User;
import com.marathon.domain.vo.UserInfo;

/**
 * 用户服务接口
 *
 * @author marathon
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    R<String> login(String username, String password);

    /**
     * 微信小程序登录
     *
     * @param code 微信授权码
     * @return 登录结果
     */
    R<UserInfo> wxLogin(String code);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    R<User> getUserInfo(Long userId);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新结果
     */
    R<Boolean> updateUserInfo(User user);

    /**
     * 注册用户
     *
     * @param user 用户信息
     * @return 注册结果
     */
    R<Boolean> register(User user);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    R<Boolean> changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     *
     * @param userId   用户ID
     * @param password 新密码
     * @return 重置结果
     */
    R<Boolean> resetPassword(Long userId, String password);

    /**
     * 获取用户列表
     *
     * @param user     查询条件
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 用户列表
     */
    R<?> getUserList(User user, Integer pageNum, Integer pageSize);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    R<Boolean> deleteUser(Long userId);

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态（0正常 1停用）
     * @return 更新结果
     */
    R<Boolean> updateUserStatus(Long userId, Integer status);
}