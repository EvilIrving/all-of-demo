package com.ygkj.big.screen.vo.request;

import com.ygkj.big.screen.model.BsDrillCase;
import com.ygkj.big.screen.model.BsDrillStcase;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
@Data
public class DrillParamReqVo {
    BsDrillCase drillCase;

    List<BsDrillStcase> stcaseList;

    Date endTime;

    String unitId;

    String unitName;

    int timeIndex;

    String dataGroupId;

}
