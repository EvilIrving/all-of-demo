package com.ygkj.project;

import com.ygkj.project.vo.request.BhLgMfdReqVo;
import com.ygkj.project.vo.request.MtDmDailyTaskReqVo;
import com.ygkj.project.vo.request.MtDmPlanReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-18 16:46
 * @description 物业管理事项-维修养护
 */
@Api(tags = "维修养护接口")
public interface MaintenanceControllerApi {

    @ApiOperation("年度维养计划-新增或更新")
    CommonResult saveOrUpdateMtDmPlan(MtDmPlanReqVo vo);

    @ApiOperation(value = "年度维养计划-删除")
    CommonResult delMtDmPlan(String id);

    @ApiOperation(value = "年度维养计划-分页查找")
    CommonResult loadPageMtDmPlan(MtDmPlanReqVo vo);

    @ApiOperation(value = "年度维养计划-导出")
    void exportMtDmPlan(MtDmPlanReqVo vo,HttpServletResponse response);

    @ApiOperation(value = "年度维养计划-获取工程运管文件链接")
    CommonResult getYgFile(String pid,String fileId);

    @ApiOperation("维修养护日常任务-新增或更新")
    CommonResult saveOrUpdateMtDailyTask(MtDmDailyTaskReqVo vo);

    @ApiOperation(value = "维修养护日常任务-删除")
    CommonResult delMtDailyTask(String id);

    @ApiOperation(value = "维修养护日常任务-分页查找")
    CommonResult loadPageMtDailyTask(MtDmDailyTaskReqVo vo);


    @ApiOperation(value = "维修养护资金-新增或更新")
    CommonResult saveOrUpdateMtFund(BhLgMfdReqVo vo);

    @ApiOperation(value = "维修养护资金-删除")
    CommonResult delMtFund(String id);

    @ApiOperation(value = "维修养护资金-分页查询")
    CommonResult loadPageMtFund(BhLgMfdReqVo vo);

    @ApiOperation(value = "维修养护资金-导出列表")
    void exportMtFund(BhLgMfdReqVo vo, HttpServletResponse response);

}
