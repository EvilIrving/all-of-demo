package com.ygkj.water.project.service.impl;

import com.ygkj.water.project.mapper.AncillaryBuildingMapper;
import com.ygkj.water.project.service.AncillaryBuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-15 15:55
 */
@Service
public class AncillaryBuildingServiceImpl implements AncillaryBuildingService {

    @Autowired
    private AncillaryBuildingMapper ancillaryBuildingMapper;
    @Override
    public List<Map<String, Object>> selectAncillaryBuilding(String projectCategory, String prcd) {
        return ancillaryBuildingMapper.selectAncillaryBuilding(projectCategory,prcd);
    }
}
