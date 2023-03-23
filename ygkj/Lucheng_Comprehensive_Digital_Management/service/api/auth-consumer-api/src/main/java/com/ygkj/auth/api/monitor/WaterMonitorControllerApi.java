package com.ygkj.auth.api.monitor;

import com.ygkj.auth.VO.request.WaterBaseAddReqVo;
import com.ygkj.auth.VO.request.WaterBaseByInfoPageReqVo;
import com.ygkj.auth.VO.request.WaterBaseUpdateReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author: fml
 * @Date: 2020/9/8 18:07
 * @Description:
 */
@Api(value = "WaterMonitorControllerApi", tags = "水域监测服务")
public interface WaterMonitorControllerApi {

    @ApiOperation(value = "新增水域基本信息接口")
    Object insert(WaterBaseAddReqVo vo);

    @ApiOperation(value = "删除水域基本信息接口")
    Object delete(@PathVariable("id") String id);

    @ApiOperation(value = "更新水域基本信息接口")
    Object update(WaterBaseUpdateReqVo vo);

    @ApiOperation(value = "主键 id 查询水域基本信息接口")
    Object load(@PathVariable("id") String id);

    @ApiOperation(value = "根据条件分页查询水域基本信息接口")
    Object pageListByInfo(WaterBaseByInfoPageReqVo vo);

    @ApiOperation(value = "获取行政区划信息")
    Object queryAreaNames();

    @ApiOperation(value = "上传解析经纬度excel")
    Object parseExcel(MultipartFile file);

    @ApiOperation(value = "获取附近5KM的水域基本情况")
    @ApiImplicitParams({@ApiImplicitParam(name = "lng", value = "经度"),
            @ApiImplicitParam(name = "lat", value = "纬度")})
    Object queryNearbyWaterBaseInfo(Double lng, Double lat);
}
