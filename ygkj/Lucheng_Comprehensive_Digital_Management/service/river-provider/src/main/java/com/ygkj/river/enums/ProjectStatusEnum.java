package com.ygkj.river.enums;

/**
 * @author lxl
 * @create 2021-10-20 14:24
 * @description 项目阶段枚举
 */
public enum ProjectStatusEnum {

    //项目阶段 1-前期阶段 2-施工阶段 3-完工阶段
    EARLY("前期阶段", 1),
    CONSTRUCTION("施工阶段", 2),
    FINISHED("完工阶段",3);

    private Integer value;

    private String name;


    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    ProjectStatusEnum(String name, Integer value) {
        this.value = value;
        this.name = name;
    }

    public static ProjectStatusEnum getStatusEnumByCode(Integer code){
        for(ProjectStatusEnum statusEnum : ProjectStatusEnum.values()){
            if(code.equals(statusEnum.getValue())){
                return statusEnum;
            }
        }
        return null;
    }


}
