package com.ygkj.river.service;

import com.ygkj.river.model.HikvisionCameraRecord;
import com.ygkj.river.model.RiverPatrolTaskEvent;
import com.ygkj.river.model.RiverPatrolTaskEventProb;
import com.ygkj.river.vo.request.*;
import com.ygkj.river.vo.response.RptEventResVo;
import com.ygkj.river.vo.response.RptProbResVo;
import com.ygkj.river.vo.response.RptTaskResVo;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/6
 */
public interface RiverPatrolTaskService {

    List<HikvisionCameraRecord> cameraOfRiver(String riverCode);

    List<HikvisionCameraRecord> cameraByCode(String cameraCode);

    /**
     * -------------------------任务巡河---------------------------------
     */
    /**
     * 查询所有的巡河任务
     *
     * @param reqVo
     * @return
     */
    PageVO<RptTaskResVo> taskPage(RptTaskQueryReqVo reqVo);

    /**
     * 增加/更新巡河任务
     *
     * @param reqVo
     * @return
     */
    int addTask(RptTaskEditReqVo reqVo);

    /**
     * 增加/更新巡河任务
     */
    int updateTask(RptTaskEditReqVo reqVo);

    /**
     * 删除一项巡河任务
     *
     * @param id
     * @return
     */
    int delTask(String id);

    /**
     * 查询一个巡河任务
     *
     * @param id
     * @return
     */
    RptTaskResVo getOneTask(String id);

    /**
     * ------------------------任务巡河事件-----------------------------------
     */

    /**
     * 查询所有的巡河任务事件
     *
     * @param reqVo
     * @return
     */
    PageVO<RptEventResVo> eventPage(RptEventQueryReqVo reqVo);

    /**
     * 增加/更新巡河任务事件
     *
     * @param reqVo
     * @return
     */
    String addEvent(RptEventEditReqVo reqVo);

    /**
     * 增加/更新巡河任务事件
     *
     * @param reqVo
     * @return
     */
    int updateEvent(RptEventEditReqVo reqVo);

    /**
     * 删除一项巡河任务事件
     */
    int delEvent(String id);

    /**
     * 查询一个巡河任务事件
     *
     * @param id
     * @return
     */
    RptEventResVo getOneEvent(String id);

    /**
     * 查询所有巡河任务事件进行导出
     *
     * @param reqVo
     * @return
     */
    List<RiverPatrolTaskEvent> getAllEvent(RptEventQueryReqVo reqVo);

    /**
     * ------------------------任务巡河事件问题-----------------------------------
     */
    int addProblem(RptProbEditReqVo reqVo);

    int updateProblem(RptProbEditReqVo reqVo);

    int delProblem(String id);

    /**
     * 查询一个巡河任务事件问题
     *
     * @param eventId
     * @param cameraCode
     * @return
     */
    List<RptProbResVo> problemByEventIdCameraCode(String eventId, String cameraCode);

    /**
     * 查询一个巡河任务事件问题
     *
     * @param probId
     * @return
     */
    RptProbResVo problemById(String probId);
}
