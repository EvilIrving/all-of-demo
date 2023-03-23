package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xq
 * @Date 2021/1/17
 */
@Data
@ApiModel("值班常用电话导出接口查询参数")
public class MonitorContactToExcelQueryVo {
    @ApiModelProperty("人员姓名或单位名称")
    String contactName;
    @ApiModelProperty("通讯类型:1为人员，2为单位")
    Integer contactType;
}
