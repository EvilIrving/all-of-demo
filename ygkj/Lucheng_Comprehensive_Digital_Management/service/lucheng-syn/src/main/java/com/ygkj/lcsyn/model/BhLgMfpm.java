package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BhLgMfpm {

    @TableId(type = IdType.INPUT)
    /**
     * id
     */
    private Integer id;

    /**
     * 工程编号
     */
    private String pid;

    /**
     * 类型（sign=4150）
     */
    private String mfType;

    /**
     * 部位
     */
    private String position;

    /**
     * 维养项目
     */
    private String project;
}
