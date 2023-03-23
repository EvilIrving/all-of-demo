package com.ygkj.river.service;

import com.ygkj.river.model.AttBlueInspectionProblem;
import com.ygkj.river.vo.request.AttBlueInspectionProblemReqVo;
import com.ygkj.river.vo.request.AttPatrolFeedbackReqVo;

import java.util.Map;

/**
 * @author lxl
 * @create 2021-08-06 9:31
 * @description 巡查异常问题
 */
public interface AttBlueInspectionProblemService {


    /**
     * 新增巡查异常问题
     * @param attBlueInspectionProblem
     * @return
     */
    Boolean addInspectionProblem(AttBlueInspectionProblem attBlueInspectionProblem);

    /**
     * 设置问题无隐患
     * @param problemIds
     * @return
     */
    Boolean setProblemNormal(String problemIds);


    /**
     * 设置改巡查问题异常，或者正常  设置异常的话同步更改为未整改
     * @param problemId
     * @param status
     * @return
     */
    Boolean setProblemAbNormal(String problemId, Byte status);

    /**
     * 根据id获取问题列表内容
     * @param reqVo
     * @return
     */
    Object getProblemListByInspectionId(AttBlueInspectionProblemReqVo reqVo);

    /**
     * 巡查反馈
     * @param type
     * @param problems
     * @return
     */
    Boolean patrolFeedback(AttPatrolFeedbackReqVo reqVo) throws Exception;

    /**
     * 移动端接口 - 页签数量
     * @return
     */
    Map<String,Object> statusCount();
}
