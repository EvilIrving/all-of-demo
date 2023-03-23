package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2022/5/23 11:24
 * @Description:
 */
@Data
public class WatersReport extends PageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 水域名称
     */
    @ApiModelProperty(value = "水域名称")
    private String name;

    /**
     * 行政区划，多个逗号拼接
     */
    @ApiModelProperty(value = "行政区划，多个逗号拼接")
    private String adcds;

    /**
     * 水域面积（m2）
     */
    @ApiModelProperty(value = "水域面积（m2）")
    private String area;

    /**
     * 库容量
     */
    //@ApiModelProperty(value = "库容量")
    //private String capacity;

    /**
     * 实际岸线
     */
    //@ApiModelProperty(value = "实际岸线")
    //private String shoreline;

    /**
     * 管理单位
     */
    //@ApiModelProperty(value = "管理单位")
    //private String management;

    /**
     * 水体用途
     */
    //@ApiModelProperty(value = "水体用途")
    //private String purpose;

    /**
     * 上传照片id集合 多个逗号拼接
     */
    //@ApiModelProperty(value = "上传照片id集合 多个逗号拼接")
    //private String picturesIds;

    /**
     * 水域类型（1.河道，2.水库，3.山塘，4.人工水道，5.其他水域）
     */
    //@ApiModelProperty(value = "水域类型（1.河道，2.水库，3.山塘，4.人工水道，5.其他水域）")
    //private Integer waterType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "行政区划名称")
    @TableField(exist = false)
    private String adcdNames;

   // @ApiModelProperty(value = "文件")
   // @TableField(exist = false)
   // private List<SysFile> files;

    public WatersReport() {}
}
