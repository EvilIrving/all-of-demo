package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import com.ygkj.enums.Type;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Data
@ApiModel
public class IndicatorAnalysisPageQueryVo extends PageEntity {

    @ApiModelProperty("划分类型：部门/人员")
    private String type;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("人员名称")
    private String username;

    public boolean checkParameters() {
        return StringUtils.isNotBlank(type) &&
                Arrays.stream(PartitionType.values()).anyMatch(partitionType -> partitionType.type().equals(this.type));
    }

    public enum PartitionType implements Type {
        DEPARTMENT("部门"),
        MEMBER("人员");

        String type;

        PartitionType(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }
    }
}
