package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DataMaintainDirectory implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @TableId("id")
    private String id;

    /**
     * 目录名称
     */
    private String directoryName;

    /**
     * 父级目录id
     */
    private String pid;

    /**
     * 新增时间
     */
    private Date tm;

    /**
     * 目录路径
     */
    private String dirPath;
}
