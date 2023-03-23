package com.ygkj.river;

import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.vo.request.AttBlueInspectionReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author lxl
 * @create 2021-08-05 21:18
 * @description
 */
@Api(tags = "蓝线管理巡查接口")
public interface AttBlueInspectionControllerApi {

    /**
     * 新增蓝线管理巡查  巡查派发
     * @param reqVo
     * @return
     */
    @ApiOperation("新增蓝线管理巡查")
    CommonResult addInspection(AttBlueInspectionReqVo reqVo);


    /**
     * 更新蓝线管理
     * @param attBlueInspection
     * @return
     */
    @ApiOperation("更新蓝线管理")
    CommonResult updateInspection(AttBlueInspection attBlueInspection);


    /**
     * 获取部门人员树
     * @param key
     * @return
     */
    @ApiOperation("获取部门人员树")
    CommonResult getDeptUserTree(String key);


    @ApiOperation("复核派发")
    CommonResult reviewDistribute(AttBlueInspectionReqVo reqVo);


    @ApiOperation("确认是否隐患")
    CommonResult hiddenDanger(String appraisalResult,String inspectionId,boolean flag);


    /**
     * 巡查处理列表
     * @param stage 阶段
     * @param type 状态  已处理 1、未处理 2
     * @param result null-全部  0-正常 1-异常
     * @return
     */
    @ApiOperation("手机端接口- 巡查处理列表")
    CommonResult inspectionList(String stage,String type,Integer pageSize,Integer pageNum,Integer result);

    @ApiOperation("手机端接口- 本次巡查详情信息")
    CommonResult inspectionDetail(String inspectionId);

    @ApiOperation("案件结办")
    CommonResult caseClosed(AttBlueInspectionReqVo reqVo);

    @ApiOperation("生成整改单")
    void generateRectificationOrder(HttpServletResponse response, Map<String,String> inspectionId) throws IOException;

}
