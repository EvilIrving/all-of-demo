package com.ygkj.big.screen.vo.request;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
@Data
@ToString
public class DoDrillParamReqVo {

    // true 表示被影响 false表示不被影响
    boolean effect = false;

    String caseId;

    Map<Date, String> effectGroupIdMap;
}
