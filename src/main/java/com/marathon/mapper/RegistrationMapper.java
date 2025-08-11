package com.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marathon.domain.entity.Registration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报名Mapper接口
 *
 * @author marathon
 */
@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {
}