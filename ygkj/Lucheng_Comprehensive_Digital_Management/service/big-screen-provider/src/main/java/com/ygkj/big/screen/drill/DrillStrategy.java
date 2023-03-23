package com.ygkj.big.screen.drill;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.model.BsUnitData;
import com.ygkj.big.screen.vo.request.DrillParamReqVo;
import com.ygkj.big.screen.vo.response.UnitDataResVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author xq
 * @Description
 * @Date 2021/7/8
 */
public interface DrillStrategy {

    Map<String, List<UnitDataResVo>> buildData(DrillParamReqVo reqVo, Date endTime);

    default UnitDataResVo buildUnitData(Object originData,
                                        String api,
                                        String param,
                                        long timeIndex,
                                        Date timeStamp,
                                        String caseId,
                                        String unitName,
                                        String dataGroupId,
                                        String data) {
        UnitDataResVo unitData = new UnitDataResVo();
        unitData.setDataId(data);
        unitData.setGroupId(dataGroupId);
        unitData.setUnitName(unitName);
        unitData.setCaseId(caseId);
        unitData.setTimeIndex(timeIndex);
        unitData.setTimeStamp(timeStamp);
        unitData.setDataApi(api);
        unitData.setDataParam(param);
        unitData.setDataType(0);
//        JSONObject object = (JSONObject) JSONObject.toJSON(originData);
        unitData.setDataValue(JSONObject.toJSONString(originData, SerializerFeature.WriteMapNullValue));
        unitData.setOriginData(originData);
        unitData.setOriginDataType(originData.getClass());
        return unitData;
    }
}
