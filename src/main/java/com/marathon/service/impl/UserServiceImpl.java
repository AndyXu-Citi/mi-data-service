package com.marathon.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marathon.common.api.R;
import com.marathon.common.security.JwtTokenUtil;
import com.marathon.domain.entity.User;
import com.marathon.domain.vo.UserInfo;
import com.marathon.mapper.UserMapper;
import com.marathon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.marathon.common.security.JwtTokenUtil.getTokenRedisKey;

/**
 * 用户服务实现类
 *
 * @author marathon
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 微信小程序appid和secret（实际项目中建议配置在配置文件中）
    private static final String APPID = "wx21a3d7f98708c369";
    private static final String SECRET = "abe589b46239a194c8424329c18547a5";
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    @Value("${jwt.expiration}")
    private Long expiration;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper; // 假设存在用户Mapper接口

//    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;
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

//         用户不存在或密码错误
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
    public R wxLogin(String code) {
        try {
            // 1. 调用微信接口获取openid和session_key
            String url = String.format(WX_LOGIN_URL, APPID, SECRET, code);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    String.class
            );
            log.error("微信小程序登录:" + response);
            Map result = (Map) JSONUtils.parse(response.getBody());
            // 2. 解析微信返回结果
//            JSONObject result = (JSONObject) JSONUtils.parse(response.getBody());
            String openid = result.get("openid").toString();
            String sessionKey = result.get("session_key").toString();

            // 检查是否有错误
            if (result.containsKey("errcode")) {
                String errMsg = result.get("errMsg").toString();

                return R.fail("登录失败: " + errMsg);
            }

            // 3. 根据openid查询或创建用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getOpenid, openid);
            User user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setOpenid(openid);
                user.setCreateTime(LocalDateTime.now());
                user.setUsername("andy123");
                user.setNickname("云里星河");
                user.setPassword("xuzhenyu123");
                userMapper.insert(user); // 创建新用户
            }

            // 4. 生成token并存储到Redis
            String token = jwtTokenUtil.generateToken(openid);

            //将用户ID和sessionKey存入Redis，设置过期时间
//            redisTemplate.opsForValue().set(
//                    "token:" + token,
//                    user.getUserId() + ":" + sessionKey,
//                    7,
//                    TimeUnit.DAYS
//            );
            redisTemplate.opsForValue().set(getTokenRedisKey(user.getOpenid()), token, expiration, TimeUnit.SECONDS);

            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getUserId());
            userInfo.setOpenId(user.getOpenid());
            userInfo.setTokenId(token);


            // 5. 返回token给前端
            return R.ok(userInfo);

        } catch (Exception e) {
            e.printStackTrace();
            return R.fail("登录异常，请稍后重试");
        }

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