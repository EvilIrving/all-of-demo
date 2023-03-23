package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class SubAdcd implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 行政区划编码
     */
    @TableId
    private String adcd;

    /**
     * 名称
     */
    private String adName;

    /**
     * 行政区划编码
     */
    private String adCode;

}
