package com.ygkj.big.screen.strategy;

import com.ygkj.big.screen.model.MessageRecord;

import java.util.Collection;
import java.util.List;

public interface WarningStrategy {

    String DEFAULT_RECEIVE_PHONE = "15958129170";

    String DEFAULT_RECEIVE_PERSON = "黄政豪";

    String SEND_URL_PREFIX = "http://172.20.25.165:8085/message/sendMessage?phone=";

    String POST_SEND_URL_PREFIX = "http://172.20.25.165:8085/message/sendMessage";

    String type();

    List<MessageRecord> handle(Collection<?> collection);
}
