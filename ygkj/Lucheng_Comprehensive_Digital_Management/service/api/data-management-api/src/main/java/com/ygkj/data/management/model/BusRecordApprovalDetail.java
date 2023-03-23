package com.ygkj.data.management.model;

import com.google.common.base.Converter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 数据审核字段详细记录表
 * @date 2021-06-25
 */
@Data
@ApiModel("数据审核字段详细记录表")
public class BusRecordApprovalDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 操作批次，关联到bus_record_approval中的batch_id
     */
    @ApiModelProperty("操作批次，关联到bus_record_approval中的batch_id")
    private String batchId;

    @ApiModelProperty("字段id")
    private String columnId;

    /**
     * 修改的字段名
     */
    @ApiModelProperty("修改的字段名")
    private String columnRealName;

    /**
     * 字段中文释义
     */
    @ApiModelProperty("字段中文释义")
    private String columnComment;

    /**
     * 原始值
     */
    @ApiModelProperty("原始值")
    private String originalValue;

    /**
     * 修改后的值
     */
    @ApiModelProperty("修改后的值")
    private String targetValue;

    /**
     * 提交人
     */
    @ApiModelProperty("提交人")
    private String submitUserName;

    /**
     * 提交时间
     */
    @ApiModelProperty("提交时间")
    private Date submitTime;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operationType;

    /**
     * 数据源id,默认省厅
     */
    @ApiModelProperty("数据源id")
    private String dataSourceId;
    @ApiModelProperty("数据源名称")
    private String dataSourceName;

    public BusRecordApprovalDetail() {
    }

    public BusRecordApprovalDetail(String columnRealName, String columnComment,
                                   String originalValue, String targetValue,
                                   String submitUserName, Date submitTime,
                                   String operationType, String dataSourceId) {
        this.columnRealName = columnRealName;
        this.columnComment = columnComment;
        this.originalValue = originalValue;
        this.targetValue = targetValue;
        this.submitUserName = submitUserName;
        this.submitTime = submitTime;
        this.operationType = operationType;
        this.dataSourceId = dataSourceId;
    }

    public ValidateDto convertToValidateDto(BusRecordApprovalDetail detail) {
        ValidateDtoConverter converter = new ValidateDtoConverter();
        return converter.doForward(detail);
    }

    @NoArgsConstructor
    private class ValidateDtoConverter extends Converter<BusRecordApprovalDetail, ValidateDto> {
        @Override
        protected ValidateDto doForward(BusRecordApprovalDetail busRecordApprovalDetail) {
            ValidateDto validateDto = new ValidateDto(busRecordApprovalDetail.getColumnId(),
                    busRecordApprovalDetail.getTargetValue(), busRecordApprovalDetail.getColumnRealName(),
                    busRecordApprovalDetail.getColumnComment());
            return validateDto;
        }

        @Override
        protected BusRecordApprovalDetail doBackward(ValidateDto validateDto) {
            throw new ClassCastException("不允许逆向转换！");
        }
    }

    @Data
    @NoArgsConstructor
    public static class ValidateDto {

        private String columnId;

        private String targetValue;

        private String columnRealName;

        private String columnComment;


        public ValidateDto(String columnId, String targetValue, String columnRealName, String columnComment) {
            this.columnId = columnId;
            this.targetValue = targetValue;
            this.columnRealName = columnRealName;
            this.columnComment = columnComment;
        }
    }
}

