package com.ygkj.project;

import com.ygkj.project.model.DtsRaBasicDataDbWaterExpert;
import com.ygkj.project.vo.request.WaterExpertReqVo;
import com.ygkj.project.vo.response.WaterExpertResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-12 14:20
 */
@Api(tags = "专家库")
public interface WaterExpertControllerApi {
    @ApiOperation(value = "水利专家列表")
    CommonResult listWaterExpert(WaterExpertReqVo reqVo);

    @ApiOperation(value = "新增或修改水利专家")
    CommonResult editWaterExpert(WaterExpertResVo waterExpert);

    @ApiOperation(value = "查询水利专家详情")
    CommonResult selectWaterExpert(String dtsCmfId);

    @ApiOperation(value = "删除水利专家")
    CommonResult delWaterExpert(String dtsCmfId);

    @ApiOperation(value = "统计各级水利专家数量")
    CommonResult countWaterExpert(WaterExpertReqVo reqVo);
}
