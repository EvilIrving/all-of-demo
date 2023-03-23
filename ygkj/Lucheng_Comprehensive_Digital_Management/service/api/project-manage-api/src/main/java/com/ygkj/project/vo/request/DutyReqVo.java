package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-09-28 15:07
 * @description
 */
@Data
public class DutyReqVo extends PageEntity {

    @ApiModelProperty("查询筛选参数 年月 yyyy-MM 格式")
    private String yearAndMonth;

    @ApiModelProperty("查询筛选参数 用户名")
    private String userName;

    /**
     * 编辑 参数
     */
    @ApiModelProperty("参数id")
    private String id;

    /**
     * 带班领导id
     */
    @ApiModelProperty("带班领导id")
    private String leader;

    /**
     * 带班领导姓名
     */
    @ApiModelProperty("带班领导姓名")
    private String leaderName;

    /**
     * 白班值班人员id
     */
    @ApiModelProperty("白班值班人员id")
    private String manager;

    /**
     * 白班值班人员姓名
     */
    @ApiModelProperty("白班值班人员姓名")
    private String managerName;

    /**
     * 晚班值班人员id
     */
    @ApiModelProperty("晚班值班人员id")
    private String ngihtManager;

    /**
     * 晚班值班人员姓名
     */
    @ApiModelProperty("晚班值班人员姓名")
    private String ngihtManagerName;

    /**
     * 当班主任id
     */
    @ApiModelProperty("当班主任id")
    private String dutyChief;

    /**
     * 当班主任名称
     */
    @ApiModelProperty("当班主任名称")
    private String dutyChiefName;
}
