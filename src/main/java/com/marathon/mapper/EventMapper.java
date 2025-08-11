package com.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marathon.domain.entity.Event;
import org.apache.ibatis.annotations.Mapper;

/**
 * 赛事Mapper接口
 *
 * @author marathon
 */
@Mapper
public interface EventMapper extends BaseMapper<Event> {
}