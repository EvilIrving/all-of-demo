package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.big.screen.vo.request.MessageRecordQueryVo;
import com.ygkj.vo.PageVO;

/**
 * 短信服务
 */
public interface MessageService {

    PageVO<MessageRecord> selectByCondition(MessageRecordQueryVo queryVo);
}
