package com.marathon.common.security;


import com.marathon.common.exception.BusinessException;
import com.marathon.domain.entity.CurrentUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Objects;

import static com.marathon.common.security.JwtTokenUtil.getTokenRedisKey;


/**
 * JWT认证过滤器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        // 获取请求头中的JWT
        String jwt = getJwtFromRequest(request);

        // 验证JWT
        if (StringUtils.hasText(jwt) && jwtTokenUtil.validateToken(jwt)) {
            // 从JWT中获取用户名
            String username = jwtTokenUtil.getUsernameFromToken(jwt);

            // 加载用户详情
            CurrentUser userDetails = (CurrentUser) userDetailsService.loadUserByUsername(username);

            Object token = redisTemplate.opsForValue().get(getTokenRedisKey(userDetails.getUsername()));
            if (Objects.isNull(token)) {
                throw new BusinessException("请重新登录");
            }

            // 创建认证对象
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 设置认证信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, servletResponse);
    }

    /**
     * 从请求头中获取JWT
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String bearerToken = request.getHeader("Authorization");
        String method = request.getMethod();
        String referer = request.getHeader("Referer");
        log.info("requestURI:{}", requestURI);
        log.info("bearerToken:{}", bearerToken);
        log.info("method:{}", method);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}