package com.marathon.util;

import com.marathon.common.exception.BusinessException;
import com.marathon.domain.entity.CurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @author 苏三
 * @date 2025/3/14 19:07
 */
public class UserUtil {


    public static CurrentUser getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new BusinessException("当前登录状态过期");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            throw new BusinessException("当前登录状态过期");
        }
        return (CurrentUser) authentication.getPrincipal();
    }
}