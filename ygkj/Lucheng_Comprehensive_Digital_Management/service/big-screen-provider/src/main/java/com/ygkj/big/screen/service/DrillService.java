package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.BsDrillCase;
import com.ygkj.big.screen.vo.request.DrillCaseResVo;
import com.ygkj.big.screen.vo.response.UnitDataResVo;
import com.ygkj.water.model.CommonResult;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
public interface DrillService {

    CommonResult startDrillAffair(String caseId);

    CommonResult closeDrillAffair(String affairId);

    List<DrillCaseResVo> listDrillCase(String unitName, Integer year, String caseType);

    BsDrillCase getDrillCaseByAffairId(String affairId);

    String getDrillCaseIdByAffairId(String affairId);

    String getDrillData(String caseId, String unitName, String dataApi, String dataParam, Date timestamp, Integer dataType);

    String addOrUpdateDrillData(Map<String, List<UnitDataResVo>> dataMap,
                                Map<Date, String> effectPreGroupIdMap, Date dataTime);
}
