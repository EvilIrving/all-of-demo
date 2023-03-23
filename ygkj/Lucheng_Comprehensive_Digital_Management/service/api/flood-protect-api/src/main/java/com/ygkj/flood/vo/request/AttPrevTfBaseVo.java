package com.ygkj.flood.vo.request;

import lombok.Data;

@Data
public class AttPrevTfBaseVo {
    /*
     是否预警 true是预警
     */
    private String isAlarm;

    /*
     自然村
     */
    private String naturalAdnm;
}
