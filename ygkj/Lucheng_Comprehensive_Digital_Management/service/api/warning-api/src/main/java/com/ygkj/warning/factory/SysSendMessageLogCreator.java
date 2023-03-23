package com.ygkj.warning.factory;

import com.ygkj.warning.model.SysSendMessageLog;
import org.apache.commons.lang3.StringUtils;

public class SysSendMessageLogCreator {

    /**
     * 构建最简单的出站消息，不包括首发者信息以及后续处理逻辑，一般不建议使用
     *
     * @param content
     * @param phone
     * @param sendType
     * @param type
     * @return
     */
    public static SysSendMessageLog createMessage(String content, String phone,
                                                  String sendType, String type, String system, String messageType) {
        return createMessage(content, phone,
                StringUtils.EMPTY, StringUtils.EMPTY,
                sendType, type, system, messageType);
    }

    /**
     * 构建一条的出站消息，包括首发者信息id
     *
     * @param content
     * @param phone
     * @param fromUserId
     * @param destinationUserId
     * @param sendType
     * @param type
     * @return
     */
    public static SysSendMessageLog createMessage(String content, String phone,
                                                  String fromUserId, String destinationUserId,
                                                  String sendType, String type, String system, String messageType) {
        return createMessage(content, phone,
                fromUserId, StringUtils.EMPTY,
                destinationUserId, StringUtils.EMPTY,
                sendType, type, system, messageType);
    }


    /**
     * 构建一条的出站消息，包括首发者信息，如果业务中能拿到收发者的id以及姓名，建议通过该方法构建
     *
     * @param content
     * @param phone
     * @param fromUserId
     * @param fromUserName
     * @param destinationUserId
     * @param destinationUserName
     * @param sendType
     * @param type
     * @return
     */
    public static SysSendMessageLog createMessage(String content, String phone,
                                                  String fromUserId, String fromUserName,
                                                  String destinationUserId, String destinationUserName,
                                                  String sendType, String type, String system, String messageType) {
        return createMessage(content, phone,
                fromUserId, fromUserName,
                destinationUserId, destinationUserName,
                sendType, type,
                StringUtils.EMPTY, null,
                StringUtils.EMPTY, Boolean.FALSE, system, messageType, StringUtils.EMPTY);
    }

    /**
     * 构建一条的出站消息，包括首发者信息以及后续业务处理的逻辑（URL以及是否需要处理标记），如果出站信息需要提供处理页面，建议通过该方法构建
     *
     * @param content
     * @param phone
     * @param fromUserId
     * @param fromUserName
     * @param destinationUserId
     * @param destinationUserName
     * @param sendType
     * @param type
     * @param handleUrl
     * @param needHandle
     * @return
     */
    public static SysSendMessageLog createMessage(String content, String phone,
                                                  String fromUserId, String fromUserName,
                                                  String destinationUserId, String destinationUserName,
                                                  String sendType, String type,
                                                  String handleUrl, Boolean needHandle, String system, String messageType, String batchId) {
        return createMessage(content, phone,
                fromUserId, fromUserName,
                destinationUserId, destinationUserName,
                sendType, type,
                StringUtils.EMPTY, null,
                handleUrl, needHandle, system, messageType, batchId);
    }


    public static SysSendMessageLog createMessage(String content, String phone,
                                                  String fromUserId, String fromUserName,
                                                  String destinationUserId, String destinationUserName,
                                                  String sendType, String type,
                                                  String timeUnit, Integer handleFrequency,
                                                  String handleUrl, Boolean needHandle, String system, String messageType, String batchId) {
        return new SysSendMessageLog(content, phone,
                fromUserId, fromUserName,
                destinationUserId, destinationUserName,
                sendType, type,
                timeUnit, handleFrequency,
                needHandle, handleUrl, system, messageType, batchId);
    }

}
