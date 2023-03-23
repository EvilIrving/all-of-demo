package com.dcy.datamanage.api.model;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BusDataResource {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("父资源id")
    private String parentId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createId;

    @ApiModelProperty("是否删除 1-已删除 0-未删除")
    private int delFlag;

    @ApiModelProperty("科室id")
    private String deptId;
}
