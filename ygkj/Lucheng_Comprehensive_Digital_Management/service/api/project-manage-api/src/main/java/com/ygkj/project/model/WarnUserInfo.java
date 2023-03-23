package com.ygkj.project.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class WarnUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("报警配置id")
    private String configId;

    @ApiModelProperty("人员id")
    private String userId;

    @ApiModelProperty("接收人姓名")
    private String userName;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("单位名")
    private String deptName;
}
