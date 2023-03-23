package com.ygkj.river.adapters;

import com.ygkj.river.mapper.CommonCountMapper;
import com.ygkj.enums.AbstractStationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AreaCountAdapter implements CountAdapter {

    private static String AREA_COLUMN = "area_name";

    private CommonCountMapper commonCountMapper;

    public static void setAreaColumn(String areaColumn) {
        AREA_COLUMN = areaColumn;
    }

    public AreaCountAdapter(@Autowired CommonCountMapper commonCountMapper) {
        this.commonCountMapper = commonCountMapper;
    }

    @Override
    public List<Map<String, Integer>> countNumberByColumn(AbstractStationType countType) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "河道河景");
        return commonCountMapper.CommonCountByColumn(countType.table(), AREA_COLUMN, map);
    }
}
