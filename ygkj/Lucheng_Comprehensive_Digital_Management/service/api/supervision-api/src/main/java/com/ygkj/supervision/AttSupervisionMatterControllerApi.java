package com.ygkj.supervision;


import com.ygkj.supervision.vo.request.CountOverViewReqVo;
import com.ygkj.supervision.vo.request.MatterQueryReqVo;
import com.ygkj.supervision.vo.request.MatterReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @date 2021/5/10
 */
@Api(tags = "检查事项", value = "检查事项")
public interface AttSupervisionMatterControllerApi {

    /**
     * 根据工程编码 获取检查事项列表
     *
     * @param vo
     * @return
     */
    @ApiOperation("监督总览-- 根据工程编码 获取检查事项列表")
    CommonResult matterListByCondition(MatterReqVo vo);


    /**
     * 检查情况统计
     *
     * @param vo 时间筛选
     * @return
     */
    @ApiOperation("检查情况统计")
    CommonResult countMatterSituation(CountOverViewReqVo vo);


    /**
     * 根据条件获取 对应内容
     *
     * @param vo
     * @return
     */
    @ApiOperation("问题反馈-- 根据条件（检查事项类型、项目名称） 获取检查事项列表")
    CommonResult matterListByCondition(MatterQueryReqVo vo);

    /**
     * 根据追究类型进行返回统计值 （ 5/14 需求字段还未完善 ）
     *
     * @param matterCode
     * @return
     */
    @ApiOperation("检查事项--获取追究类型统计值")
    CommonResult investigateDetails(String matterCode);

    /**
     * 删除检查事项
     *
     * @param matterCode
     * @return
     */
    @ApiOperation("删除检查事项")
    CommonResult deleteMatterByCode(String matterCode);


    /**
     * 获取 整改反馈事项
     *
     * @param vo 获取需要进行整改反馈的任务 根据用户id 等
     * @return
     */
    @ApiOperation("获取 整改反馈事项")
    CommonResult getUpcomingTask(MatterQueryReqVo vo);

    /**
     * 根据当前登录用户以及被检查人用户 获取问题反馈列表
     *
     * @param vo
     * @return
     */
    @ApiOperation("根据当前登录用户以及被检查人用户 获取问题反馈列表")
    CommonResult getMatterByLogin(MatterQueryReqVo vo);

    @ApiOperation("根据事项编码获取检查事项详情")
    CommonResult getMatterDetailByMatterCode(String matterCode);

}
