package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel
public class BusDataTableExamineVo {

    @ApiModelProperty("id")
    @NotBlank(message = "请指定一条要审核的记录！")
    private String id;

    @ApiModelProperty("状态：待审核/已通过/已驳回")
    @NotBlank(message = "请指定该条记录要审核的状态！")
    private String status;

    @ApiModelProperty("前端忽略")
    private String examinePerson;

    @ApiModelProperty("审核意见")
    private String examineRemark;

    public BusDataTableExamineVo(@NotBlank(message = "请指定一条要审核的记录！") String id, @NotBlank(message = "请指定该条记录要审核的状态！") String status, String examinePerson) {
        this.id = id;
        this.status = status;
        this.examinePerson = examinePerson;
    }

    public BusDataTableExamineVo(@NotBlank(message = "请指定一条要审核的记录！") String id, @NotBlank(message = "请指定该条记录要审核的状态！") String status, String examinePerson, String examineRemark) {
        this.id = id;
        this.status = status;
        this.examinePerson = examinePerson;
        this.examineRemark = examineRemark;
    }

    public BusDataTableExamineVo() {
    }
}
