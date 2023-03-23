package com.ygkj.big.screen.vo.request;

import com.ygkj.big.screen.model.BsDrillCase;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/7/12
 */
@Data
public class DrillCaseResVo extends BsDrillCase {

    private static final long serialVersionUID = -6950681513922348979L;

    Date futureStartTime;

    Date futureEndTime;

}
