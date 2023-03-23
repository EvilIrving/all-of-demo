package com.ygkj.water.project.strategy;

import com.ygkj.water.project.mapper.EmergencySupportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author lxl
 */
@Component
public class WarehouseStrategy implements Strategy{

    private static final String TYPE = "仓库";

    private EmergencySupportMapper emergencySupportMapper;

    public WarehouseStrategy(@Autowired EmergencySupportMapper emergencySupportMapper) {
        this.emergencySupportMapper = emergencySupportMapper;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection records(String codes) {
        return emergencySupportMapper.candidateWareHouse(Arrays.asList(codes.split(",")));
    }
}
