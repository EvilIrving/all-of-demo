package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.AdcdQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "行政区划", value = "行政区划")
public interface AdcdControllerApi {
    @ApiOperation(value = "行政区划下拉框选项")
    CommonResult adcd(AdcdQueryVo adcdQueryVo);

//    @ApiOperation(value = "根据adcd集合查询对应的adnm，用于通讯录管理")
//    CommonResult selectAdnmByAdcdList(List<String> adcdList);
}
