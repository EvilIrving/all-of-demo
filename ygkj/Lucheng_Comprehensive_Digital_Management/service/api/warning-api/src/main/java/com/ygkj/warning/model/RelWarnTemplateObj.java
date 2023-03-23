package com.ygkj.warning.model;

import lombok.Data;

import java.io.Serializable;

/**
 * rel_warn_template_obj
 *
 * @author huangzh 2021-03-04
 */
@Data
public class RelWarnTemplateObj implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 实体表的唯一标识
     */
    private String objId;

    /**
     * 预警组id
     */
    private Long groupId;

    /**
     * 预警模板id
     */
    private Long templateId;

    /**
     * 此关联关系是否开启 0开启 1关闭
     */
    private boolean off;

    /**
     * 是否必须确认 1必须确认 0非必须
     */
    private boolean mustCheck;

    /**
     * 消息类型：rn100短信rn010钉钉rn001站内rn110短信加钉钉rn111短信加钉钉加站内信
     */
    private String msgType;

    public RelWarnTemplateObj() {
    }

}

