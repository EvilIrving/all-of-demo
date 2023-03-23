package com.ygkj.sendprovider.service;

import com.ygkj.warning.model.MsgSendRecord;

public interface SendService {

    /**
     * 同步执行发送任务
     */
    MsgSendRecord synSubmit(MsgSendRecord msgSendRecord);

    /**
     * 提交发送任务
     *
     * @param msgSendRecord
     * @return
     */
    Integer submit(MsgSendRecord msgSendRecord);
}
