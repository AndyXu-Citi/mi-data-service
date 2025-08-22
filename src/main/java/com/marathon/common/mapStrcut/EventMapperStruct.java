package com.marathon.common.mapStrcut;
import com.marathon.common.enums.EventStatus;
import com.marathon.domain.entity.Event;
import com.marathon.domain.vo.EventVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


// componentModel = "spring"：生成Spring Bean，可通过@Autowired注入
@Mapper(componentModel = "spring")
public interface EventMapperStruct {

    // 基础映射：处理属性名不同和特殊转换
    @Mapping(source = "eventId", target = "id")
    @Mapping(source = "eventName", target = "title") // 实体name → VO eventName
    @Mapping(source = "eventStatus", target = "status", qualifiedByName = "getStatusDesc")
    @Mapping(source = "startTime", target = "date", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventVO toVO(Event entity);

    // 批量转换（MapStruct自动支持集合映射）
    List<EventVO> toVOList(List<Event> entities);

    // 自定义状态编码→描述的转换方法
    @Named("getStatusDesc")
    default String getStatusDesc(int statusCode) {
        try {
            return EventStatus.getByCode(statusCode).getDescription();
        } catch (IllegalArgumentException e) {
            return "未知状态";
        }
    }
}

