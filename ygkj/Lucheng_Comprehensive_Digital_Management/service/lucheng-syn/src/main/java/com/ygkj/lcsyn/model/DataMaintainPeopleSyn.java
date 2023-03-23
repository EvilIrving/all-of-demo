package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class DataMaintainPeopleSyn implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 工程编码
     */
    @TableId("prcd")
    private String prcd;

    /**
     * 工程名称
     */
    @TableField("name")
    private String name;

    /**
     * 工程类型
     */
    @TableField("type")
    private String type;

    /**
     * 工程规模
     */
    @TableField("projectScale")
    private String projectScale;
}
