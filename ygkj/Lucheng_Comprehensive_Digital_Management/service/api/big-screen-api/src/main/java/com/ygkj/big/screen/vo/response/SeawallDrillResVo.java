package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.AttSeawallBase;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/24
 */
@Data
public class SeawallDrillResVo extends AttSeawallBase {
    private static final long serialVersionUID = 8239516629564843843L;

    Double tdz;

    Date dataTime;

    String warnState;
}
