package com.ygkj.visualization;

import com.ygkj.visualization.vo.request.CollectPointAddReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Date 2021/2/26
 */
@Api(tags = "气象可视化-地图点位收藏")
public interface CollectPointControllerApi {

    @ApiOperation("查询当前用户下所有的收藏点位")
    CommonResult getAllPoint(String name);

    @ApiOperation("添加一个点位进行收藏")
    CommonResult addPoint(CollectPointAddReqVo collectPoint);

    @ApiOperation("删除一个点位")
    CommonResult deletePoint(String id);
}
