package com.marathon.common.security;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.marathon.domain.entity.CurrentUser;
import com.marathon.domain.entity.User;
import com.marathon.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.Collections;

/**
 * 用户详情服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        // 使用QueryWrapper查询用户（推荐方式，兼容性更好）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 微信小程序会通过OpenId去查找
        queryWrapper.eq("openid", username); // 直接使用字符串字段名
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 创建UserDetails对象
        return new CurrentUser(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getOpenid(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
                Lists.newArrayList("ROLE_USER"));
    }
}