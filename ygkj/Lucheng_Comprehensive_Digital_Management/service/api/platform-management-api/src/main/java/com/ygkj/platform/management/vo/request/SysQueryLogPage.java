package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import lombok.Data;

import java.util.Date;

@Data
public class SysQueryLogPage extends PageEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 关键词
     */
    private String keyWord;

    /**
     * 来自系统
     */
    private String sysCode;

    /**
     * 系统名称
     */
    private String sysName;

    /**
     * user_id
     */
    private String userId;

    /**
     * user_name
     */
    private String userName;

    /**
     * 来源ip
     */
    private String sourceIp;

    /**
     * 查询时间
     */
    private Date queryTime;

    /**
     * 查询星期
     */
    private String dayWeek;

}
