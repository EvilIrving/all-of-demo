package com.ygkj.water.project.strategy;

import com.ygkj.water.project.mapper.HikCameraMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component("cameraStrategy")
public class CameraStrategy implements Strategy{

    private static final String TYPE = "视频";

    private HikCameraMapper hikCameraMapper;

    public CameraStrategy(@Autowired HikCameraMapper hikCameraMapper) {
        this.hikCameraMapper = hikCameraMapper;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public Collection records(String codes) {
        return hikCameraMapper.candidateCamera(Arrays.asList(codes.split(",")));
    }
}
