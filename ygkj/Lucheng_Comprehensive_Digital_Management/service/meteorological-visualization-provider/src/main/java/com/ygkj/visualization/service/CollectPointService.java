package com.ygkj.visualization.service;

import com.ygkj.visualization.vo.request.CollectPointAddReqVo;
import com.ygkj.visualization.vo.response.CollectPointResVo;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author xq
 * 
 * @Date 2021/2/26
 */
public interface CollectPointService {

    List<CollectPointResVo> getAllPoint(String name);

    Object addPoint(CollectPointAddReqVo collectPoint);

    Object deletePoint(String id);
}
