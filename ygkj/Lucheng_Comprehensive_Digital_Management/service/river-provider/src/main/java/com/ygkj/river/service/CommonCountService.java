package com.ygkj.river.service;

import java.util.List;
import java.util.Map;

public interface CommonCountService {

    List<Map<String, Integer>> count(String type, String column);
}
