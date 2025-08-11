package com.marathon.service;

import com.marathon.common.api.R;
import com.marathon.domain.entity.Favorite;

/**
 * 收藏服务接口
 *
 * @author marathon
 */
public interface FavoriteService {

    /**
     * 添加收藏
     *
     * @param userId  用户ID
     * @param eventId 赛事ID
     * @return 添加结果
     */
    R<Boolean> addFavorite(Long userId, Long eventId);

    /**
     * 取消收藏
     *
     * @param userId  用户ID
     * @param eventId 赛事ID
     * @return 取消结果
     */
    R<Boolean> cancelFavorite(Long userId, Long eventId);

    /**
     * 获取用户收藏列表
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 收藏列表
     */
    R<?> getUserFavorites(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 检查是否已收藏
     *
     * @param userId  用户ID
     * @param eventId 赛事ID
     * @return 是否已收藏
     */
    R<Boolean> checkFavorite(Long userId, Long eventId);
}