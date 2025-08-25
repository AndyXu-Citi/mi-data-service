package com.marathon.service;

import com.marathon.common.api.R;
import com.marathon.domain.entity.Event;
import com.marathon.domain.vo.SearchParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 赛事服务接口
 *
 * @author marathon
 */
public interface EventService {

    /**
     * 获取赛事列表
     *
     * @param event    查询条件
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 赛事列表
     */
    R<?> getEventList(Event event, Integer pageNum, Integer pageSize);
    R<?> getEventList();

    /**
     * 获取赛事详情
     *
     * @param eventId 赛事ID
     * @return 赛事详情
     */
    R<Event> getEventDetail(Long eventId);

    /**
     * 添加赛事
     *
     * @param event 赛事信息
     * @return 添加结果
     */
    R<Boolean> addEvent(Event event);

    /**
     * 更新赛事
     *
     * @param event 赛事信息
     * @return 更新结果
     */
    R<Boolean> updateEvent(Event event);

    /**
     * 删除赛事
     *
     * @param eventId 赛事ID
     * @return 删除结果
     */
    R<Boolean> deleteEvent(Long eventId);


    /**
     * 获取热门赛事
     *
     * @param limit 数量限制
     * @return 热门赛事列表
     */
    R<List<Event>> getHotEvents(Integer limit);

    /**
     * 获取推荐赛事
     *
     * @param limit 数量限制
     * @return 推荐赛事列表
     */
    R<List<Event>> getRecommendedEvents(Integer limit);

    /**
     * 获取即将开始的赛事
     *
     * @param limit 数量限制
     * @return 即将开始的赛事列表
     */
    R<List<Event>> getUpcomingEvents(Integer limit);

    /**
     * 搜索赛事
     *
     * @param keyword  关键词
     * @param type     赛事类型
     * @param status   赛事状态
     * @param province 省份
     * @param city     城市
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @return 搜索结果
     */
    R<?> searchEvents(String keyword, Integer type, Integer status, String province, String city, Integer pageNum, Integer pageSize);
    R<?> searchEventsMock(SearchParam param);

    /**
     * 更新赛事状态
     *
     * @param eventId 赛事ID
     * @param status  状态
     * @return 更新结果
     */
    R<Boolean> updateEventStatus(Long eventId, Integer status);

    /**
     * 增加赛事浏览量
     *
     * @param eventId 赛事ID
     * @return 更新结果
     */
    R<Boolean> incrementViewCount(Long eventId);

    String handleUpload(MultipartFile file, String folderName, Long eventId);
}