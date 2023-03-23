package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SectionStorm {

    @TableId
    private String sectionId;

    /**
     * 时间
     */
    private Date tm;

    /**
     * h1
     */
    private Double h1;

    /**
     * h2
     */
    private Double h2;

    /**
     * h13
     */
    private Double h13;

    @ApiModelProperty("塘顶高程")
    @TableField(exist = false)
    private Double crestElevatio;

    @ApiModelProperty("最新潮位数据")
    @TableField(exist = false)
    private Double tdz;
}
