package com.ygkj.visualization.service;

import com.ygkj.visualization.model.WindFarm;

import java.util.List;
import java.util.Map;

public interface SiteService {

    List<Map<String,String>> siteTimeList();

    WindFarm loadWindFarm(Integer id);
}
