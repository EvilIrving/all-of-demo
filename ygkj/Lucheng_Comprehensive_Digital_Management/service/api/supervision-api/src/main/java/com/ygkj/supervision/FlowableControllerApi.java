package com.ygkj.supervision;


import com.ygkj.supervision.vo.request.FlowableReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "流程相关接口", value = "流程相关接口")
public interface FlowableControllerApi {


    /**
     * 流程发起  检查下发
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiOperation("流程发起  检查下发")
    CommonResult start(FlowableReqVo vo) throws Exception;


    /**
     * 整改完毕，进行上报 进入审核
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiOperation("整改完毕，进行上报 进入审核")
    CommonResult report(FlowableReqVo vo, HttpServletRequest request) throws Exception;

    /**
     * 获取用户待办事项
     *
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiOperation("获取待办")
    CommonResult getTasks(String userId, Integer pageSize, Integer pageNum);

    /**
     * 通过审核
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiOperation("审核未通过")
    CommonResult backReview(FlowableReqVo vo);


    /**
     * 获取当前流程节点
     *
     * @param httpServletResponse
     * @param vo
     * @throws Exception
     */
    @ApiOperation("获取当前流程节点")
    void getProcessNode(HttpServletResponse httpServletResponse, FlowableReqVo vo) throws Exception;


    /**
     * 完成结束流程
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiOperation("审核通过完成结束流程")
    CommonResult EndProcess(FlowableReqVo vo) throws Exception;


}
