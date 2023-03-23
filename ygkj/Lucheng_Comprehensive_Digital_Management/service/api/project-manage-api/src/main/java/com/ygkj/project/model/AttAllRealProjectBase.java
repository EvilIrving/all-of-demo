package com.ygkj.project.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Data
public class AttAllRealProjectBase implements Serializable {

    private static final long serialVersionUID = -4197104062998721118L;

    String projectCode;
    String projectName;
    BigDecimal longitude;
    BigDecimal latitude;
    String projectType;
    Integer delFlag;
    String adcd;
    String bas;
    String engScal;
    String engGrad;
    String govermentChargeId;
    String waterDepartmentChargeId;
    String competentDepartmentChargeId;
    String manageDepartmentChargeId;
    String technologyChargeId;
    String patrolChargeId;
}
