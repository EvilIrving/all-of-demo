package com.ygkj.gragh.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.List;
import java.util.Map;

@DS("soft")
public interface DataManagementMapper {

    List<Map<String, Object>> countOfflineNumbers(String start, String end);

    List<Map<String, Object>> countCurrentYearOfflineNumbersByMonth(String type);
}
