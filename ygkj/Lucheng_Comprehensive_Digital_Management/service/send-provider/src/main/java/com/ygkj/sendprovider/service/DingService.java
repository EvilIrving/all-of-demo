package com.ygkj.sendprovider.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.warning.vo.request.DingWorkReqVo;

import java.util.Map;

public interface DingService {

    /**
     * 根据手机号给浙政钉发消息
     *
     * @param phone
     * @param context
     */
    JSONObject sendByPhone(String phone, String context) throws Exception;

    /**
     * 根据手机号获取用户信息
     */
    JSONObject getUserInfoByMobile(String phone);

    /**
     * 向水管理平台发送不带参数的post请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    JSONObject post(String url, Map<String, Object> paramMap);

    /**
     * 新增待办
     *
     * @param reqVo
     * @return
     */
    JSONObject addWork(DingWorkReqVo reqVo);

    /**
     * 结束待办
     *
     * @param reqId
     * @return
     */
    JSONObject endWork(String reqId);

    /**
     * 取消待办
     *
     * @param reqId
     * @return
     */
    JSONObject cancelWork(String reqId);

    /**
     * 处理待办
     *
     * @param reqId
     * @return
     */
    JSONObject dealWork(String reqId);

    /**
     * 浙政钉发送
     * @param msgSendRecord
     * @return
     */
    JSONObject sendDingDing(MsgSendRecord msgSendRecord);
}
