package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.WraClient;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@Component
public class WResourceAbilityManager extends AbstractUnitManager {

    @Autowired
    WraClient wraClient;

    public WResourceAbilityManager(@Autowired ApplicationContext context) {
        super(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "stats4WraUnit": {
                Integer type = reqVo.getParam("type", Integer.class);
                return stats4WraUnit(type);
            }
            case "statsCwindow4WraUnit": {
                return statsCwindow4WraUnit();
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }


    /**
     * 水资源承载能力大屏组件统计接口
     *
     * @param type 1：用水量评价；2：水质评价；3：经济评价；4：人口评价；5：用水评价
     * @return
     */
    public CommonResult stats4WraUnit(Integer type) {
        switch (type) {
            case 1:
                return wraClient.waterConsumeEvaluation(null);
            case 2:
                return wraClient.waterQualityEvaluation(null);
            case 3:
                return wraClient.ecoAppraisal(null);
            case 4:
                return wraClient.popAppraisal(null);
            case 5:
                return wraClient.watGdpPreCurveByDouExpoModel(null, null);
            default:
                return null;
        }
    }

    /**
     * 水资源承载能力大屏组件C位统计接口
     *
     * @return
     */
    public JSONObject statsCwindow4WraUnit() {
        int consumeOverLoadNum = 0;
        int qualityOverLoadNum = 0;
        Integer watConSatYear = null;
        CommonResult respConsume = wraClient.waterConsumeEvaluation(null);
        if (respConsume.getData() != null) {
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(respConsume.getData(), SerializerFeature.WriteMapNullValue));
            consumeOverLoadNum += data.getIntValue("overload");
            consumeOverLoadNum += data.getIntValue("superOverload");
        }
        CommonResult respQuality = wraClient.waterQualityEvaluation(null);
        if (respQuality.getData() != null) {
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(respQuality.getData(), SerializerFeature.WriteMapNullValue));
            qualityOverLoadNum += data.getIntValue("overload");
            qualityOverLoadNum += data.getIntValue("superOverload");
        }
        CommonResult watGdpPredictResp = wraClient.watGdpPreCurveByDouExpoModel(null, null);
        if (watGdpPredictResp.getData() != null) {
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(watGdpPredictResp.getData(), SerializerFeature.WriteMapNullValue));
            watConSatYear = data.getInteger("watConSatYear");
        }
        if (watConSatYear == null) {
            CommonResult respTemp = wraClient.watGdpPreCurveByTrendPreModel(null, null);
            if (respTemp.getData() != null) {
                JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(respTemp.getData(), SerializerFeature.WriteMapNullValue));
                watConSatYear = data.getInteger("watConSatYear");
            }
        }
        JSONObject result = new JSONObject();
        result.put("consumeOverLoadNum", consumeOverLoadNum);
        result.put("qualityOverLoadNum", qualityOverLoadNum);
        result.put("consumeSatYear", watConSatYear);
        return result;
    }
}
