package com.ygkj.lcsyn.model;

import lombok.Data;

import java.util.Date;

@Data
public class StPptnR {

    private String mgstcd;

    private String stcd;

    private Date tm;

    private String drp;

    private String intv;

    public void setStcd(String stcd){
        this.stcd = stcd.trim();
        this.mgstcd = this.stcd;
    }

}
