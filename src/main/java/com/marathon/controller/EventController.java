package com.marathon.controller;

import com.marathon.common.api.R;
import com.marathon.domain.entity.Event;
import com.marathon.domain.entity.EventItem;
import com.marathon.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 赛事控制器
 *
 * @author marathon
 */
@Tag(name = "赛事管理", description = "赛事相关接口")
@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * 获取赛事列表
     */
    @Operation(summary = "获取赛事列表", description = "分页获取赛事列表")
    @GetMapping("/list")
    public R<?> getEventList(Event event, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return eventService.getEventList(event, pageNum, pageSize);
    }

    /**
     * 获取赛事详情
     */
    @Operation(summary = "获取赛事详情", description = "根据赛事ID获取赛事详情")
    @GetMapping("/{eventId}")
    public R<Event> getEventDetail(@Parameter(description = "赛事ID") @PathVariable Long eventId) {
        return eventService.getEventDetail(eventId);
    }

    /**
     * 添加赛事
     */
    @Operation(summary = "添加赛事", description = "添加新赛事")
    @PostMapping
    public R<Boolean> addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    /**
     * 更新赛事
     */
    @Operation(summary = "更新赛事", description = "更新赛事信息")
    @PutMapping
    public R<Boolean> updateEvent(@RequestBody Event event) {
        return eventService.updateEvent(event);
    }

    /**
     * 删除赛事
     */
    @Operation(summary = "删除赛事", description = "根据赛事ID删除赛事")
    @DeleteMapping("/{eventId}")
    public R<Boolean> deleteEvent(@Parameter(description = "赛事ID") @PathVariable Long eventId) {
        return eventService.deleteEvent(eventId);
    }

    /**
     * 获取赛事项目列表
     */
    @Operation(summary = "获取赛事项目列表", description = "根据赛事ID获取赛事项目列表")
    @GetMapping("/{eventId}/items")
    public R<?> getEventItems(@Parameter(description = "赛事ID") @PathVariable Long eventId) {
        return eventService.getEventItems(eventId);
    }

    /**
     * 添加赛事项目
     */
    @Operation(summary = "添加赛事项目", description = "添加赛事项目")
    @PostMapping("/item")
    public R<Boolean> addEventItem(@RequestBody EventItem eventItem) {
        return eventService.addEventItem(eventItem);
    }

    /**
     * 更新赛事项目
     */
    @Operation(summary = "更新赛事项目", description = "更新赛事项目信息")
    @PutMapping("/item")
    public R<Boolean> updateEventItem(@RequestBody EventItem eventItem) {
        return eventService.updateEventItem(eventItem);
    }

    /**
     * 删除赛事项目
     */
    @Operation(summary = "删除赛事项目", description = "根据项目ID删除赛事项目")
    @DeleteMapping("/item/{itemId}")
    public R<Boolean> deleteEventItem(@Parameter(description = "项目ID") @PathVariable Long itemId) {
        return eventService.deleteEventItem(itemId);
    }

    /**
     * 获取热门赛事
     */
    @Operation(summary = "获取热门赛事", description = "获取热门赛事列表")
    @GetMapping("/hot")
    public R<?> getHotEvents(@RequestParam(defaultValue = "5") Integer limit) {
        return eventService.getHotEvents(limit);
    }

    /**
     * 获取推荐赛事
     */
    @Operation(summary = "获取推荐赛事", description = "获取推荐赛事列表")
    @GetMapping("/recommend")
    public R<?> getRecommendEvents(@RequestParam(defaultValue = "5") Integer limit) {
        return eventService.getRecommendEvents(limit);
    }

    /**
     * 获取即将开始的赛事
     */
    @Operation(summary = "获取即将开始的赛事", description = "获取即将开始的赛事列表")
    @GetMapping("/upcoming")
    public R<?> getUpcomingEvents(@RequestParam(defaultValue = "5") Integer limit) {
        return eventService.getUpcomingEvents(limit);
    }

    /**
     * 搜索赛事
     */
    @Operation(summary = "搜索赛事", description = "根据关键词搜索赛事")
    @GetMapping("/search")
    public R<?> searchEvents(@RequestParam String keyword, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        return eventService.searchEvents(keyword, pageNum, pageSize);
    }

    /**
     * 更新赛事状态
     */
    @Operation(summary = "更新赛事状态", description = "更新赛事状态")
    @PutMapping("/status")
    public R<Boolean> updateEventStatus(@RequestParam Long eventId, @RequestParam Integer status) {
        return eventService.updateEventStatus(eventId, status);
    }

    /**
     * 增加赛事浏览量
     */
    @Operation(summary = "增加赛事浏览量", description = "增加赛事浏览量")
    @PutMapping("/view/{eventId}")
    public R<Boolean> incrementViewCount(@Parameter(description = "赛事ID") @PathVariable Long eventId) {
        return eventService.incrementViewCount(eventId);
    }
}