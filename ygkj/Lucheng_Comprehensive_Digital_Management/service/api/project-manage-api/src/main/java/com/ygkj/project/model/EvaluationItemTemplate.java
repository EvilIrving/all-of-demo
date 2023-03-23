package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf
 * @date 2022/4/25 9:57
 * @Description: 考评事项模板
 */
@Data
public class EvaluationItemTemplate extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 验收事项
     */
    @ApiModelProperty(value = "验收事项")
    private String acceptanceItem;

    /**
     * 验收要求
     */
    @ApiModelProperty(value = "验收要求")
    private String acceptanceRequirements;

    /**
     * 标准分
     */
    @ApiModelProperty(value = "标准分")
    private String standardScore;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private String sort;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    public EvaluationItemTemplate() {}
}
