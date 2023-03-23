package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: fml
 * @Date: 2020/10/10 13:46
 * @Description: 流程定制信息
 */
@Data
public class ProcessCustomize implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 步骤
     */
    private Integer step;

    /**
     * 操作人员
     */
    private String operator;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * is_deleted
     */
    private Integer isDeleted;
}
