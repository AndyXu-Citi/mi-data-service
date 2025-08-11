package com.marathon.controller;

import com.marathon.common.api.R;
import com.marathon.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏控制器
 *
 * @author marathon
 */
@Tag(name = "收藏管理", description = "收藏相关接口")
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * 添加收藏
     */
    @Operation(summary = "添加收藏", description = "用户收藏赛事")
    @PostMapping
    public R<Boolean> addFavorite(@RequestParam Long userId, @RequestParam Long eventId) {
        return favoriteService.addFavorite(userId, eventId);
    }

    /**
     * 取消收藏
     */
    @Operation(summary = "取消收藏", description = "用户取消收藏赛事")
    @DeleteMapping
    public R<Boolean> cancelFavorite(@RequestParam Long userId, @RequestParam Long eventId) {
        return favoriteService.cancelFavorite(userId, eventId);
    }

    /**
     * 获取用户收藏列表
     */
    @Operation(summary = "获取用户收藏列表", description = "获取用户收藏的赛事列表")
    @GetMapping("/user/{userId}")
    public R<?> getUserFavorites(
            @Parameter(description = "用户ID") @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return favoriteService.getUserFavorites(userId, pageNum, pageSize);
    }

    /**
     * 检查是否已收藏
     */
    @Operation(summary = "检查是否已收藏", description = "检查用户是否已收藏赛事")
    @GetMapping("/check")
    public R<Boolean> checkFavorite(@RequestParam Long userId, @RequestParam Long eventId) {
        return favoriteService.checkFavorite(userId, eventId);
    }
}