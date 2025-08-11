package com.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marathon.common.api.R;
import com.marathon.domain.entity.Event;
import com.marathon.domain.entity.Favorite;
import com.marathon.mapper.EventMapper;
import com.marathon.mapper.FavoriteMapper;
import com.marathon.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏服务实现类
 *
 * @author marathon
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {

    private final EventMapper eventMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addFavorite(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            return R.fail("参数不能为空");
        }

        // 检查赛事是否存在
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            return R.fail("赛事不存在");
        }

        // 检查是否已收藏
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getEventId, eventId);
        if (count(queryWrapper) > 0) {
            return R.ok(true, "已收藏");
        }

        // 添加收藏
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setEventId(eventId);
        favorite.setCreateTime(LocalDateTime.now());
        boolean result = save(favorite);

        if (result) {
            // 更新赛事收藏数
            event.setFavoriteCount(event.getFavoriteCount() + 1);
            eventMapper.updateById(event);
        }

        return result ? R.ok(true, "收藏成功") : R.fail("收藏失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> cancelFavorite(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            return R.fail("参数不能为空");
        }

        // 查询收藏记录
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getEventId, eventId);
        Favorite favorite = getOne(queryWrapper);

        if (favorite == null) {
            return R.ok(true, "未收藏");
        }

        // 删除收藏
        boolean result = remove(queryWrapper);

        if (result) {
            // 更新赛事收藏数
            Event event = eventMapper.selectById(eventId);
            if (event != null && event.getFavoriteCount() > 0) {
                event.setFavoriteCount(event.getFavoriteCount() - 1);
                eventMapper.updateById(event);
            }
        }

        return result ? R.ok(true, "取消成功") : R.fail("取消失败");
    }

    @Override
    public R<?> getUserFavorites(Long userId, Integer pageNum, Integer pageSize) {
        if (userId == null) {
            return R.fail("用户ID不能为空");
        }

        // 分页查询收藏记录
        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                .orderByDesc(Favorite::getCreateTime);

        Page<Favorite> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        // 获取赛事详情
        List<Event> eventList = new ArrayList<>();
        for (Favorite favorite : page.getRecords()) {
            Event event = eventMapper.selectById(favorite.getEventId());
            if (event != null) {
                eventList.add(event);
            }
        }

        // 构建返回结果
        Page<Event> resultPage = new Page<>(pageNum, pageSize, page.getTotal());
        resultPage.setRecords(eventList);

        return R.ok(resultPage);
    }

    @Override
    public R<Boolean> checkFavorite(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            return R.fail("参数不能为空");
        }

        LambdaQueryWrapper<Favorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, userId)
                .eq(Favorite::getEventId, eventId);
        boolean isFavorite = count(queryWrapper) > 0;

        return R.ok(isFavorite);
    }
}