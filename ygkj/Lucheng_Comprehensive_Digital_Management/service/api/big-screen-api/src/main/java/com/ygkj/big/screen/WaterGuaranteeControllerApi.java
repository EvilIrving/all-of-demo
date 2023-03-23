package com.ygkj.big.screen;

import com.ygkj.entity.SingletonMap;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.*;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@Api(tags = "水资源保障大屏")
public interface WaterGuaranteeControllerApi {

    @ApiOperation("降水量公报统计")
    CommonResult stats4WrbRainfall(Integer year);

    @ApiOperation("降水量公报C位统计")
    CommonResult statsCwindow4WrbRainfall(Integer year);

    @ApiOperation("降水量公报详表")
    CommonResult table4WrbRainfall(Integer year, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("水资源量公报统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "year", value = "年份，整数类型"),
            @ApiImplicitParam(name = "type", value = "1：水资源总量，2：人均水资源量")})
    CommonResult stats4WrbWra(Integer year, Integer type);

    @ApiOperation("水资源量公报C位统计")
    CommonResult statsCwindow4WrbWra(Integer year);

    @ApiOperation("水资源量公报详表")
    CommonResult table4WrbWra(Integer year, String orderColumn, Boolean ascOrDesc);

    @ApiImplicitParams({@ApiImplicitParam(name = "year", value = "年份，整数类型"),
            @ApiImplicitParam(name = "type", value = "1：供水量，2：供水工程")})
    @ApiOperation("供水量公报统计")
    CommonResult stats4WrbWaterSupply(Integer year, Integer type);

    @ApiOperation("供水量公报C位统计")
    CommonResult statsCwindow4WrbWaterSupply(Integer year);

//    @ApiOperation("供水量公报详表")
//    CommonResult table4WrbWaterSupply(Integer year,String orderColumn,Boolean ascOrDesc);

    @ApiOperation("用水量公报统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "year", value = "年份，整数类型"),
            @ApiImplicitParam(name = "type", value = "1：行业类别，2：行政区划")})
    CommonResult stats4WrbWaterConsume(Integer year, Integer type);

    @ApiOperation("用水量公报C位统计")
    CommonResult statsCwindow4WrbWaterConsume(Integer year);

    @ApiOperation("用水量公报详表")
    CommonResult table4WrbWaterConsume(Integer year, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("水库蓄水量公报统计")
    CommonResult stats4WrbRsvrStore(Integer year);

    @ApiOperation("水库蓄水量公报C位统计")
    CommonResult statsCwindow4WrbRsvrStore(Integer year);

    @ApiOperation("水库蓄水量公报详表")
    CommonResult table4WrbRsvrStore(Integer year, String orderColumn, Boolean ascOrDesc);

    @ApiOperation("水库蓄水量公报蓄水曲线")
    CommonResult curve4WrbRsvrStore(String resCode);

    @ApiOperation("水资源承载能力大屏组件统计接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1：用水量评价；2：水质评价；3：经济评价；4：人口评价")})
    CommonResult stats4WraUnit(Integer type);

    @ApiOperation("水资源承载能力大屏组件C位统计接口")
    CommonResult statsCwindow4WraUnit();

    @ApiOperation("流域生态流量组件统计以及C位窗口")
    CommonResult stats4SectFlow();

    @ApiOperation("水源保障列表和柱状图")
    @ApiImplicitParams(@ApiImplicitParam(name = "watSrcName", value = "水源地名称"))
    CommonResult rsvrWatSrcTable(String watSrcName);

    @ApiOperation("水源保障c位")
    CommonResult rsvrWatSrc4CWindow();

    @ApiOperation("节水指标曲线")
    @ApiImplicitParams({@ApiImplicitParam(name = "adcd", value = "行政区划"),
            @ApiImplicitParam(name = "startYear", value = "起始年份"),
            @ApiImplicitParam(name = "endYear", value = "结束年份")
    })
    CommonResult watSavNormByYearInterval(String adcd, Integer startYear, Integer endYear);

    @ApiOperation("节水指标落点和列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份"),
            @ApiImplicitParam(name = "compareType", value = "1. 年用水总量;2.万元GDP用水量；3.万元工业增加至用水量；4农业灌溉水有效利用系数；5.县域节水型社会创建达标率")})
    CommonResult watSavNormByYear(Integer year, Integer compareType);

    @ApiOperation("节水指标c位")
    CommonResult watSavNorm4CWindow();

    @ApiOperation("用水效率组件卡片")
    @ApiImplicitParams({@ApiImplicitParam(name = "areaLevel", value = "国际，国内，省内，市内"),
            @ApiImplicitParam(name = "year", value = "年份")})
    CommonResult wpgEffect(String areaLevel, Integer year);

    @ApiOperation("用水效率组件C位")
    CommonResult wpgEffect4CWindow();

    @ApiOperation("三条红线用水总量")
    CommonResult redLineWatCsm(Integer year);

    @ApiOperation("三条红线用水效率")
    CommonResult redLineWpgEffect(Integer year);

    @ApiOperation("三条红线C位窗口")
    CommonResult redLine4CWindow();

    @ApiOperation(value = "取水监管可证统计", response = SingletonMap.class)
    CommonResult intakeSuperviseStats(@ApiParam("1. 根据行政区划统计，2.按取水许可统计（过期、超许可、有证无点、取水偏快）,3，取水用途统计：城市生活、农村生活、工业、农业、水力发电、其他、非水力发电;4，统计总许可取水量，总许可证次数") int type);

    @ApiOperation(value = "取水监管C位窗口", response = SingletonMap.class)
    CommonResult intakeSupervise4CWindow();

    @ApiOperation(value = "取水户统计", response = SingletonMap.class)
    CommonResult wiuStats(@ApiParam("1. 根据行政区划统计，2.超许可统计：超许可、接近许可、正常") int type);

    @ApiOperation(value = "取水户组件C位窗口", response = SingletonMap.class)
    CommonResult wiuUnit4CWindow();

}
