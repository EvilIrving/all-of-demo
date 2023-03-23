package com.ygkj.soft.database.api;

import com.ygkj.soft.database.api.VO.DataAssessResultQueryVo;
import com.ygkj.soft.database.api.VO.DataAssessUpdFreQueryVo;
import com.ygkj.soft.database.api.model.DataAssessUpdFrequency;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author fml
 * @date 2021-05-20 17:54
 * @description 数据考核信息管理
 */
@Api(value = "数据考核信息管理", tags = "数据考核信息管理")
public interface DataAssessControllerApi {

    @ApiOperation(value = "新增更新频次信息")
    CommonResult addFreInfo(DataAssessUpdFrequency frequency);

    @ApiOperation(value = "删除更新频次信息")
    @ApiImplicitParam(name = "ids", value = "频次id，多个以逗号分隔")
    CommonResult delFreInfos(String ids);

    @ApiOperation(value = "修改更新频次信息")
    CommonResult updFreInfo(DataAssessUpdFrequency frequency);

    @ApiOperation(value = "查询更新频次信息")
    CommonResult queryFreInfos(DataAssessUpdFreQueryVo vo);

    @ApiOperation(value = "查询最新的考核结果信息")
    CommonResult queryAssessResults(DataAssessResultQueryVo vo);

    @ApiOperation("数据考核表更新时间排名")
    CommonResult assessTable();
}
