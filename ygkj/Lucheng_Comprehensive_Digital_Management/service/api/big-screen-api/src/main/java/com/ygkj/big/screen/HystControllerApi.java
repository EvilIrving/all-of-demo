package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.HystReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 水电站  根据宏泰数据库 数据获取
 *
 * @author lxl
 */
@Api(tags = "水电站组件内容接口")
public interface HystControllerApi {

    /**
     * 按照装机容量或者行政区划统计 1-容量 2-行政区划 3-开发方式
     *
     * @param type 1-容量 2-行政区划 3-开发方式
     * @return
     */
    @ApiOperation("按照装机容量或者行政区划统计 1-容量 2-行政区划 3-开发方式")
    CommonResult countByType(Byte type);

    /**
     * 达标率或者在线率排序取前三计算 1-达标率 2-在线率
     *
     * @param type 1-达标率 2-在线率
     * @return
     */
    @ApiOperation("达标率或者在线率排序取前三计算 1-达标率 2-在线率")
    CommonResult rateOrderByMoon(Byte type, Integer moon, Integer year);

    /**
     * C位统计 包括总数  最多水电站地区  总达标率  总在线率
     *
     * @return
     */
    @ApiOperation("C位统计 包括总数  最多水电站地区  本月总达标率  本月总在线率")
    CommonResult mainTotalNum();

    @ApiOperation("根据条件筛选水电站列表")
    CommonResult hystDetailListByCondition(HystReqVo reqVo);

    /**
     * 查询水电站详情
     *
     * @param hystCode
     * @return
     */
    @ApiOperation("查询水电站详情")
    CommonResult hystDetailInfoByCode(String hystCode);

    /**
     * 获取生态流量曲线
     *
     * @param hystCode
     * @param startTime
     * @param endTime
     * @return
     */
    @ApiOperation("获取生态流量曲线")
    CommonResult getFlowCurve(String hystCode, String startTime, String endTime);


    @ApiOperation("水电站落点")
    CommonResult dropPointHyst();

}
