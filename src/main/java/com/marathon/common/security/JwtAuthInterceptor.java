package com.marathon.common.security;

import com.marathon.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT认证拦截器
 *
 * @author marathon
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从请求头中获取token
        String token = getTokenFromRequest(request);

        // 验证token
        if (StringUtils.hasText(token)) {
            try {
                boolean valid = jwtTokenUtil.validateToken(token);
                if (valid) {
                    // 从token中获取用户名
                    String username = jwtTokenUtil.getUsernameFromToken(token);
                    // 将用户名设置到请求属性中，方便后续使用
                    request.setAttribute("username", username);
                    return true;
                }
            } catch (Exception e) {
                log.error("Token验证失败", e);
                throw new UnauthorizedException("Token无效或已过期");
            }
        }

        throw new UnauthorizedException("未提供有效的Token");
    }

    /**
     * 从请求中获取token
     *
     * @param request 请求
     * @return token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}