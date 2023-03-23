package com.ygkj.digitization.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xq
 * @Description 值班常用电话 联系类型
 * @Date 2021/1/14
 */
public enum MonitorContactType {
    PERSON("人员", 1), DEPT("单位", 2);

    String typeName;
    Integer typeValue;

    MonitorContactType(String typeName, Integer typeValue) {
        this.typeName = typeName;
        this.typeValue = typeValue;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public static String typeValueToName(Integer typeValueParam){
        if (typeValueParam==null){
            return null;
        }else if (PERSON.typeValue.equals(typeValueParam)){
            return PERSON.typeName;
        }else if (DEPT.typeValue.equals(typeValueParam)){
            return DEPT.typeName;
        }else {
            return null;
        }
    }
}
