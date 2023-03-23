package com.ygkj.visualization;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = "区县人口/经济/防台人员信息", value = "区县人口/经济/防台人员信息")
public interface AdcdBaseDataControllerApi {

    @ApiOperation("区县信息统计区县信息统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "adnm", value = "行政区划名"),
            @ApiImplicitParam(name = "type", value = "1人口，2经济，3防台联系人")})
    CommonResult dataStatistics(String adnm, String type);


    @ApiOperation("人口经济统计")
    CommonResult ecoPopuStatistics(String adnm, Integer years);

    @ApiOperation("人口经济统计")
    CommonResult ecoPopuStatisticsByYears(Integer years);
}
