package com.ygkj.big.screen.vo.request;


import lombok.Data;

import java.util.Date;

/**
 * 生态流量实时数据请求类
 */
@Data
public class HystStatisticalReqVo {

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;


    /**
     * 根据月份获取的实时表 表名
     */
    private String tableName;

    /**
     * 测站编码
     */
    private String hystCode;



}
