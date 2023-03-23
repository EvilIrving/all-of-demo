package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description att_video_base
 * @date 2020-12-10
 */
@Data
public class AttVideoBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * device
     */
    private String device;

    /**
     * area_code
     */
    private String areaCode;

    /**
     * check_count
     */
    private String checkCount;

    /**
     * community
     */
    private String community;

    /**
     * current_status
     */
    private String currentStatus;

    /**
     * db33
     */
    private String db33;

    /**
     * device_class
     */
    private String deviceClass;

    /**
     * device_code
     */
    private String deviceCode;

    /**
     * factory_id
     */
    private String factoryId;

    /**
     * group_code
     */
    private String groupCode;

    /**
     * last_check_time
     */
    private String lastCheckTime;

    /**
     * last_status
     */
    private String lastStatus;

    /**
     * last_url
     */
    private String lastUrl;

    /**
     * lat
     */
    private String lat;

    /**
     * lng
     */
    private String lng;

    /**
     * mp_id
     */
    private String mpId;

    /**
     * mp_name
     */
    private String mpName;

    /**
     * need_check
     */
    private String needCheck;

    /**
     * res_type
     */
    private String resType;

    /**
     * update_time
     */
    private String updateTime;

    public AttVideoBase() {
    }

}

