package com.ygkj.digitization.vo.response;

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
public class WarnDeptRespVo implements Serializable {

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
    @ApiModelProperty("父id")
    private String pid;

    @ApiModelProperty("部门人员数量")
    private Integer userNum;

    @ApiModelProperty("子部门")
    private List<WarnDeptRespVo> children;

    @ApiModelProperty("部门人员list")
    private List<WarnUserRespVo> warnUserList;

    public WarnDeptRespVo() {
    }

}