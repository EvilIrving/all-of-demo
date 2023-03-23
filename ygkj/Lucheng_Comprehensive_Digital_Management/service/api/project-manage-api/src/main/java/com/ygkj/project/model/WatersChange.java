package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2022/5/23 11:30
 * @Description:
 */
@Data
public class WatersChange extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 水域报表的id
     */
    @ApiModelProperty(value = "水域报表的id")
    private String reportId;

    /**
     * 年份
     */
    @ApiModelProperty(value = "年份")
    private Integer yearTime;

    /**
     * 水域面积（m2）
     */
    @ApiModelProperty(value = "水域面积（m2）")
    private String area;

    /**
     * 库容量
     */
    @ApiModelProperty(value = "库容量")
    private String capacity;

    /**
     * 实际岸线
     */
    @ApiModelProperty(value = "实际岸线")
    private String shoreline;

    /**
     * 河底高程
     */
    @ApiModelProperty(value = "河底高程")
    private String bottomElevation;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public WatersChange() {}
}
