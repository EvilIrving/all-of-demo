package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Huang.zh
 * @date 2020/11/9 14:19
 * @Description:
 */
@Data
public class BusDataTableStatusChangeVo {
    @ApiModelProperty("当前要修改的记录的id")
    @NotBlank(message = "请指定当前记录的id！")
    private String id;
    @ApiModelProperty("审核状态 1、待审核 2、通过 3、驳回 4、上线")
    @NotBlank(message = "请指定当前记录的审核状态！")
    private Integer status;
}
