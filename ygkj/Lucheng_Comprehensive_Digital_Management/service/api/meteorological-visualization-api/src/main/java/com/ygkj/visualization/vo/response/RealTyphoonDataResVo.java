package com.ygkj.visualization.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class RealTyphoonDataResVo {

    /**
     * id
     */
    private Integer id;

    /**
     * 台风名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 台风编号
     */
    private String code;

    /**
     * 台风年份
     */
    private String year;

    /**
     * 登陆强度
     */
    private String landPower;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 受影响地区
     */
    private String affectedArea;

    /**
     * 台风年月
     */
    private String yearsAndMonth;

    /**
     * 登陆/暴雨时间
     */
    private Date landTime;

    /**
     * 影响时间
     */
    private Integer hours;

    private Date futureBegin;

    private Date futureEnd;

    private boolean isCurrent = false;

}
