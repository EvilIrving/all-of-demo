package com.ygkj.digitization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 * @Date 2021/1/14
 */
@Data
public class MonitorPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id，自动增长
     */
    private Integer id;

    /**
     * 值班人id，与sys_user.id关联
     */
    private String userId;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 1：副班人员，2：值班人员，3：带班领导
     */
    private Integer personType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer delFlag;

}
