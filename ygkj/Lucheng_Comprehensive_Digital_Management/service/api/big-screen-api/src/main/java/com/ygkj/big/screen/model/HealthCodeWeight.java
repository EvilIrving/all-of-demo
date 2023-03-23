package com.ygkj.big.screen.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class HealthCodeWeight {

    private String id;

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("父项id")
    private String parentId;

    @ApiModelProperty("类别")
    private String category;

    @ApiModelProperty("分数")
    private Integer score;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("子项")
    @TableField(exist = false)
    private List<HealthCodeWeight> subWeight;
}
