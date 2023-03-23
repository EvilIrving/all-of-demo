package com.ygkj.water.project.enums;

import lombok.Getter;

/**
 * @author lxl
 * @create 2022-06-17 13:39
 * @description
 */
@Getter
public enum AttendanceApiEnum {

    ATTENDANCE_LOGIN("考勤平台-在线登录","http://47.106.181.248:8080/login"),
    ATTENDANCE_DATA("考勤平台-获取考勤数据","http://47.106.181.248:8080/getCheckingInPage"),
    ATTENDANCE_PERSON("考勤平台-获取考勤人员数据","http://47.106.181.248:8080/equipment/getPersonalIsDeviceNew"),
    ATTENDANCE_MACHINE_LOGIN("考勤平台-考勤机登录设备","http://47.106.181.248:8080/equipmentLogin");


    private String desc;

    private String url;

    AttendanceApiEnum(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }
}
