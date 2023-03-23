package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.vo.request.MtTaskNodeReqVo;

/**
 * @author lxl
 * @create 2022-04-21 17:41
 * @description
 */
public interface HiddenDangerService {

    /**
     * 返回隐患详情以及节点详情
     * @param id
     * @return
     */
    Object getHiddenDangerDetail(String id);

    /**
     * 处理人提交
     * @param reqVo
     * @return
     */
    Object implementSubmit(MtTaskNodeReqVo reqVo);

    /**
     * 审批人提交审批意见等
     * @param reqVo
     * @return
     */
    Object submitProgressInfo(MtTaskNodeReqVo reqVo);

    /**
     * 发送提醒催办消息
     * @param id
     * @return
     */
    boolean sendNoticeMsg(String id);

    Object msgList();

    /**
     * 消息已读
     * @param id
     * @return
     */
    boolean msgRead(String id);

    /**
     * app应用数量统计
     * @return
     */
    JSONObject hiddenAppTotal();
}
