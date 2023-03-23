package com.ygkj.big.screen;

import com.ygkj.big.screen.vo.request.MessageRecordQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "短信预警记录", value = "短信预警记录")
public interface MessageRecordControllerApi {

    @ApiOperation("条件查询表格")
    CommonResult messageRecords(MessageRecordQueryVo queryVo);
}
