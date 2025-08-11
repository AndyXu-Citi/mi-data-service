package com.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marathon.domain.entity.EventItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 赛事项目Mapper接口
 *
 * @author marathon
 */
@Mapper
public interface EventItemMapper extends BaseMapper<EventItem> {
}