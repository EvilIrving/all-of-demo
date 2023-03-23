package com.ygkj.project;

import com.ygkj.project.model.*;
import com.ygkj.project.vo.request.BhLgMpAfReqVo;
import com.ygkj.project.vo.request.BhLgMpSpReqVo;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-26 10:06
 * @description 物业管理事项 - 工程检查相关接口
 */
@Api(tags = "工程检查")
public interface ProjCheckControllerApi {

    @ApiOperation("汛前检查列表查询")
    CommonResult bhLgMpFcList(ProjCheckReqVo reqVo);

    @ApiOperation("导出汛前检查列表")
    void exportLgMpFc(ProjCheckReqVo reqVo , HttpServletResponse response);

    @ApiOperation("汛前检查 - 新增或更新")
    CommonResult saveOrUpdateBhLgMpFc(BhLgMpFc bhLgMpFc);

    @ApiOperation("汛前检查 - 删除")
    CommonResult deleteBhLgMpFc(String id);

    @ApiOperation("日常检查")
    CommonResult dailyCheckList(ProjCheckReqVo reqVo);

    @ApiOperation("日常检查-巡查轨迹")
    CommonResult checkTrack(String id);

    @ApiOperation("日常检查- 导出")
    void exportDailyCheckList(ProjCheckReqVo reqVo,HttpServletResponse response);

    @ApiOperation("年度检查列表查询")
    CommonResult bhLgMpAcList(ProjCheckReqVo reqVo);

    @ApiOperation("年度检查 - 新增或更新")
    CommonResult saveOrUpdateBhLgMpAc(BhLgMpAc bhLgMpAc);

    @ApiOperation("年度检查 - 删除")
    CommonResult deleteBhLgMpAc(String id);

    @ApiOperation("年度检查- 导出")
    void exportBhLgMpAcList(ProjCheckReqVo reqVo,HttpServletResponse response);

    @ApiOperation("特殊检查 - 列表")
    CommonResult specialCheckList(ProjCheckReqVo reqVo);

    @ApiOperation("特殊检查 - 新增或更新")
    CommonResult saveOrUpdateSpecialCheck(BhLgPsSpecialCheck check);

    @ApiOperation("特殊检查 - 删除")
    CommonResult deleteSpecialCheck(String id);

    @ApiOperation("特殊检查 - 导出")
    void exportSpecialCheck(ProjCheckReqVo reqVo,HttpServletResponse response);

    @ApiOperation("汛后检查 -列表")
    CommonResult mpAfList(ProjCheckReqVo reqVo);

    @ApiOperation("汛后检查 - 新增或者更新")
    CommonResult mpAfSaveOrUpdate(BhLgMpAfReqVo bhLgMpAf);

    @ApiOperation("汛后检查 - 删除")
    CommonResult mpAfDelete(String id);

    @ApiOperation("汛后检查 - 导出")
    void mpAfExport(ProjCheckReqVo reqVo,HttpServletResponse response);

    @ApiOperation("专项检查 - 列表")
    CommonResult mpSpList(ProjCheckReqVo reqVo);

    @ApiOperation("专项检查 - 新增或更新")
    CommonResult mpSpSaveOrUpdate(BhLgMpSpReqVo bhLgMpSp);

    @ApiOperation("专项检查 - 删除")
    CommonResult mpSpDelete(String id);

    @ApiOperation("专项检查 - 导出")
    void mpSpExport(ProjCheckReqVo reqVo,HttpServletResponse response);


}
