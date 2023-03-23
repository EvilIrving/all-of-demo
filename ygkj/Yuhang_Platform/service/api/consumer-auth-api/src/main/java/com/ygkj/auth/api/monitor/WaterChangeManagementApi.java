package com.ygkj.auth.api.monitor;

import com.ygkj.auth.VO.request.ChangeDetailUpdateReqVo;
import com.ygkj.auth.VO.request.WaterChangeDetailAddReqVo;
import com.ygkj.auth.VO.request.WaterChangePageReqVo;
import com.ygkj.auth.VO.request.WaterChangeYearReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: fml
 * @Date: 2020/9/11 16:19
 * @Description:
 */
@Api(value = "WaterMonitorControllerApi", tags = "水域监测-水域变化管理")
public interface WaterChangeManagementApi {

    //根据行政区划和水域类型查询水域变化信息
    @ApiOperation(value = "分页查询水域变化信息(现状)接口")
    Object queryWaterChangeInfo(WaterChangePageReqVo vo);

    //根据id获取水域变化详情记录
    @ApiOperation(value = "根据水域变化id获取水域变化详情记录")
    Object getChangeDetails(@PathVariable("id") String id);

    //新增水域详情记录
    @ApiOperation(value = "新增水域详情记录")
    Object addChangeDetail(WaterChangeDetailAddReqVo vo);

    @ApiOperation(value = "根据id查询水域详情记录")
    Object queryChangeDetail(@PathVariable("id") String id);

    //更新水域变化信息(更新后新增历史数据)
    @ApiOperation(value = "更新水域变化信息")
    Object updateChangeDetail(ChangeDetailUpdateReqVo vo);

    @ApiOperation(value = "删除水域详情记录")
    Object deleteChangeDetail(@PathVariable("id") String id);

    @ApiOperation(value = "根据水域id查询水域详情记录")
    Object queryWaterDetail(@PathVariable("id") String id);

    @ApiOperation(value = "根据年度、所需字段获取当前水域变化详情相关数据")
    Object queryYearWaterDetailInfo(WaterChangeYearReqVo vo);
}
