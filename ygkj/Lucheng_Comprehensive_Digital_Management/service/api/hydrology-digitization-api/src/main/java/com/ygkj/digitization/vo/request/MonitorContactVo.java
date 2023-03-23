package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description 值班常用电话实体类对外暴露的属性
 * @Date 2021/1/14
 */
@Data
@ApiModel("值班常用电话")
public class MonitorContactVo {

    /**
     * 表主键，自动增长
     */
    @ApiModelProperty("表主键，更新的时候需要提供此属性")
    private Integer id;
    /**
     * 通讯类型：1为人员，2为单位
     */
    @ApiModelProperty("通讯类型:1为人员，2为单位")
    private Integer contactType;

    /**
     * 通讯类型：人员或单位
     */
    @ApiModelProperty("通讯类型名称，前端不需要传递此参数")
    private String contactTypeName;

    /**
     * 联系名称：1为人员姓名，2为单位名称
     */
    @ApiModelProperty("人员姓名或单位名称")
    private String contactName;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 传真
     */
    @ApiModelProperty("传真")
    private String fax;
}
