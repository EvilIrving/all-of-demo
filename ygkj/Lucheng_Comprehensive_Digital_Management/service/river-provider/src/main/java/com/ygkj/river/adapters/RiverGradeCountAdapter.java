package com.ygkj.river.adapters;

import com.ygkj.river.mapper.CommonCountMapper;
import com.ygkj.enums.AbstractStationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RiverGradeCountAdapter implements CountAdapter {

    private static String RIVER_GRADE_COLUMN = "river_level";

    private CommonCountMapper commonCountMapper;

    public RiverGradeCountAdapter(@Autowired CommonCountMapper commonCountMapper) {
        this.commonCountMapper = commonCountMapper;
    }

    public static void setRiverGradeColumn(String riverGradeColumn) {
        RIVER_GRADE_COLUMN = riverGradeColumn;
    }

    @Override
    public List<Map<String, Integer>> countNumberByColumn(AbstractStationType countType) {
        return commonCountMapper.CommonCountByColumn(countType.table(), RIVER_GRADE_COLUMN, new HashMap<>());
    }
}
