package com.ygkj.gragh.model;

import lombok.Data;

import java.util.List;

@Data
public class YongjiaMetaData {

    List<YongjiaRain> rains;

    List<YongjiaWater> waters;

    public YongjiaMetaData(List<YongjiaRain> rains, List<YongjiaWater> waters) {
        this.rains = rains;
        this.waters = waters;
    }
}
