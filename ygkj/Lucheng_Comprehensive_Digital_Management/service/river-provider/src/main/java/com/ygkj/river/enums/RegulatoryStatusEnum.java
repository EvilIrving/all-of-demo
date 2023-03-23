package com.ygkj.river.enums;

/**
 * @author lxl
 * @create 2021-10-20 14:39
 * @description 监管状态枚举
 */
public enum RegulatoryStatusEnum {

    //监管状态
    // 1）新增项目，未做任何操作【待监管】
    // 2）移动端上传无异常问题 【无问题】；检查/复核有问题 【待整改】；复核无问题 【无问题】
    // 3）整改期限内未整改；【已逾期】；
    // 4）判断为隐患后，移交执法大队【已移交】，不是隐患，【无问题】；
    // 5）完工阶段，监管无问题的，【已完工】
    NEW("待监管", 1),
    CONDUCT("待整改", 2),
    REVIEW("已逾期",3),
    TRANSFER("已移交",4),
    NO_PROBLEM("无问题",5),
    OVER("已完工",6);

    private Integer value;

    private String name;


    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    RegulatoryStatusEnum(String name, Integer value) {
        this.value = value;
        this.name = name;
    }

    public static RegulatoryStatusEnum getStatusEnumByCode(Integer code){
        for(RegulatoryStatusEnum statusEnum : RegulatoryStatusEnum.values()){
            if(code.equals(statusEnum.getValue())){
                return statusEnum;
            }
        }
        return null;
    }


}
