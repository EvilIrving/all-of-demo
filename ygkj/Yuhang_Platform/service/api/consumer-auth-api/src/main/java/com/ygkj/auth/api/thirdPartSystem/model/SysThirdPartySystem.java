package com.ygkj.auth.api.thirdPartSystem.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 第三方系统接入信息表
 * @author Huangzh
 * @date 2020-10-27
 */
@Data
public class SysThirdPartySystem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 第三方系统名称
     */
    private String systemName;

    /**
     * 第三方系统跳转url
     */
    private String url;

    /**
     * 是否删除(1已删除；0未删除)
     */
    private boolean deleted;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 更新人
     */
    private String updateId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    public SysThirdPartySystem() {
    }

}
