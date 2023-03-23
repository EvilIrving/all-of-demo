package com.ygkj.project;

import com.ygkj.project.vo.request.WaterWorksReqVo;
import com.ygkj.project.vo.response.CwsWcpResVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description
 * @Date 2021/8/16
 */
@Api(tags = "水厂-接口")
public interface WaterWorksControllerApi {

    @ApiOperation(value = "查询水厂列表", response = CwsWcpResVo.class)
    CommonResult listWaterWorks(WaterWorksReqVo reqVo);

    @ApiOperation("查询水厂列表表头统计")
    CommonResult stats4listWaterWorks(WaterWorksReqVo reqVo);

    @ApiOperation("查询水厂运行状态列表表头统计")
    CommonResult stats4listWaterWorksState(WaterWorksReqVo reqVo);

    @ApiOperation("水厂统计")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1. 根据行政区划统计：城市、乡镇、联村、单村,全部;" +
            "2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；" +
            "3. 根据水厂类型统计：城市、乡镇、联村、单村；" +
            "4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警" +
            "5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警" +
            "6. 统计日累计供水量，异常供水次数")
    })
    CommonResult<Object> waterWorksStats(int type);

    @ApiOperation("水厂水量水质曲线")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "1. 水量，2水质"),
            @ApiImplicitParam(name = "days", value = "时间区间类型：过去24小时=1，近7天=7；近1个月=30"),
            @ApiImplicitParam(name = "cwsCode", value = "水厂编码")})
    CommonResult waterWorksStateCurve(int type, int days, String cwsCode);
}
