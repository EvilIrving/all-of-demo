package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2022/5/10 17:35
 * @Description: 戌浦江断面配置
 */
@Data
public class SectionConfiguration extends PageEntity implements Serializable {

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
     * 风区长度
     */
    private Double fetchLength;

    /**
     * 堤前平均水深
     */
    private Double averageDepth;

    /**
     * 风向
     */
    private String windDirection;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 坝顶高程
     */
    private Double crestElevatio;

    /**
     * 绑定的海塘编码d主潮位测站编码
     */
    @TableField(exist = false)
    private String stcd;

    /**
     * 绑定的海塘编码
     */
    private String prcd;

    /**
     * 创建时间
     */
    private Date creationTime;

    /**
     * 最近一条潮位数据
     */
    @TableField(exist = false)
    private Double tdz;

    /**
     * 下一小时整点风速
     */
    @TableField(exist = false)
    private Double speed;

    /**
     * 下一小时整点天文潮数值
     */
    @TableField(exist = false)
    private Double astrotd;

    @TableField(exist = false)
    private Double h1;

    @TableField(exist = false)
    private Double h2;

    @TableField(exist = false)
    private Double h13;

    @TableField(exist = false)
    private Double blueLevel;

    @TableField(exist = false)
    private String seawallName;

    public SectionConfiguration() {}
}
