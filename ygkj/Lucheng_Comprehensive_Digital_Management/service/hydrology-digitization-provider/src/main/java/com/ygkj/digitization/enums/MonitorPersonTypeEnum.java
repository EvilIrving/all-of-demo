package com.ygkj.digitization.enums;

/**
 * @author xq
 * @Description 值班人员管理 人员类型
 * @Date 2021/1/14
 */
public enum MonitorPersonTypeEnum {
    FBRY(1,"副班人员"),
    ZBRY(2,"值班人员"),
    DBLD(3,"带班领导");
    Integer personType;
    String personTypeName;
    MonitorPersonTypeEnum(Integer personType,String personTypeName){
        this.personType=personType;
        this.personTypeName=personTypeName;
    }

    public Integer getPersonType(){
        return personType;
    }

    public String getPersonTypeName(){
        return personTypeName;
    }
}
