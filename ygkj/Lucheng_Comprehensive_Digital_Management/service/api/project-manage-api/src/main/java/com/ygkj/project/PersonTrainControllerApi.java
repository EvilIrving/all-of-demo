package com.ygkj.project;

import com.ygkj.project.vo.request.BhDepPersontrainReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "人员培训")
public interface PersonTrainControllerApi {
    @ApiOperation("人员培训-新增修改")
    CommonResult saveOrUpdatePersonTrain(BhDepPersontrainReqVo vo);

    @ApiOperation("人员培训-分页查询")
    CommonResult loadPagePersonTrain(BhDepPersontrainReqVo vo);

    @ApiOperation("人员培训-删除")
    CommonResult deletePersonTrain(String id);

}
