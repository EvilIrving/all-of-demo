package com.ygkj.visualization.vo.request;

import lombok.Data;

@Data
public class CollectPointQueryVo {

    private String longitude;

    private String latitude;

    public CollectPointQueryVo(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
