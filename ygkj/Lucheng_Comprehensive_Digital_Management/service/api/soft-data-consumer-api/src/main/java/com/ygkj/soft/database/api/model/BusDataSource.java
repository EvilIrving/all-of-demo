package com.ygkj.soft.database.api.model;

/**
 * @author Huang.zh
 * @date 2020/9/7 17:17
 * @Description: 数据源管理实体类
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description bus_data_source
 * @date 2020-09-07
 */
@Data
@ApiModel("bus_data_source")
public class BusDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

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
     * 服务地址
     */
    @ApiModelProperty("服务地址")
    private String serverName;

    /**
     * 端口号
     */
    @ApiModelProperty("端口号")
    private Integer port;

    /**
     * 数据库名称
     */
    @ApiModelProperty("数据库名称")
    private String dbName;

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
     * 该数据源是否被校验了
     */
    @ApiModelProperty("该数据源是否被校验了")
    private String checked;

    /**
     * 是否初始化
     */
    @ApiModelProperty("是否初始化")
    private boolean init;

    /**
     * 所属系统
     */
    @ApiModelProperty("所属系统")
    private String belongSystem;

    /**
     * 数据性质
     */
    @ApiModelProperty("数据性质")
    private String dataNature;

    /**
     * 数据来源
     */
    @ApiModelProperty("数据来源")
    private String dataFrom;

    /**
     * 审核状态 0-待审核 1-审核通过 2-审核失败
     */
    @ApiModelProperty("审核状态 0-待审核 1-审核通过 2-审核失败")
    private boolean state;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 1-未删除 0-已删除
     */
    @ApiModelProperty("是否删除 1-未删除 0-已删除")
    private boolean deleted;

    @ApiModelProperty("数据源关联科室ID")
    private String deptId;

    public BusDataSource() {
    }

}

