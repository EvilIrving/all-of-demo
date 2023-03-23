package com.ygkj.gragh.vo.request;

import lombok.Data;

import java.util.Date;

/**
 * @author wgf
 * @date 2022/5/25 13:56
 * @Description:
 */
@Data
public class SelectTideByTimeCodeVo {
    private String table;
    private java.util.List<String> stcds;
    private Date dataTime;
}
