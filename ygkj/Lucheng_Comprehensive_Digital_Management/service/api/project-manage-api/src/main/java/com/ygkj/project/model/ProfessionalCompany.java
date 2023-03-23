package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 物业化公司
 * </p>
 *
 * @author wgf
 * @since 2021-11-23
 */
@ApiModel(value="ProfessionalCompany对象", description="物业化公司")
public class ProfessionalCompany extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "物业公司名称")
    private String companyName;

    @ApiModelProperty(value = "负责人名称")
    private String principal;

    @ApiModelProperty(value = "负责人电话")
    private String phone;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ProfessionalCompany{" +
            "id=" + id +
            ", companyName=" + companyName +
            ", principal=" + principal +
            ", phone=" + phone +
            ", createTime=" + createTime +
        "}";
    }
}
