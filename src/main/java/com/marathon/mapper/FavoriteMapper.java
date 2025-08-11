package com.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marathon.domain.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper接口
 *
 * @author marathon
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}