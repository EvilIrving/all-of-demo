package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BhDepProsoninfo {

    @TableId(type = IdType.INPUT)
    /**
     * id
     */
    private Integer id;

    /**
     * pjcd
     */
    private String pjcd;

    /**
     * name
     */
    private String name;

    /**
     * id_card
     */
    private String idCard;

    /**
     * profession
     */
    private String profession;

    /**
     * education
     */
    private String education;

    /**
     * jobtitle
     */
    private String jobtitle;

    /**
     * jobs
     */
    private String jobs;

    /**
     * preparation
     */
    private Integer preparation;

    /**
     * photo
     */
    private String photo;

    /**
     * is_post
     */
    private Integer isPost;

    /**
     * dep_id
     */
    private Integer depId;

    /**
     * telphone
     */
    private String telphone;

    /**
     * 职务
     */
    private String duty;

    /**
     * 排序
     */
    private Integer orderbyId;

    /**
     * obligation
     */
    private String obligation;

    /**
     * user_id
     */
    private String userId;

    /**
     * 是否岗位责任人
     */
    private Integer isResponsible;

    /**
     * 备注
     */
    private String remark;
}
