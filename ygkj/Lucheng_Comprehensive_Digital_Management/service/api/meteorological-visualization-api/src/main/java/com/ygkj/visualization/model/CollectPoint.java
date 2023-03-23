package com.ygkj.visualization.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 气象可视化-地图点位收藏
 *
 * @author xq 2021-02-26
 */
@Data
public class CollectPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 点位名称
     */
    private String pointName;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * user_id
     */
    private String userId;

    /**
     * 地址
     */
    private String location;

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
    private int delFlag;

    /**
     * dept_id
     */
    private String deptId;

    public CollectPoint() {
    }

}
