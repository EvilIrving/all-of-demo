package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

/**
 * @description 工程巡查上传附件记录表
 * @author
 * @date 2022-04-25
 */
@Data
public class BhSlPatrolGisAnnex {

    /**
     * id
     */
    private Integer id;

    /**
     * 附件类型
     */
    private String type;

    /**
     * 附件名称
     */
    private String filename;

    /**
     * 附件路径
     */
    private String filepath;

    /**
     * 创建时间
     */
    private Date edittime;

    public BhSlPatrolGisAnnex() {
    }

}

