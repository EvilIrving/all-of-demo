package com.ygkj.big.screen.vo.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/7/5
 */
@Data
public class MountainTorrentReqVo implements Serializable {
    private static final long serialVersionUID = -2147076468537742862L;
    String id;
    BigDecimal period;
    BigDecimal rainfall;
}
