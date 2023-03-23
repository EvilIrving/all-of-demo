package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 督办评价
 * @author wugf
 * @date 2022-06-10
 */
@Data
public class OverseeEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    private String id;

    /**
     * 督办任务的id
     */
    private String dbId;

    /**
     * 督办效率（1~5星）
     */
    private Integer dbxl;

    /**
     * 督办质量（1~5星）
     */
    private Integer dbzl;

    /**
     * 服务态度（1~5星）
     */
    private Integer fwtd;

    /**
     * 评价内容
     */
    private String pj;

    /**
     * 评价人id
     */
    private String userId;

    /**
     * 评价人id名称
     */
    @TableField(exist = false)
    private String name;

    public OverseeEvaluation() {}
}
