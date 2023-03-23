package com.ygkj.auth.api.company.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: huangzh
 * @Date: 2020/8/24 16:48
 * @Description:
 */
public class SysCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String companyName;

    private String socialCreditCode;

    private String areaCode;

    private String areaAddr;

    private String managerName;

    private String phone;

    private String industryCategory;

    private Date registerTime;

    private Integer status;

    private Integer deleted;

    private Date createTime;

    private Date updateTime;

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

    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaAddr() {
        return areaAddr;
    }

    public void setAreaAddr(String areaAddr) {
        this.areaAddr = areaAddr;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIndustryCategory() {
        return industryCategory;
    }

    public void setIndustryCategory(String industryCategory) {
        this.industryCategory = industryCategory;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SysCompany{" +
                "id='" + id + '\'' +
                ", companyName='" + companyName + '\'' +
                ", socialCreditCode='" + socialCreditCode + '\'' +
                ", areaCode='" + areaCode + '\'' +
                ", areaAddr='" + areaAddr + '\'' +
                ", managerName='" + managerName + '\'' +
                ", phone='" + phone + '\'' +
                ", industryCategory='" + industryCategory + '\'' +
                ", registerTime=" + registerTime +
                ", status=" + status +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
