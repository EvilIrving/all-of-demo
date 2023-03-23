package com.ygkj.data.management.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class BusDataTableApplyResVo {

    private String id;

    private String tableName;

    private String category;

    private String basis;

    private String applyNumber;

    private String applicantDepartment;

    private String applicant;

    private String applicantId;

    private String status;

    private String examinePerson;

    private Date createTime;

    private String dataName;

    private Date updateTime;
}
