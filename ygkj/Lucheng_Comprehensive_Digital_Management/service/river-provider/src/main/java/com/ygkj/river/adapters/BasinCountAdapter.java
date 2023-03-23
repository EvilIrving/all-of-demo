package com.ygkj.river.adapters;

import com.ygkj.river.mapper.CommonCountMapper;
import com.ygkj.enums.AbstractStationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BasinCountAdapter implements CountAdapter {

    private static String BASIN_COLUMN = "basin";

    private CommonCountMapper commonCountMapper;

    public static void setBasinColumn(String basinColumn) {
        BASIN_COLUMN = basinColumn;
    }

    public BasinCountAdapter(@Autowired CommonCountMapper commonCountMapper) {
        this.commonCountMapper = commonCountMapper;
    }

    @Override
    public List<Map<String, Integer>> countNumberByColumn(AbstractStationType countType) {
        return commonCountMapper.CommonCountByColumn(countType.table(), BASIN_COLUMN, new HashMap<>());
    }
}
