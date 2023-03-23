package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description 接口分类表
 * @author wd
 * @date 2020-09-18
 */
@Data
@ApiModel("接口分类参数更新实体")
public class ApiCategoryUpdateVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 分类名称
     */
    @NotNull(message = "分类名称不能为空")
    @ApiModelProperty("分类名称")
    private String categoryName;

    /**
     * 分类描述
     */
    @ApiModelProperty("分类描述")
    private String categoryRemark;

    /**
     * 上级id
     */
    @ApiModelProperty("上级id")
    private String parentId;

}
