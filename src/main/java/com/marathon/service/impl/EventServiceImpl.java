package com.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.marathon.common.api.R;
import com.marathon.domain.entity.Event;
import com.marathon.domain.entity.EventItem;
import com.marathon.mapper.EventItemMapper;
import com.marathon.mapper.EventMapper;
import com.marathon.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 赛事服务实现类
 *
 * @author marathon
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    private final EventItemMapper eventItemMapper;

    @Override
    public R<?> getEventList(Event event, Integer pageNum, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(event.getEventName()), Event::getEventName, event.getEventName())
                .eq(event.getEventType() != null, Event::getEventType, event.getEventType())
                .eq(event.getEventStatus() != null, Event::getEventStatus, event.getEventStatus())
                .like(StringUtils.hasText(event.getProvince()), Event::getProvince, event.getProvince())
                .like(StringUtils.hasText(event.getCity()), Event::getCity, event.getCity())
                .orderByDesc(Event::getCreateTime);

        // 分页查询
        Page<Event> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return R.ok(page);
    }

    @Override
    public R<Event> getEventDetail(Long eventId) {
        if (eventId == null) {
            return R.fail("赛事ID不能为空");
        }
        Event event = getById(eventId);
        if (event == null) {
            return R.fail("赛事不存在");
        }
        return R.ok(event);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addEvent(Event event) {
        if (event == null) {
            return R.fail("赛事信息不能为空");
        }
        // 设置默认值
        event.setCreateTime(LocalDateTime.now());
        event.setViewCount(0);
        event.setFavoriteCount(0);
        event.setRegistrationCount(0);
        event.setCurrentParticipants(0);
        if (event.getEventStatus() == null) {
            event.setEventStatus(1); // 默认未开始
        }
        boolean result = save(event);
        return result ? R.ok(true, "添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateEvent(Event event) {
        if (event == null || event.getEventId() == null) {
            return R.fail("赛事ID不能为空");
        }
        event.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(event);
        return result ? R.ok(true, "更新成功") : R.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteEvent(Long eventId) {
        if (eventId == null) {
            return R.fail("赛事ID不能为空");
        }
        // 删除赛事项目
        LambdaQueryWrapper<EventItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EventItem::getEventId, eventId);
        eventItemMapper.delete(queryWrapper);
        // 删除赛事
        boolean result = removeById(eventId);
        return result ? R.ok(true, "删除成功") : R.fail("删除失败");
    }

    @Override
    public R<List<EventItem>> getEventItemList(Long eventId) {
        if (eventId == null) {
            return R.fail("赛事ID不能为空");
        }
        LambdaQueryWrapper<EventItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EventItem::getEventId, eventId)
                .orderByAsc(EventItem::getCreateTime);
        List<EventItem> eventItems = eventItemMapper.selectList(queryWrapper);
        return R.ok(eventItems);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> addEventItem(EventItem eventItem) {
        if (eventItem == null || eventItem.getEventId() == null) {
            return R.fail("参数不能为空");
        }
        // 检查赛事是否存在
        Event event = getById(eventItem.getEventId());
        if (event == null) {
            return R.fail("赛事不存在");
        }
        // 设置默认值
        eventItem.setCreateTime(LocalDateTime.now());
        eventItem.setRegisteredNumber(0);
        if (eventItem.getStatus() == null) {
            eventItem.setStatus(1); // 默认开放
        }
        int result = eventItemMapper.insert(eventItem);
        return result > 0 ? R.ok(true, "添加成功") : R.fail("添加失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateEventItem(EventItem eventItem) {
        if (eventItem == null || eventItem.getItemId() == null) {
            return R.fail("项目ID不能为空");
        }
        eventItem.setUpdateTime(LocalDateTime.now());
        int result = eventItemMapper.updateById(eventItem);
        return result > 0 ? R.ok(true, "更新成功") : R.fail("更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteEventItem(Long itemId) {
        if (itemId == null) {
            return R.fail("项目ID不能为空");
        }
        int result = eventItemMapper.deleteById(itemId);
        return result > 0 ? R.ok(true, "删除成功") : R.fail("删除失败");
    }

    @Override
    public R<List<Event>> getHotEvents(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Event::getIsHot, 1)
                .orderByDesc(Event::getViewCount)
                .last("LIMIT " + limit);
        List<Event> events = list(queryWrapper);
        return R.ok(events);
    }

    @Override
    public R<List<Event>> getRecommendedEvents(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Event::getIsRecommended, 1)
                .orderByDesc(Event::getCreateTime)
                .last("LIMIT " + limit);
        List<Event> events = list(queryWrapper);
        return R.ok(events);
    }

    @Override
    public R<List<Event>> getUpcomingEvents(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(Event::getStartTime, now)
                .orderByAsc(Event::getStartTime)
                .last("LIMIT " + limit);
        List<Event> events = list(queryWrapper);
        return R.ok(events);
    }

    @Override
    public R<?> searchEvents(String keyword, Integer type, Integer status, String province, String city, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Event> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(keyword), Event::getEventName, keyword)
                .or()
                .like(StringUtils.hasText(keyword), Event::getIntroduction, keyword)
                .eq(type != null, Event::getEventType, type)
                .eq(status != null, Event::getEventStatus, status)
                .like(StringUtils.hasText(province), Event::getProvince, province)
                .like(StringUtils.hasText(city), Event::getCity, city)
                .orderByDesc(Event::getCreateTime);

        Page<Event> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return R.ok(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> updateEventStatus(Long eventId, Integer status) {
        if (eventId == null || status == null) {
            return R.fail("参数不能为空");
        }
        Event event = new Event();
        event.setEventId(eventId);
        event.setEventStatus(status);
        event.setUpdateTime(LocalDateTime.now());
        boolean result = updateById(event);
        return result ? R.ok(true, "状态更新成功") : R.fail("状态更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> incrementViewCount(Long eventId) {
        if (eventId == null) {
            return R.fail("赛事ID不能为空");
        }
        Event event = getById(eventId);
        if (event == null) {
            return R.fail("赛事不存在");
        }
        event.setViewCount(event.getViewCount() + 1);
        boolean result = updateById(event);
        return result ? R.ok(true) : R.fail("更新失败");
    }
}