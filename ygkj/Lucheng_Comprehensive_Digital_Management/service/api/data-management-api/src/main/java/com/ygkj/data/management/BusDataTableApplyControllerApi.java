package com.ygkj.data.management;

import com.ygkj.data.management.vo.request.BusDataTableApplyAddVo;
import com.ygkj.data.management.vo.request.BusDataTableApplyPageQueryVo;
import com.ygkj.data.management.vo.request.BusDataTableExamineVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(tags = "数据资源申请/审核", value = "数据资源申请/审核")
public interface BusDataTableApplyControllerApi {

    @ApiOperation("批量申请数据资源")
    CommonResult batchApply(List<BusDataTableApplyAddVo> list);

    @ApiOperation("我申请的数据资源清单")
    CommonResult applys(BusDataTableApplyPageQueryVo queryVo);

    @ApiOperation("待我审核")
    CommonResult dataForExamine(BusDataTableApplyPageQueryVo queryVo);

    @ApiOperation("审核记录")
    CommonResult examine(BusDataTableExamineVo examineVo);

    @ApiOperation("撤销")
    CommonResult cancel(String id);

    @ApiOperation("重申")
    CommonResult reply(String id);

    @ApiOperation("申请历史")
    CommonResult applyDetail(String applyId);
}
