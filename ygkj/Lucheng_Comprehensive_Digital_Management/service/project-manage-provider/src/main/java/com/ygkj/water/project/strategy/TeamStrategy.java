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
public class TeamStrategy implements Strategy{

    private static final String TYPE = "队伍";

    private EmergencySupportMapper emergencySupportMapper;

    public TeamStrategy(@Autowired EmergencySupportMapper emergencySupportMapper) {
        this.emergencySupportMapper = emergencySupportMapper;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection records(String codes) {
        return emergencySupportMapper.candidateTeam(Arrays.asList(codes.split(",")));
    }
}
