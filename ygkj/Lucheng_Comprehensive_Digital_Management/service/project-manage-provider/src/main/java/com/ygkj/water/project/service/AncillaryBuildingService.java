package com.ygkj.water.project.service;

import java.util.List;
import java.util.Map;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-15 15:52
 */
public interface AncillaryBuildingService {
    List<Map<String,Object>> selectAncillaryBuilding(String projectCategory, String prcd);
}
