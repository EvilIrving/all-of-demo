package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @描述 接口参数操作类
 * @参数
 * @返回值
 * @创建人 wd
 * @创建时间 2020/9/17 21:26
 * @修改人和其它信息
 */

@Data
@ApiModel("接口信息更新实体")
public class BusApiUpdateVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("接口名称")
    @NotNull(message = "接口名称不能为空")
    private String apiName;

    @ApiModelProperty("对外地址")
    private String outUrl;

    @ApiModelProperty("真实接口地址")
    private String apiUrl;

    @ApiModelProperty("接口状态 1-启用 2-停用")
    @NotNull
    private String apiStatus;

    @ApiModelProperty("接口类型 1-服务接口 2-数据接口")
    @NotNull
    private Integer apiType;

    @ApiModelProperty("请求方式 1-GET 2-POST")
    @NotNull
    private Integer requestType;

    @ApiModelProperty("接口说明")
    private String apiRemark;

    @ApiModelProperty("响应格式 1-JSON 2-XML 3-FILE")
    @NotNull
    private Integer responseType;

    @ApiModelProperty("响应示例")
    private String responseExample;

}
