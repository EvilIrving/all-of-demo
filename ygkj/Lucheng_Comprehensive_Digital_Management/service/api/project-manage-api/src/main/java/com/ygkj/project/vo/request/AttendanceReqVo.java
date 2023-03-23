package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-07-05 14:38
 * @description
 */
@Data
@ApiModel
public class AttendanceReqVo extends PageEntity {

    private String name;

    private String year;

    private String month;

    private String yearMonth;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departmentName;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String corporationName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


}
