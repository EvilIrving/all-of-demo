package com.ygkj.river.service;

import com.ygkj.river.model.HikvisionCameraRecord;
import com.ygkj.river.model.RiverPatrolCameraEvent;
import com.ygkj.river.vo.request.RpcEventEditReqVo;
import com.ygkj.river.vo.request.RpcEventQueryReqVo;
import com.ygkj.river.vo.response.RpcEventResVo;
import com.ygkj.vo.PageVO;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/7
 */
public interface RiverPatrolCameraService {

    /**
     * 查询所有的视频巡河事件
     *
     * @param reqVo
     * @return
     */
    PageVO<RpcEventResVo> cameraEventPage(RpcEventQueryReqVo reqVo);

    /**
     * 增加/更新视频巡河事件;当id为空的时候增加id不空时更新
     *
     * @param reqVo
     * @return
     */
    int addCameraEvent(RpcEventEditReqVo reqVo);

    /**
     * 增加/更新视频巡河事件;当id为空的时候增加id不空时更新
     *
     * @param reqVo
     * @return
     */
    int updateCameraEvent(RpcEventEditReqVo reqVo);

    /**
     * 删除一项视频巡河事件
     *
     * @param id
     * @return
     */
    int delCameraEvent(String id);

    /**
     * 查询一个视频巡河事件
     *
     * @param id
     * @return
     */
    RpcEventResVo getOneCameraEvent(String id);

    /**
     * 查询导出视频巡河事件列表
     *
     * @param reqVo
     * @return
     */
    List<RiverPatrolCameraEvent> selectForExport(RpcEventQueryReqVo reqVo);

}
