package com.ygkj.auth.api.monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author fml
 * @description 办理事项
 * @date 2020-09-16
 */
@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusProjectMatter implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 事项名称
     */
    @ApiModelProperty("事项名称")
    private String matterName;

    /**
     * 事项编码
     */
    @ApiModelProperty("事项编码")
    private String matterNo;

    /**
     * 子项名称
     */
    @ApiModelProperty("子项名称")
    private String chileMatterName;

    /**
     * 孙项名称
     */
    @ApiModelProperty("孙项名称")
    private String grandsonMatterName;

    /**
     * 项目阶段
     */
    @ApiModelProperty("项目阶段")
    private String projectStage;

    /**
     * 办理天数
     */
    @ApiModelProperty("办理天数")
    private Integer processDays;

    /**
     * 资料文件信息
     */
    @ApiModelProperty("资料文件信息")
    private List<BusProjectMatterFile> busProjectMatterFiles;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * 是否删除(1未删除；0已删除)
     */
    private Integer isDeleted;

}
