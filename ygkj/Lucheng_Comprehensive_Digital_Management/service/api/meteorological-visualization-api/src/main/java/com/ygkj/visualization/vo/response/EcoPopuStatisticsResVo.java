package com.ygkj.visualization.vo.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Date 2021/2/24
 */
@Data
public class EcoPopuStatisticsResVo {
    /**
     * gdp
     */
    List<Map<String, Object>> gdpSumByYear;
    /**
     * 人口 population
     */
    List<Map<String, Object>> popuSumByYear;
}
