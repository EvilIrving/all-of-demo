package com.ygkj.data.management.vo.request;

import com.ygkj.data.management.model.BusRecordApprovalDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("发起数据审核流程时的入参封装")
public class DataRecordApprovalAddVo {

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("表名称")
    private String tableName;

    @ApiModelProperty("记录标识，暂定格式：主键值_名称，比如海塘id_海塘名称")
    private String recordPrimary;

    @ApiModelProperty("操作类型：新增、修改、删除")
    private String operationType;

    @ApiModelProperty("新增、修改的时候需要拼接列字段，用于构造数据字段历史")
    private List<BusRecordApprovalDetail> history;
}
