package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hucong
 * @description 预警部门表
 * @date 2021-07-26
 */
@Data
@ApiModel("预警部门表")
public class WarnDeptReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String name;

    /**
     * 父id
     */
    @ApiModelProperty("父id 父节点传-1")
    private String pid;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("人员名称")
    private String userName;

    public WarnDeptReqVo() {
    }

}

