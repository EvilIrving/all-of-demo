package com.ygkj.water.project.service;

import com.ygkj.project.vo.request.AllProjectReqVo;
import com.ygkj.project.vo.request.DelProjectReqVo;
import com.ygkj.project.vo.request.ProjectEditReqVo;
import com.ygkj.project.vo.response.AllProjectResVo;
import com.ygkj.project.vo.response.DictResVo;
import com.ygkj.project.vo.response.ThreeChargeResVo;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface EngineeringSupervisionService {

    /**
     * 工程类型统计
     *
     * @param type 统计方式 1流域 2类型 3行政区划
     * @return
     */
    List<DictResVo> countProject(Integer type);

    /**
     * 流域列表
     *
     * @return
     */
    List<DictResVo> listBas();

    /**
     * 工程规模列表
     *
     * @return
     */
    List<DictResVo> listScale();

    /**
     * 工程等别列表
     *
     * @return
     */
    List<DictResVo> listGrad();

    /**
     * 工程列表
     *
     * @param allProjectReqVo
     * @return
     */
    List<AllProjectResVo> listProject(AllProjectReqVo allProjectReqVo);

    /**
     * 编辑工程信息
     *
     * @param projectEditReqVo
     */
    void editProject(ProjectEditReqVo projectEditReqVo);

    /**
     * 工程详细信息
     *
     * @param projectCode
     * @return
     */
    AllProjectResVo projectDetail(String projectCode);

    /**
     * 三个责任人详情
     *
     * @param projectCode
     * @return
     */
    List<ThreeChargeResVo> threeCharge(String projectCode);

    /**
     * 批量删除工程
     *
     * @param reqVo
     */
    void delProject(DelProjectReqVo reqVo);

    /**
     * 分页工程列表
     *
     * @param allProjectReqVo
     * @return
     */
    PageVO<AllProjectResVo> pageProject(AllProjectReqVo allProjectReqVo);

    /**
     * 发送消息
     * @param phone
     * @param content
     * @param sendType
     * @return
     */
    Object sendMsg(String phone, String content, String sendType);
}
