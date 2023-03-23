package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hucong
 * @description 磁盘信息
 * @date 2021-07-17
 */
@Data
@ApiModel("磁盘信息")
public class DiskRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 盘符路径
     */
    @ApiModelProperty("盘符路径")
    private String dirName;

    /**
     * 盘符类型
     */
    @ApiModelProperty("盘符类型")
    private String sysTypeName;

    /**
     * 文件类型
     */
    @ApiModelProperty("文件类型")
    private String typeName;

    /**
     * 总大小
     */
    @ApiModelProperty("总大小")
    private BigDecimal total;

    /**
     * 已经使用量
     */
    @ApiModelProperty("已经使用量")
    private BigDecimal used;

    /**
     * 剩余大小
     */
    @ApiModelProperty("剩余大小")
    private BigDecimal free;

    /**
     * 资源的使用率
     */
    @ApiModelProperty("资源的使用率")
    private BigDecimal usaged;

    @ApiModelProperty("读取速度")
    private BigDecimal diskWrite;

    @ApiModelProperty("写入速度")
    private BigDecimal diskRead;

    public DiskRespVo() {
    }

}
