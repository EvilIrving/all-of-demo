package com.ygkj.warning.vo.response;

import lombok.Data;

@Data
public class SysWarnTemplateResVo {
    /**
     * id
     */
    private Long id;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 预警组id
     */
    private Integer groupId;

    /**
     * 审核人id
     */
    private String checkUserId;

}
