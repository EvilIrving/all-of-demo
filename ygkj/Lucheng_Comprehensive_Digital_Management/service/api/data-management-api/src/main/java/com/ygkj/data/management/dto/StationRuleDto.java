package com.ygkj.data.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class StationRuleDto extends PageEntity {

    @ApiModelProperty("测站编码")
    private String stCode;
    @ApiModelProperty("测站名称")
    private String stName;
    @ApiModelProperty("测站类型：雨量站PP，水库水位站RR，河道水位站ZZ，潮位站TT，闸站DD，墒情站SS")
    private String stType;

    private String recordValidateResult;

    private List<String> recordValidateList;

    private String roleNames;

}
