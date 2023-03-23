package com.dcy.datamanage.biz.dto.output;

import com.dcy.datamanage.api.model.BusDataSet;
import com.dcy.datamanage.api.model.BusDataSetParam;
import com.dcy.datamanage.api.model.BusDataSetResField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BusDataSetSelectOutputDTO extends BusDataSet {

    @ApiModelProperty("数据集详情")
    private Object setDetail;

    @ApiModelProperty("数据集参数")
    private List<BusDataSetParam> paramList;

    @ApiModelProperty("返回字段列表")
    private List<BusDataSetResField> fieldList;
}