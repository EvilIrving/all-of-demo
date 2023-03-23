package com.ygkj.river.service;

import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.vo.request.AttBlueInspectionReqVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author lxl
 * @create 2021-08-06 9:31
 * @description 蓝线巡查内容
 */
public interface AttBlueInspectionService {

    /**
     * 新增蓝线巡查内容
     *
     * @param reqVo
     * @return
     */
    Boolean addInspection(AttBlueInspectionReqVo reqVo) throws Exception;

    /**
     * 更新蓝线巡查内容
     *
     * @param attBlueInspection
     * @return
     */
    Boolean updateInspection(AttBlueInspection attBlueInspection);

    /**
     * 复核派发
     *
     * @param reqVo
     * @return
     */
    Boolean reviewDistribute(AttBlueInspectionReqVo reqVo);

    /**
     * 确认是否隐患
     * @param inspectionId
     * @param flag
     * @return
     */
    Boolean hiddenDanger(String appraisalResult,String inspectionId, boolean flag);

    /**
     * 获取巡查列表 根据状态 和阶段
     *
     * @param stage  当前项目阶段  1开工 2施工  3完工
     * @param type   根据当前处理状态 1-未处理 2-已处理
     * @param result 处理结果  null-全部  0-正常  1-异常
     * @return
     */
    Object inspectionList(String stage, String type, Integer pageSize, Integer pageNum, Integer result);

    /**
     * 本次巡查详情
     *
     * @param inspectionId
     * @return
     */
    Object inspectionDetail(String inspectionId);

    /**
     * 案件结办
     *
     * @param reqVo
     * @return
     */
    Object caseClosed(AttBlueInspectionReqVo reqVo);


    /**
     * 获取批后监管 组织人员树
     * @param key
     * @return
     */
    Object getInspectDeptUser(String key);

    /**
     * 生成整改单
     * @param inspectionId
     */
    void generateRectificationOrder(HttpServletResponse response, Map<String,String> inspectionId) throws IOException;
}
