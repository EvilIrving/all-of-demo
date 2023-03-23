package com.ygkj.project.vo.response;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ProjectPatrolResVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("开始时间")
    @ExcelProperty(value = "巡查开始时间",index = 1)
    @ColumnWidth(20)
    private Date patrolBegintime;

    @ApiModelProperty("结束时间")
    @ExcelProperty(value = "巡查结束时间",index = 2)
    @ColumnWidth(20)
    private Date patrolEndtime;

    @ApiModelProperty("名称")
    @ExcelProperty(value = "巡查人",index = 0)
    @ColumnWidth(10)
    private String realName;

    @ApiModelProperty("隐患数量")
    @ExcelIgnore
    private Integer hiddenTroubleCount;

    @ExcelProperty(value = "是否有隐患",index = 3)
    @ColumnWidth(20)
    private String isTrouble;

    public void setHiddenTroubleCount(Integer hiddenTroubleCount){
        this.hiddenTroubleCount = hiddenTroubleCount;
        if(hiddenTroubleCount != null){
            if(hiddenTroubleCount == 0){
                isTrouble = "否";
            }else {
                isTrouble = "是";
            }
        }
    }
}
