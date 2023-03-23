package com.ygkj.flood;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Api(tags = "防汛研判-水位分析", value = "防汛研判-水位分析")
public interface WaterLevelAnalyseControllerApi {

    @ApiOperation("水位历史极值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1大中型水库，2流域水位，3平原水位"),
            @ApiImplicitParam(name = "stName", value = "测站名"),
            @ApiImplicitParam(name = "stCode", value = "测站编码(单查)"),
    })
    CommonResult hisHighestWaterLevel(String type, String stName, String stCode);

    @ApiOperation("水库、河道、闸坝报警数")
    CommonResult warnNumStatistics();

    @ApiOperation("超警、超保、超汛限数量统计")
    CommonResult overWarnStatistics();

    @ApiOperation("浮窗-水位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1大中型水库，2流域水位，3平原水位"),
            @ApiImplicitParam(name = "name", value = "测站名"),
            @ApiImplicitParam(name = "adcd", value = "行政区划"),
            @ApiImplicitParam(name = "stCode", value = "测站编码(单查)")
    })
    CommonResult floatWinWaterLevel(String type, String name, String adcd, String stCode);

    @ApiOperation("获取所有水位站的实时水位")
    CommonResult getAllStWatLev(String stName);

    @ApiOperation("查询水位历史极值表中的极值数据")
    CommonResult getHistoricWaterLevelExtremum(String stName);

    /**
     * type=1 为平原水位
     * type=2 为流域水位
     * type=3 为内河水位
     *
     * @param stName
     * @param type
     * @return
     */
    @ApiOperation("查询平原/流域/内河的实时水位")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1 为平原水位，2 为流域水位，3 为内河水位"),
            @ApiImplicitParam(name = "stName", value = "测站名")
    })
    CommonResult watLevOfPlainBasInnerRiv(String stName, Integer type);

    /**
     * 查询内河的实时水位
     *
     * @param stName
     * @return
     */
    @ApiOperation("查询内河的实时水位")
    CommonResult innerRiverWaterLevel(String stName);

}
