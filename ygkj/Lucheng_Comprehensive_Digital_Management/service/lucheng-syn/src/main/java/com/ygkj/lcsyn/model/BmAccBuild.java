package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class BmAccBuild {

    @TableId(type = IdType.INPUT)
    /**
     * 附属工程编码
     */
    private String id;

    /**
     * 主体工程(海塘工程编码)
     */
    private String prcd;

    /**
     * 附属工程名称
     */
    private String proName;

    /**
     * 1管理房 2 码头 3桥梁 4道路
     */
    private String proType;

    /**
     * 位置
     */
    private String proLoc;

    /**
     * 面积
     */
    private Double proArea;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 简介
     */
    private String proInfo;

    /**
     * 照片
     */
    private String imgFile;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creatorId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private String updatorId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private int delFlag;
}
