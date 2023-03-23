package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

/**
 * @description sys_attachment
 * @author lxl
 * @date 2022-06-27
 */
@Data
public class BhSysAttachment {

    /**
     * aid
     */
    private Integer aid;

    /**
     * aobjid
     */
    private Integer aobjid;

    /**
     * filetype
     */
    private String filetype;

    /**
     * filepath
     */
    private String filepath;

    /**
     * filetile
     */
    private String filetile;

    /**
     * createid
     */
    private String createid;

    /**
     * createtime
     */
    private Date createtime;

    /**
     * updateid
     */
    private String updateid;

    /**
     * updatetime
     */
    private Date updatetime;

    /**
     * remark
     */
    private String remark;

    /**
     * aobjtype
     */
    private String aobjtype;

    /**
     * orderid
     */
    private Integer orderid;

    /**
     * filenumber
     */
    private String filenumber;

    /**
     * isconverttoswf
     */
    private Integer isconverttoswf;

    /**
     * convertdatetime
     */
    private Date convertdatetime;

    /**
     * convertremark
     */
    private String convertremark;

    /**
     * convertstatus
     */
    private Integer convertstatus;

    /**
     * fileclass
     */
    private Integer fileclass;

    /**
     * isprivate
     */
    private Integer isprivate;

    /**
     * isedit
     */
    private Integer isedit;

    /**
     * previewpath
     */
    private String previewpath;

    public BhSysAttachment() {
    }

}

