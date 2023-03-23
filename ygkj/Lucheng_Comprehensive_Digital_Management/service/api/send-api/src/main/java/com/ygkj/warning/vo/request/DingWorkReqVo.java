package com.ygkj.warning.vo.request;

import lombok.Data;

@Data
public class DingWorkReqVo {

    /**
     * 水管平台用户编码，待办信息接收者
     */
    private String userCode;

    /**
     * 待办id，业务侧生成，最大长度50，要求业务应用侧唯一
     */
    private String reqId;

    /**
     * 办公类型：0-其他，1-待办，2-日程，3-会议,4-发文，5-收文，6-督办
     */
    private String workType="1";

    /**
     * 标题
     */
    private String title;

    /**
     * 办公内容，内容描述，最大2000字符
     */
    private String content;

    /**
     * 待办开始时间，格式yyyy-MM-dd HH:mm:ss
     */
    private String startTime;

    /**
     * 待办截止时间，格式yyyy-MM-dd HH:mm:ss
     */
    private String endTime;

    /**
     * 办公链接
     */
    private String workUrl;

    /**
     * 手机端办公链接
     */
    private String mobileWorkUrl;

    /**
     * 所属业务：0-其他，1-OA，2-最多跑一次，3-互联网+政务，4-线上会议
     */
    private String belongBusiness="0";

    /**
     * 水管平台用户编码，待办信息创建者
     */
    private String createUser;

    /**
     * 通知状态，待办信息是否已通知接收者：0-否，1-是，2-无需
     */
    private String notifyStatus;

    /**
     *附件信息。json数组，格式：[{"file_name":"附件1.pdf","file_url":"http://xxx","orderId":"1"},{}..]
     */
    private String fileInfo;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 扩展信息
     */
    private String remark;

}
