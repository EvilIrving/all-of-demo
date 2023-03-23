package com.ygkj.project;

import com.ygkj.project.model.AttSixchargeBase;
import com.ygkj.project.model.AttSixchargeChangeRecord;
import com.ygkj.project.vo.request.AttSixchargeBaseReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "三个责任人接口")
public interface AttSixchargeBaseControllerApi {
    @ApiOperation("新增三个责任人")
    CommonResult saveOrUpdate(AttSixchargeBase entity, HttpServletRequest request);

    @ApiOperation("分页查询三个责任人")
    CommonResult queryPage(AttSixchargeBaseReqVo vo);

    @ApiOperation("单条查询查询三个责任人")
    CommonResult queryOne(AttSixchargeBaseReqVo vo);

    @ApiOperation("查询三个责任人变更记录")
    CommonResult queryChangeRecord(AttSixchargeChangeRecord record);
}
