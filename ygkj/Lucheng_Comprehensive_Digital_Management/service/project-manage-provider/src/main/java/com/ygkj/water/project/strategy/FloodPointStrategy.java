package com.ygkj.water.project.strategy;

import com.ygkj.project.model.FloodPoint;
import com.ygkj.water.project.mapper.FloodPointMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component("floodPointStrategy")
public class FloodPointStrategy implements Strategy{

    private static final String TYPE = "积水";

    private FloodPointMapper floodPointMapper;

    public FloodPointStrategy(@Autowired FloodPointMapper floodPointMapper) {
        this.floodPointMapper = floodPointMapper;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection records(String codes) {
        List<FloodPoint> floodPoints = floodPointMapper.candidateFloodPoints(Arrays.asList(codes.split(",")));
        return floodPoints;
    }
}
