package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.CameraRecordMapper;
import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.service.CameraRecordService;
import com.ygkj.gragh.utils.DistanceUtil;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.gragh.vo.request.CameraRangeQueryVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CameraServiceImpl implements CameraRecordService {

    @Autowired
    private CameraRecordMapper cameraRecordMapper;

    @Override
    public List<HikvisionCameraRecord> cameras(CameraQueryVo cameraQueryVo) {
        return cameraRecordMapper.selectByCondition(cameraQueryVo);
    }

    @Override
    public List<HikvisionCameraRecord> camerasInRange(CameraRangeQueryVo cameraRangeQueryVo) {
        List<HikvisionCameraRecord> cameras = cameras(new CameraQueryVo());
        cameras = cameras.stream().filter(camera -> StringUtils.isNotBlank(camera.getLongitude()) && StringUtils.isNotBlank(camera.getLatitude()))
                .filter(camera ->
                        DistanceUtil.getDistance(cameraRangeQueryVo.getLatitude(),
                                cameraRangeQueryVo.getLongitude(), Double.valueOf(camera.getLatitude()).doubleValue(), Double.valueOf(camera.getLongitude()).doubleValue())
                                .abs().doubleValue() <= cameraRangeQueryVo.getDistance()).collect(Collectors.toList());
        return cameras;
    }
}
