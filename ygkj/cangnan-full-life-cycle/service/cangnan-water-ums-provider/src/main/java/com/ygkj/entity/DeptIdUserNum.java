package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * 
 * @Date 2021/1/13
 */
@Data
public class DeptIdUserNum implements Serializable {
    String deptId;
    Integer num;

    public Integer getNumOrDefault(Integer defaultVal){
        return num==null?defaultVal:num;
    }
}
