package com.ygkj.project.model;

import lombok.Data;

/**
 * @author wgf
 * @date 2022/5/20 14:41
 * @Description: 未来24小时风速
 */
@Data
public class FutureWindSpeed {
    private String time;
    private Double windSpeed;
}
