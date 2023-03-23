package com.ygkj.gragh.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.TableField;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageEntity {

    @ApiModelProperty("每页展示条数")
    @TableField(exist = false)
    @ExcelIgnore
    private Integer pageSize;

    @ApiModelProperty("页码")
    @TableField(exist = false)
    @ExcelIgnore
    private Integer pageNum;

    private static final long serialVersionUID = -1792474383774710851L;

    /**
     * 判断是否能够分页
     *
     * @return true 入参能够分页，false 不满足分页入参
     */
    public boolean isPaged() {
        return pageSize != null && pageNum != null && pageSize > 0 && pageNum > 0;
    }

    public void page() {
        if (isPaged()) {
            PageHelper.startPage(pageNum, pageSize);
        }
    }

    public void noPage() {
        this.pageNum = null;
        this.pageSize = null;
    }
}
