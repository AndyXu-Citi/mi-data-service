package com.marathon.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    String activeCondition;
    String searchKeyword;
    List<String> searchHistory;
    List<String> hotKeywords;
    List<EventVO> searchResults;
    Boolean loading;
    String loadStatus;
    Boolean showFilter;
    Boolean showResults;
    Filter filter;
    List<Options> cityOptions;
    List<Options> typeOptions;
    Long pageNum;
    Long pageSize;
    Boolean hasMore;
}
