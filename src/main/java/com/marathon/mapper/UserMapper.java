package com.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marathon.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 *
 * @author marathon
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}