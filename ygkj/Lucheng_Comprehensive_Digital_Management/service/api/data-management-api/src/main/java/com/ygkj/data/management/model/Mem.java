package com.ygkj.data.management.model;

import cn.hutool.core.util.NumberUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Mem implements Serializable {

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
     * 内存总量
     */
    private BigDecimal total;

    /**
     * 已用内存
     */
    private BigDecimal used;

    /**
     * 剩余内存
     */
    private BigDecimal free;

    /**
     * 使用率
     */
    private BigDecimal usaged;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getUsed() {
        return used;
    }

    public void setUsed(BigDecimal used) {
        this.used = used;
    }

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public BigDecimal getUsaged() {
        return usaged;
    }

    public void setUsaged(BigDecimal usaged) {
        this.usaged = usaged;
    }
}
