package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @date 2021-05-25 11:12
 * @description 数据考核-表数据更新时间信息
 */
@Data
public class AssessTableUdpInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("表名注释")
    private String remarks;

    @ApiModelProperty("最新更新时间到当前的天数")
    private Integer days;

    /*@ApiModelProperty("最新更新时间")
    private Date updateTime;*/
}
