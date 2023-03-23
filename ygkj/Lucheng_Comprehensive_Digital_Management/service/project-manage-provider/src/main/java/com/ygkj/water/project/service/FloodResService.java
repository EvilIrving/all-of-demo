package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.AttAllRealProjectBase;
import com.ygkj.project.model.StPptnR;
import com.ygkj.project.vo.request.ResProjectReqVo;
import com.ygkj.project.vo.request.RainReqVo;
import com.ygkj.project.vo.request.TyphoonProjectReqVo;
import com.ygkj.project.vo.request.WaterReqVo;
import com.ygkj.project.vo.response.*;

import java.util.List;

public interface FloodResService {

    /**
     * 雨量列表
     *
     * @param reqVo
     * @return
     */
    List<RainResVo> listRain(RainReqVo reqVo);

    /**
     * 雨量柱状图
     *
     * @param stCode
     * @return
     */
    List<StPptnR> rainHistogram(String stCode, Integer days);

    /**
     * 关联工程列表
     *
     * @param reqVo
     * @return
     */
    List<ResProjectResVo> listResProject(ResProjectReqVo reqVo);

    /**
     * 水位列表
     *
     * @param reqVo
     * @return
     */
    List<WaterResVo> listWater(WaterReqVo reqVo);

//    /**
//     * 水位曲线
//     *
//     * @param stCode
//     * @return
//     */
//    WaterCurveResVo waterCurve(String stCode);
//
//    /**
//     * 视频列表
//     *
//     * @param stCode
//     * @return
//     */
//    List<CameraResVo> listCamera(String stCode);


    /**
     * 台风关联工程列表
     *
     * @param reqVo
     * @return
     */
    List<ResProjectResVo> listTyphoonProject(TyphoonProjectReqVo reqVo);


    /**
     * 台风关联工程列表
     *
     * @param reqVo
     * @return
     */
    List<? extends AttAllRealProjectBase> listTyphoonAllRealProject(TyphoonProjectReqVo reqVo);

    /**
     * 台风关联工程列表 船新版本
     *
     * @param reqVo
     * @return
     */
    JSONObject listTyphoonAllRealProjectNewestVersion(TyphoonProjectReqVo reqVo);

    /**
     * 台风关联工程统计
     *
     * @param reqVo
     * @return
     */
    Integer countTyphoonAllRealProject(TyphoonProjectReqVo reqVo);

    /**
     * 大中型水库最新水位
     *
     * @param code
     * @return
     */
    JSONObject reservoirWaterLevel(String code);
}
