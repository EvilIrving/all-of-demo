package com.ygkj.river;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Api(tags = "河道周边分析", value = "河道周边分析")
@Validated
public interface RiverPeripheralAnalysisControllerApi {

    @ApiOperation("河道周边分析左侧树")
    @ApiImplicitParam(name = "riverSegId", value = "河段id", required = true, paramType = "query")
    CommonResult peripheralAnalysisTree(@NotNull(message = "请指定一个河段！") Integer riverSegId, Integer distance);

    @ApiOperation("河道周边分析右侧点位列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "类型，由左侧树获得", required = true, paramType = "query"),
            @ApiImplicitParam(name = "riverSegId", value = "河段id", required = true, paramType = "query")})
    CommonResult selectPoints(String type, Integer riverSegId, Integer distance);
}
