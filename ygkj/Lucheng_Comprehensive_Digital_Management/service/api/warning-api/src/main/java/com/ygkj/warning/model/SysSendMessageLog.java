package com.ygkj.warning.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.enums.Type;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Huangzh
 * @description 统一消息发送表
 * @date 2021-07-13
 */
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysSendMessageLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 接收号码
     */
    private String phone;

    /**
     * 发送者id（外部人员可能没有）
     */
    private String fromUserId;

    /**
     * 发送者姓名（外部人员可能没有）
     */
    private String fromUserName;

    /**
     * 接收者id
     */
    private String destinationUserId;

    /**
     * 接收者姓名
     */
    private String destinationUserName;

    /**
     * 发送类型：浙政钉/短信/站内
     */
    private String sendType;

    /**
     * 消息类型：由业务系统自定义，比如（雨量预警、水文预警、oa报销流程等）
     */
    private String type;

    /**
     * 部分消息存在跳转处理页面的url，由子系统定义
     */
    private String handleUrl;

    /**
     * 发送日期
     */
    private Date sendTime;

    private String timeUnit;

    private Integer handleFrequency;

    private Boolean needHandle;

    private String handleStatus;

    private List<SysSendMessageHistory> histories;

    private String system;

    private String messageType;

    private String batchId;

    public SysSendMessageLog() {
    }

    public SysSendMessageLog(String content, String phone, String fromUserId, String fromUserName,
                             String destinationUserId, String destinationUserName, String sendType,
                             String type, String timeUnit, Integer handleFrequency, Boolean needHandle, String handleUrl,
                             String system, String messageType, String batchId) {
        this.content = content;
        this.phone = phone;
        this.fromUserId = fromUserId;
        this.fromUserName = fromUserName;
        this.destinationUserId = destinationUserId;
        this.destinationUserName = destinationUserName;
        this.sendType = sendType;
        this.type = type;
        this.timeUnit = timeUnit;
        this.handleFrequency = handleFrequency;
        this.needHandle = needHandle;
        this.handleUrl = handleUrl;
        this.system = system;
        this.messageType = messageType;
        this.batchId = batchId;
    }

    public enum MessageSendType implements Type {
        GOVERNMENT_DING_DING("浙政钉"),
        SMS("短信"),
        STATION_NEWS("站内");

        private final String type;

        MessageSendType(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }

        public static MessageSendType candidateType(String type) {
            if (StringUtils.isBlank(type)) {
                return null;
            }
            switch (type) {
                case "浙政钉":
                    return GOVERNMENT_DING_DING;
                case "短信":
                    return SMS;
                case "站内":
                    return STATION_NEWS;
                default:
                    break;
            }
            return null;
        }
    }


    public enum MessageHandleStatus implements Type {
        WAITING_HANDLE("待处理"),
        HANDLED("已处理"),
        TIME_OUT("超时未处理");

        private final String handleStatus;

        MessageHandleStatus(String handleStatus) {
            this.handleStatus = handleStatus;
        }


        @Override
        public String type() {
            return handleStatus;
        }
    }

    public enum SystemType implements Type {
        WENZHOU_PLATFORM("温州市水平台", "79ab17b9df254bf08300465ea81e93b3", "7c38816351774a20"),
        DATA_MANAGEMENT("数据管理系统", "9deea5b1dcce42a3a062baae6074a489", "a22e2d7d39cd4f52"),
        HYDROLOGY_DIGITIZATION("水文数字化系统", "c359ae5473aa4b8080c285768a5b336e", "d2906fa819e74d23"),
        PROJECT_MANAGE("水利工程可视化系统", "dce5a48fca50488b8e38b9e329b83c48", "28224e613be944b3"),
        PLAN_DIGITIZATION("规划数字化系统", "1a6998bc57184e6baed5e4fc2c555e77", "3b8b67a8e1f646a0"),
        FLOOD_PROTECT("防洪减灾调度系统", "108f0c3184a64c62a77ab450b32d7268", "ac0aa6e2cd7c4501"),
        //        398f756ec683420ba3d8156e4f58b88f       2b2e90b263714dff
        OA_SMART_GOVERNANCE("OA智治系统", "facd5a8a9fba49f0bd1ee23eb59bd7d6", "0abdb5ce641c48f7"),
        DYNAMIC_SUPERVISION("水域动态监管", "f0d36504d0f74b44bced019d73615b1b", "26bc71a61e7c481c"),
        WATER_MANAGE("水利工程建设周期管理系统", "d9f7fe1a39984c3294306f108f0ba9bb", "634bb619e5f741a1"),
        SUPERVISION_PROJECT("强监管系统", "dcaf3351ef664fb399595b929e104cc9", "73d178a0f0544a4a");

        private final String type;

        private final String appKey;

        private final String appSecret;

        SystemType(String type, String appKey, String appSecret) {
            this.type = type;
            this.appKey = appKey;
            this.appSecret = appSecret;
        }

        @Override
        public String type() {
            return type;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public String getAppKey() {
            return appKey;
        }

        public static SystemType getApp(String type) {
            for (SystemType systemType : SystemType.values()) {
                if (StringUtils.equals(type, systemType.type())) {
                    return systemType;
                }
            }
            return null;
        }

    }

    public enum messageType implements Type {
        NOTICE("提醒"),
        WARNING("预警"),
        DATAAPPLY("数据资源申请");

        private final String type;

        messageType(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }
    }
}
