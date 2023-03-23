package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.BusManAddUpdateVo;
import com.ygkj.digitization.vo.request.BusManQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author xq
 * @Description 日常业务管理-业务管理
 * @Date 2021/2/1
 */
@Api(tags = "日常业务管理-业务管理")
public interface GmBusinessControllerApi {

    @ApiOperation("市级界面-分页查询多个业务管理记录")
    CommonResult upperQuery(BusManQueryVo queryVo);

    @ApiOperation("市级界面-根据id查询单个业务管理记录")
    CommonResult upperQueryOneById(String id);

    @ApiOperation("市级界面-新增/修改")
    CommonResult upperAddOrUpdate(BusManAddUpdateVo addUpdateVo);

    @ApiOperation("市级界面-根据id删除单个业务管理记录")
    CommonResult upperDeleteById(String id);

    @ApiOperation("市级界面-下达操作：如果id为空，则为全部下达")
    CommonResult sendOrder(String id);

    @ApiOperation("市级界面-同步某年的任务")
    CommonResult syncTask(Integer year);

    @ApiOperation("市级界面-提醒")
    CommonResult alertTask(String id);
}
