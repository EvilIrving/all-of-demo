package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-11-12 15:22
 */
@Data
public class LbKnowledge implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 规范名称
     */
    private String canonicalName;

    /**
     * 附件
     */
    private String appendix;

    /**
     * 创建者id
     */
    private String creatorId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 删除标识
     */
    private String delFlag;
}
