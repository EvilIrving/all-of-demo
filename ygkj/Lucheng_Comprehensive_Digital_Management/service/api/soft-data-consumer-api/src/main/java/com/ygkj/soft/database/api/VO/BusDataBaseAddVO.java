package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/9/7 21:46
 * @Description:
 */
@Data
@ApiModel("添加数据源记录实体")
public class BusDataBaseAddVO {
    /**
     * 数据库名称
     */
    @ApiModelProperty("数据库名称")
    private String dataBaseName;

    /**
     * 数据库地址
     */
    @ApiModelProperty("数据库地址")
    private String dataBaseAddress;

    /**
     * 数据库类型
     */
    @ApiModelProperty("数据库类型")
    private Integer dataBaseType;

    /**
     * 数据库账号
     */
    @ApiModelProperty("数据库账号")
    private String accountNumber;

    /**
     * 数据库密码
     */
    @ApiModelProperty("数据库密码")
    private String accountPwd;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 是否初始化
     */
    @ApiModelProperty("是否初始化")
    private String init;
}
