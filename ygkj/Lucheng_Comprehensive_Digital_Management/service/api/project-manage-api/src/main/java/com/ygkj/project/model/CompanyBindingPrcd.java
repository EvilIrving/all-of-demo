package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 物业化公司绑定工程表
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@ApiModel(value="CompanyBindingPrcd对象", description="物业化公司绑定工程表")
public class CompanyBindingPrcd extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "物业化公司id")
    private String wyhId;

    @ApiModelProperty(value = "工程编码")
    private String prcd;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getWyhId() {
        return wyhId;
    }

    public void setWyhId(String wyhId) {
        this.wyhId = wyhId;
    }

    public String getPrcd() {
        return prcd;
    }

    public void setPrcd(String prcd) {
        this.prcd = prcd;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CompanyBindingPrcd{" +
            "id=" + id +
            ", wyhId=" + wyhId +
            ", prcd=" + prcd +
            ", createTime=" + createTime +
        "}";
    }
}
