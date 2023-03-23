package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hucong
 * @description 雨量辅助表
 * @date 2021-06-08
 */
@Data
public class StPptnRAssist implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 测站编码
     */
    private String stcd;

    /**
     * 时间
     */
    private Date time;

    /**
     * 1小时累计雨量
     */
    private String hour;

    /**
     * 3小时累计雨量
     */
    private String threehour;

    /**
     * 1天累计雨量
     */
    private String day;

    /**
     * 累计雨量类型 1：1小时 2：3小时 3：1天
     */
    private String type;

    public StPptnRAssist() {
    }

}

