package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.LineQueryVo;
import com.ygkj.gragh.vo.request.RiverProjectQueryVo;
import com.ygkj.gragh.vo.request.RiverQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "河湖库保护", value = "河湖库保护")
public interface RiverProtectionControllerApi {

    @ApiOperation("河道查询，地图和列表通用")
    CommonResult rivers(RiverQueryVo riverQueryVo);

    @ApiOperation("岸线查询")
    CommonResult lines(LineQueryVo lineQueryVo);

    @ApiOperation("每年的河道水域面积统计")
    CommonResult countYearlyArea();

    @ApiOperation("水域查询")
    CommonResult waterAreas();

    @ApiOperation("涉河项目查询")
    CommonResult riverProjects(RiverProjectQueryVo riverProjectQueryVo);
}
