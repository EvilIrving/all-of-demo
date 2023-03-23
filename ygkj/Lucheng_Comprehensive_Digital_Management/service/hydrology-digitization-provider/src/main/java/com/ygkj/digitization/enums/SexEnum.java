package com.ygkj.digitization.enums;

/**
 * @author xq
 * @Description 性别枚举
 * @Date 2021/1/14
 */
public enum SexEnum {
    MALE(1,"男"),
    FEMALE(2,"女");
    Integer sex;
    String sexName;

    SexEnum(Integer sex,String sexName){
        this.sex=sex;
        this.sexName=sexName;
    }
    public Integer getSex(){
        return sex;
    }

    public String getSexName(){
        return sexName;
    }
}
