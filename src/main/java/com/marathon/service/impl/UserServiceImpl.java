package com.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marathon.common.api.R;
import com.marathon.common.security.JwtTokenUtil;
import com.marathon.domain.entity.User;
import com.marathon.mapper.UserMapper;
import com.marathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 *
 * @author marathon
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public R<String> login(String username, String password) {
        // 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return R.fail("用户名或密码不能为空");
        }

        // 查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = getOne(queryWrapper);

        // 用户不存在或密码错误
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return R.fail("用户名或密码错误");
        }

        // 账号被停用
        if (user.getStatus() != null && user.getStatus() == 1) {
            return R.fail("账号已被停用");
        }

        // 更新登录信息
        user.setLoginDate(LocalDateTime.now());
        updateById(user);

        // 生成token
        String token = jwtTokenUtil.generateToken(user.getUsername());
        return R.ok(token, "登录成功");
    }

    @Override
    public R<String> wxLogin(String code) {
        // 微信小程序登录逻辑
        // 调用微信API获取openid
        // 根据openid查询用户，不存在则创建
        // 生成token
        return R.ok("token", "登录成功");
    }

    @Override
    public R<User> getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            return R.fail("用户不存在");
        }
        // 清除敏感信息
        user.setPassword(null);
        return R.ok(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateUserInfo(User user) {
        if (user.getUserId() == null) {
            return R.fail("用户ID不能为空");
        }
        // 不允许修改用户名和密码
        user.setUsername(null);
        user.setPassword(null);
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);
        return result ? R.ok(true, "更新成功") : R.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> register(User user) {
        // 参数校验
        if (!StringUtils.hasText(user.getUsername()) || !StringUtils.hasText(user.getPassword())) {
            return R.fail("用户名或密码不能为空");
        }

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (count(queryWrapper) > 0) {
            return R.fail("用户名已存在");
        }

        // 设置默认值
        user.setUserType(0); // 普通用户
        user.setStatus(0); // 正常状态
        user.setCreateTime(LocalDateTime.now());
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        boolean result = save(user);
        return result ? R.ok(true, "注册成功") : R.fail("注册失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> changePassword(Long userId, String oldPassword, String newPassword) {
        // 参数校验
        if (userId == null || !StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            return R.fail("参数不能为空");
        }

        // 查询用户
        User user = getById(userId);
        if (user == null) {
            return R.fail("用户不存在");
        }

        // 校验旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return R.fail("旧密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);
        return result ? R.ok(true, "密码修改成功") : R.fail("密码修改失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> resetPassword(Long userId, String password) {
        // 参数校验
        if (userId == null || !StringUtils.hasText(password)) {
            return R.fail("参数不能为空");
        }

        // 查询用户
        User user = getById(userId);
        if (user == null) {
            return R.fail("用户不存在");
        }

        // 重置密码
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);
        return result ? R.ok(true, "密码重置成功") : R.fail("密码重置失败");
    }

    @Override
    public R<?> getUserList(User user, Integer pageNum, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(user.getUsername()), User::getUsername, user.getUsername())
                .like(StringUtils.hasText(user.getNickname()), User::getNickname, user.getNickname())
                .like(StringUtils.hasText(user.getPhone()), User::getPhone, user.getPhone())
                .eq(user.getStatus() != null, User::getStatus, user.getStatus())
                .eq(user.getUserType() != null, User::getUserType, user.getUserType())
                .orderByDesc(User::getCreateTime);

        // 分页查询
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 清除敏感信息
        page.getRecords().forEach(u -> u.setPassword(null));

        return R.ok(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteUser(Long userId) {
        if (userId == null) {
            return R.fail("用户ID不能为空");
        }
        boolean result = removeById(userId);
        return result ? R.ok(true, "删除成功") : R.fail("删除失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateUserStatus(Long userId, Integer status) {
        if (userId == null || status == null) {
            return R.fail("参数不能为空");
        }
        User user = new User();
        user.setUserId(userId);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(user);
        return result ? R.ok(true, "状态更新成功") : R.fail("状态更新失败");
    }
}