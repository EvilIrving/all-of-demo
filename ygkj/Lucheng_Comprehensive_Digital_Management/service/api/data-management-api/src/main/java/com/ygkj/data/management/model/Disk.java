package com.ygkj.data.management.model;

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
public class Disk implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 删除标识
     */
    private String delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 盘符路径
     */
    private String dirName;

    /**
     * 盘符类型
     */
    private String sysTypeName;

    /**
     * 文件类型
     */
    private String typeName;

    /**
     * 总大小
     */
    private BigDecimal total;

    /**
     * 已经使用量
     */
    private BigDecimal used;

    /**
     * 剩余大小
     */
    private BigDecimal free;

    /**
     * 资源的使用率
     */
    private BigDecimal usaged;

    public Disk() {
    }

}

