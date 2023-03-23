package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class IrsInvestProjectConstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
     * 项目代码
     */
    private String projectCode;

    /**
     * 建设单位
     */
    private String constructionunit;

    /**
     * 建设内容
     */
    private String constructionContent;

    /**
     * 建设进度
     */
    private String constructionProgress;

    /**
     * 负责人
     */
    private String director;

    /**
     * 投资计划编码
     */
    private String projectBaseCode;
}
