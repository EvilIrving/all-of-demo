package com.ygkj.flood.vo.response;

import lombok.Data;

/**
 * @author lxl
 * @create 2022-05-11 11:08
 * @description
 */
@Data
public class PvFloodAffectPopulationResVo {

    /**
     * 村落编码
     */
    private String prevCode;

    /**
     * 行政区划编码
     */
    private String adcd;

    /**
     * 行政村
     */
    private String adminAdnm;

    /**
     * 自然村
     */
    private String naturalAdnm;

    /**
     * 防御对象
     */
    private String defenObj;

    /**
     * 防御类型
     */
    private String objtp;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系人号码
     */
    private String contacttel;

    /**
     * 影响人数
     */
    private Integer pcount;


    private String chargerName;

    private String chargerPhone;


}
