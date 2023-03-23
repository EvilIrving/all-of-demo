package com.ygkj.gragh.service;

import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.gragh.vo.request.CameraRangeQueryVo;

import java.util.List;

public interface CameraRecordService {

    List<HikvisionCameraRecord> cameras(CameraQueryVo cameraQueryVo);

    List<HikvisionCameraRecord> camerasInRange(CameraRangeQueryVo cameraRangeQueryVo);
}
