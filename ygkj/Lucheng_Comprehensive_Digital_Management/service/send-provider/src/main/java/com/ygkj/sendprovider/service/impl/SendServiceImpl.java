package com.ygkj.sendprovider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pcm.ws.jws.MsgSend;
import com.pcm.ws.jws.SubmitTaskResponse;
import com.ygkj.sendprovider.mapper.SendMapper;
import com.ygkj.sendprovider.service.DingService;
import com.ygkj.sendprovider.service.SendService;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.warning.model.MsgSendRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service("sendService")
@Slf4j
public class SendServiceImpl implements SendService {

    private final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 50, 1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(500), new ThreadPoolExecutor.CallerRunsPolicy());

    @Resource
    private SendMapper sendMapper;

    @Autowired
    private GenerateDistributionIDService idService;

    @Resource
    private DingService dingService;

    /**
     * 提交发送任务
     *
     * @param msgSendRecord
     * @return
     */
    @Override
    public MsgSendRecord synSubmit(MsgSendRecord msgSendRecord) {
        msgSendRecord.setId(idService.nextID());
        Integer type = msgSendRecord.getSendType();
        switch (type) {
            case 1:
                sendSms(msgSendRecord);
                break;
            case 2:
                sendZwdd(msgSendRecord);
                break;
        }
        msgSendRecord.setCreateTime(new Date());
        sendMapper.insert(msgSendRecord);
        return msgSendRecord;
    }


    /**
     * 提交发送任务
     *
     * @param msgSendRecord
     * @return
     */
    @Override
    public Integer submit(MsgSendRecord msgSendRecord) {
        threadPoolExecutor.execute(() -> synSubmit(msgSendRecord));
        return msgSendRecord.getResult();
    }

    private void sendZwdd(MsgSendRecord msgSendRecord) {
        try {
            JSONObject data = dingService.sendByPhone(msgSendRecord.getReceivePhone(), msgSendRecord.getContext());
            log.info("[发送浙政钉结果]{}", data);
            Integer result = data.getInteger("status");
            if (result == 0) {
                msgSendRecord.setResult(0);
            } else {
                msgSendRecord.setResult(1);
                msgSendRecord.setErrMsg(data.getString("errmsg"));
            }
        } catch (Exception e) {
            log.error("-----------------------发送钉钉任务失败-----------------------");
            e.printStackTrace();
        }
    }

    private void sendSms(MsgSendRecord msgSendRecord) {
        SubmitTaskResponse submitTaskResponse = MsgSend.sendMsg(msgSendRecord.getReceivePhone(), msgSendRecord.getContext());
        String code = submitTaskResponse.getReturn().getCode();
        String message = submitTaskResponse.getReturn().getMessage();
        msgSendRecord.setResult("000000".equals(code) ? 0 : 1);
        msgSendRecord.setErrMsg(message);
    }
}
