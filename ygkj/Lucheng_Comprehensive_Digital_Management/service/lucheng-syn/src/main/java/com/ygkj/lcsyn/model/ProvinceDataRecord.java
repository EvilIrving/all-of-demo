package com.ygkj.lcsyn.model;

import com.ygkj.lcsyn.enums.ProvinceDataEnum;
import lombok.Data;

import java.util.Date;

@Data
public class ProvinceDataRecord {

    private Integer id;

    private String url;

    private String describe;

    private Date tm;

    private Integer result;

    private String res;

    private String param;

    public ProvinceDataRecord(ProvinceDataEnum provinceDataEnum){
        this.url = provinceDataEnum.getUrl();
        this.describe = provinceDataEnum.getDescribe();
        this.tm = new Date();
    }
}
