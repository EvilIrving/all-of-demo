package com.ygkj.big.screen.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author wgf
 * @date 2022/5/19 16:30
 * @Description: 断面信息表
 */
@Data
public class SectionInformation extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    private String id;

    /**
     * 断面名称
     */
    private String name;

    /**
     * 经度
     */
    private String lgtd;

    /**
     * 纬度
     */
    private String lttd;

    /**
     * 左岸高程(m)
     */
    private String leftElevation;

    /**
     * 右岸高程(m)
     */
    private String rightElevation;

    /**
     * 最低点高程(m)
     */
    private String lowestElevation;

    /**
     * 创建时间
     */
    private Date creationTime;

    /**
     * 断面起始,结束位置
     */
    private String coordinates;

    /**
     * 绑定的测站
     */
    private String stcd;

    @TableField(exist = false)
    private Double waterLevel;

    @TableField(exist = false)
    private Date updateTime;

    /**
     * 汛限水位
     */
    @TableField(exist = false)
    private Double limitLevel;

    /**
     * 保证水位
     */
    @TableField(exist = false)
    private Double guaranteeLevel;

    /**
     * 警戒水位
     */
    @TableField(exist = false)
    private Double warningLevel;

    public SectionInformation() {}
}
