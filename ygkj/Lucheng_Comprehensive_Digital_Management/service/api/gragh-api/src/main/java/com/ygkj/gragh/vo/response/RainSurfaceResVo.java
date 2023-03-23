package com.ygkj.gragh.vo.response;

import lombok.Data;

/**
 * @author wgf
 * @date 2022/4/19 9:30
 * @Description:
 */
@Data
public class RainSurfaceResVo {

    private String geoJsonPogyLon;

    private String geoJsonPolyLine;

    public RainSurfaceResVo(String geoJsonPogyLon, String geoJsonPolyLine) {
        this.geoJsonPogyLon = geoJsonPogyLon;
        this.geoJsonPolyLine = geoJsonPolyLine;
    }
}

