package com.ygkj.river.adapters;

import com.ygkj.enums.AbstractStationType;

import java.util.List;
import java.util.Map;

public interface CountAdapter {

    List<Map<String, Integer>> countNumberByColumn(AbstractStationType countType);
}
