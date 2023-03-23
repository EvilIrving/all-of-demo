package com.ygkj.river.enums;

/**
 * @author lxl
 * @create 2021-10-20 15:08
 * @description 是否枚举
 */
public enum YesOrNoEnum {


    YES("是",1),
    NO("否",0);

    private Integer value;

    private String key;


    public Integer getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }

    YesOrNoEnum(String key, Integer value) {
        this.value = value;
        this.key = key;
    }


    public static YesOrNoEnum getStatusEnumByCode(Integer code){
        for(YesOrNoEnum statusEnum : YesOrNoEnum.values()){
            if(code.equals(statusEnum.getValue())){
                return statusEnum;
            }
        }
        return null;
    }


}
