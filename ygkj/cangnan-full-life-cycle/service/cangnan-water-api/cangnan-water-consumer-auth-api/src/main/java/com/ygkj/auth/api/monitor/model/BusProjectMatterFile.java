package com.ygkj.auth.api.monitor.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: huangzh
 * @Date: 2020/9/17 14:21
 * @Description: 办理事项相关材料文件信息
 */
@Data
public class BusProjectMatterFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id(修改时新增的资料该项不用填)")
    private String id;

    /**
     * 事项id
     */
    @ApiModelProperty("事项id(修改时新增的资料该项不用填)")
    private String matterId;

    /**
     * 材料名称
     */
    @ApiModelProperty("材料名称")
    private String materialName;

    /**
     * 是否必要(1必要，0不必要)
     */
    @ApiModelProperty("是否必要(1必要，0不必要)")
    private Integer isMust;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String filePath;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String fileName;

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
