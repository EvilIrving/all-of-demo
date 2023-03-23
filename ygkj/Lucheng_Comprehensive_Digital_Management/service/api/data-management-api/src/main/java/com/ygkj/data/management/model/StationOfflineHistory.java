package com.ygkj.data.management.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 测站离线时间记录表
 * @date 2021-07-21
 */
@Data
public class StationOfflineHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 离线时间
     */
    private Date offlineTime;

    /**
     * 测站编码
     */
    private String stationCode;

    private String stationType;

    public StationOfflineHistory(Date offlineTime, String stationCode, String stationType) {
        this.offlineTime = offlineTime;
        this.stationCode = stationCode;
        this.stationType = stationType;
    }

    public StationOfflineHistory() {
    }

}


