package com.dcy.datamanage.api.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("数据源信息")
public class BusDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("数据库名称")
    private String dataBaseName;

    @ApiModelProperty("数据库地址")
    private String dataBaseAddress;

    @ApiModelProperty("数据库类型 1.MySQL 2.ORACLE 3.PostgreSQL 4.SQLServer 5.DM")
    private Integer dataBaseType;

    @ApiModelProperty("数据库账号")
    private String accountNumber;

    @ApiModelProperty("数据库密码")
    private String accountPwd;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createId;

    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;

    @ApiModelProperty("数据源关联科室ID")
    private String deptId;

}

