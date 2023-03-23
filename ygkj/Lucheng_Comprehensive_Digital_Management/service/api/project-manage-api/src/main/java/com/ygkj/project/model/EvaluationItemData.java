package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf
 * @date 2022/4/25 10:00
 * @Description: 考评事项数据
 */
@Data
public class EvaluationItemData extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 考评数据id
     */
    @ApiModelProperty(value = "考评数据id")
    private String reviewId;

    /**
     * 模板数据id
     */
    @ApiModelProperty(value = "模板数据id")
    private String templateId;

    /**
     * 得分
     */
    @ApiModelProperty(value = "得分")
    private String score;

    /**
     * 扣分原则
     */
    @ApiModelProperty(value = "扣分原则")
    private String markPrinciple;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记 0未删除 1已删除")
    private int delFlag;

    public EvaluationItemData() {}
}
