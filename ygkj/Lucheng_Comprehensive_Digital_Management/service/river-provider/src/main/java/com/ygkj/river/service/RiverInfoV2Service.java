package com.ygkj.river.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SingletonMap;
import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.request.RiverQueryReqVo;
import com.ygkj.river.vo.response.NearestTheRiverRespVo;
import com.ygkj.river.vo.response.RiverBaseInfoResVo;
import com.ygkj.river.vo.response.RiverCoordinateResVo;
import com.ygkj.river.vo.response.RiverInfoResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/8/7
 */
public interface RiverInfoV2Service {


    /**
     * 查询河道基础信息
     *
     * @param reqVo
     * @return
     */
    PageVO<RiverBaseInfoResVo> pageRivSegBaseInfo(RiverQueryReqVo reqVo);

    /**
     * 对分页查询河段基础信息进行统计
     *
     * @param reqVo
     * @return
     */
    JSONObject pageRivSegStats(RiverQueryReqVo reqVo);

    /**
     * 河道地图落点
     *
     * @param reqVo
     * @return
     */
    List<RiverCoordinateResVo> riverCoordinate(RiverQueryReqVo reqVo);

    /**
     * 河道地图落点
     *
     * @param statsType 1: 根据行政区划统计，2：根据河道等级统计，3：根据健康分析统计
     * @return
     */
    List<SingletonMap<String, Long>> riverStats(Integer statsType);

    List<NearestTheRiverRespVo> nearestTheRiver(String riverNo, Double latitude, Double longitude, String address);

    /**
     * 根据id查询河段详情
     *
     * @param id 河段id
     * @return
     */
    RiverBaseInfoResVo rivSegInfoById(Integer id);

    /**
     * 更新多项河段健康评价
     *
     * @param reqVoList
     * @return
     */
    CommonResult updateRivSegHealthState(List<RiverHealthStatReqVo> reqVoList);

    /**
     * 河流分析按行政区划统计
     *
     * @return
     */
    List<JSONObject> rivSegStaticByArea(String type);

    /**
     * 河流分析按河段等级统计
     *
     * @return
     */
    List<JSONObject> rivSegStaticByAreaLevel(String type);

    /**
     * 河流分析按健康分析统计
     *
     * @return
     */
    List<JSONObject> rivSegStaticByHealthState();

    /**
     * 河流分布——C位统计
     *
     * @return
     */
    JSONObject rivSegCStatic();

}
