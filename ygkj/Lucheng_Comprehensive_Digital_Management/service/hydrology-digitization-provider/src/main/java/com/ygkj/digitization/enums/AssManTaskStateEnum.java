package com.ygkj.digitization.enums;

/**
 * @author xq
 * @Description 任务书下达-任务状态枚举
 * @Date 2021/2/1
 */
public enum AssManTaskStateEnum {
    UN_SEND(0, "未下达"),
    SENDED(1, "已下达"),
    ALERTED(2, "已提醒"),
    OUT_OF_DATE(3, "已超期"),
    FINISHED(4, "已完成");
    Integer taskStateNum;
    String taskStateDesc;

    AssManTaskStateEnum(Integer taskStateNum, String taskStateDesc) {
        this.taskStateNum = taskStateNum;
        this.taskStateDesc = taskStateDesc;
    }

    public Integer getTaskStateNum() {
        return taskStateNum;
    }

    public String getTaskStateDesc() {
        return taskStateDesc;
    }
}
