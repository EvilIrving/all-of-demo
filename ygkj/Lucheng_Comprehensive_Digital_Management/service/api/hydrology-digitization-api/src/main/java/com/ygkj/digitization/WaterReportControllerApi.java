package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "水情报表controller")
public interface WaterReportControllerApi {

    @ApiOperation(value = "河网水位")
    CommonResult RiverwaterLevel(StationQueryVo queryVo);

}
