package com.ygkj.data.management;

import com.ygkj.data.management.model.BusBusinessDataRuleColumn;
import com.ygkj.data.management.model.BusBusinessDataRuleRow;
import com.ygkj.data.management.dto.StationRuleDto;
import com.ygkj.data.management.vo.request.RuleResultPageQueryVo;
import com.ygkj.data.management.vo.request.StationRuleHistoryRequestVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api(tags = "关联规则----实时数据", value = "关联规则----实时数据")
@Validated
public interface StationRuleControllerApi {

    @ApiOperation("获得测站类型和各类型的数量")
    CommonResult stationTypeStatistic();

    @ApiOperation("测站列表")
    CommonResult stations(StationRuleDto stationRuleDto);

    @ApiOperation("测站关联校验规则列表")
    CommonResult candidateStationRule(@NotBlank(message = "请指定一个测站！") String stationCode,
                                      @NotBlank(message = "请指定当前测站的类型！") String stationType);

    @ApiOperation("测站绑定校验规则")
    CommonResult bindStationRule(List<BusBusinessDataRuleColumn> list);

    @ApiOperation("获得测站绑定的更新规则")
    CommonResult stationUpdateRules(@NotBlank(message = "请指定一个测站！") String stationCode,
                                    @NotBlank(message = "请指定当前测站的类型！") String stationType);

    @ApiOperation("测站绑定更新规则")
    CommonResult bindStationUpdateRule(BusBusinessDataRuleRow busBusinessDataRuleRow);

    @ApiOperation("研判分析列表")
    CommonResult ruleResult(RuleResultPageQueryVo queryVo);

    @ApiOperation("离线记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "测站类型"),
            @ApiImplicitParam(name = "stationName", value = "测站名称"),
    })
    CommonResult selectOfflineHistories(StationRuleHistoryRequestVo requestVo);

    @ApiOperation("校验记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "stationType", value = "测站类型"),
            @ApiImplicitParam(name = "stationName", value = "测站名称"),
    })
    CommonResult selectValidateHistories(StationRuleHistoryRequestVo requestVo);
}
