package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 预警人员表
 * @date 2021-07-26
 */
@Data
@ApiModel("预警人员表")
public class WarnUserReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String userName;

    /**
     * 移动电话
     */
    @ApiModelProperty("移动电话")
    private String phone;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    private String duty;

    /**
     * 预警部门id
     */
    @ApiModelProperty("预警部门id")
    private String deptId;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    public WarnUserReqVo() {
    }

}
