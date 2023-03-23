package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description camera_record
 * @date 2020-12-08
 */
@Data
public class CameraRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * device
     */
    private String device;

    /**
     * areacode
     */
    private String areacode;

    /**
     * checkcount
     */
    private String checkcount;

    /**
     * community
     */
    private String community;

    /**
     * currentstatus
     */
    private String currentstatus;

    /**
     * db33
     */
    private String db33;

    /**
     * deviceclass
     */
    private String deviceclass;

    /**
     * devicecode
     */
    private String devicecode;

    /**
     * factoryid
     */
    private String factoryid;

    /**
     * groupcode
     */
    private String groupcode;

    /**
     * id
     */
    private String id;

    /**
     * lastchecktime
     */
    private String lastchecktime;

    /**
     * laststatus
     */
    private String laststatus;

    /**
     * lasturl
     */
    private String lasturl;

    /**
     * lat
     */
    private String lat;

    /**
     * lng
     */
    private String lng;

    /**
     * mpid
     */
    private String mpid;

    /**
     * mpname
     */
    private String mpname;

    /**
     * needcheck
     */
    private String needcheck;

    /**
     * restype
     */
    private String restype;

    /**
     * updatetime
     */
    private String updatetime;

    public CameraRecord() {
    }

}

