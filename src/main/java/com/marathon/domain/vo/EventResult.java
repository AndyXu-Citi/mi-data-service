package com.marathon.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class EventResult {
    String searchKeyword;
    List<BannerList> bannerList;
    List<CategoryList> categoryList;
    List<EventVO> hotEvents;
    List<EventVO> upComingEvents;
}
