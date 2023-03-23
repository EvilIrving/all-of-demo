package com.ygkj.project.model;

import lombok.Data;

import java.util.Date;

/**
 * @author wgf
 * @date 2022/5/18 10:48
 * @Description: 预警提醒相关最后一次提醒的时间
 */
@Data
public class ReminderTimeRecord {

    private String id;

    private Date tm;
}
