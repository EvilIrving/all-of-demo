package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/27 10:12
 * @Description:
 */
@Data
public class WatersReportInfo extends PageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    private String id;

    /**
     * 水域报表的id
     */
    private String reportId;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 发现时间
     */
    private String findTime;

    /**
     * 违法占用行为
     */
    private String illegalOccupy;

    /**
     * 涉事单位（个人信息）
     */
    private String unitsInvolved;

    /**
     * 上传照片id集合 多个逗号拼接
     */
    private String picturesIds;

    /**
     * 创建时间
     */
    private Date createTime;

    @ApiModelProperty(value = "文件")
    @TableField(exist = false)
    private List<SysFile> files;

    @ApiModelProperty(value = "行政区划名称")
    @TableField(exist = false)
    private String adcdNames;

    @ApiModelProperty(value = "行政区划")
    @TableField(exist = false)
    private String adcds;

    @ApiModelProperty(value = "水域名称")
    @TableField(exist = false)
    private String name;

    public WatersReportInfo() {}
}
