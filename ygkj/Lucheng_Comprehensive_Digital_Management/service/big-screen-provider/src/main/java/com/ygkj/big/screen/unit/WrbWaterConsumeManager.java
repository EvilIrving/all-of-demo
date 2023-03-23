package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.WraClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.WaterResourceBulletinMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.WrbRainfall;
import com.ygkj.big.screen.model.WrbReducedWater;
import com.ygkj.big.screen.model.WrbWaterConsume;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description 水资源公报-用水量
 * @Date 2021/7/29
 */
@Component
public class WrbWaterConsumeManager extends AbstractUnitManager {

    @Resource
    WaterResourceBulletinMapper wrbMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;

    @Resource
    WraClient wraClient;

    public WrbWaterConsumeManager(@Autowired ApplicationContext context) {
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
            case "stats4WrbWaterConsume": {
                Integer year = reqVo.getParam("year", Integer.class);
                Integer type = reqVo.getParam("type", Integer.class);
                return stats4WrbWaterConsume(year, type);
            }
            case "statsCwindow4WrbWaterConsume": {
                Integer year = reqVo.getParam("year", Integer.class);
                return statsCwindow4WrbWaterConsume(year);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }


    /**
     * 用水量公报统计
     *
     * @param year
     * @param type 1：行业类别，2：行政区划，3：人均综合用水量
     * @return
     */
    public List<JSONObject> stats4WrbWaterConsume(Integer year, Integer type) {
//        switch (type){
//            case 1:
//                return stats4WrbWaterConsumeByClassify(year);
//            case 2:
//                return stats4WrbWaterConsumeByAdcd(year);
//            default:
//                return null;
//        }
        return JSONArray.parseArray(JSONObject.toJSONString(
                wraClient.unitCard4WrbWatCon(year, type).getData(),
                SerializerFeature.WriteMapNullValue), JSONObject.class);
    }

    public List<JSONObject> stats4WrbWaterConsumeByClassify(Integer year) {
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_consume", null);
        }
        List<WrbWaterConsume> consumeList = wrbMapper.selectWrbWaterConsume(year, null);
        consumeList = consumeList == null ? Collections.emptyList() : consumeList;
        Integer tempYear = CollectionUtils.isNotBlank(consumeList) ? consumeList.get(0).getYear() : year;
        List<JSONObject> list = new ArrayList<>(6);
        JSONObject temp = new JSONObject();
        temp.put("classifyName", "农田灌溉");
        temp.put("classifyValue", consumeList.stream().filter(o -> o.getConArgIrr() != null).mapToDouble(o -> o.getConArgIrr().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "林牧渔畜");
        temp.put("classifyValue", consumeList.stream().filter(o -> o.getConFahf() != null).mapToDouble(o -> o.getConFahf().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "工业用水");
        temp.put("classifyValue", consumeList.stream().filter(o -> o.getConIndu() != null).mapToDouble(o -> o.getConIndu().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "城镇公共");
        temp.put("classifyValue", consumeList.stream().filter(o -> o.getConCityPub() != null).mapToDouble(o -> o.getConCityPub().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "居民生活");
        temp.put("classifyValue", consumeList.stream().filter(o -> o.getConResidLife() != null).mapToDouble(o -> o.getConResidLife().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);

        temp = new JSONObject();
        temp.put("classifyName", "生态环境");
        temp.put("classifyValue", consumeList.stream().filter(o -> o.getConEcoEnv() != null).mapToDouble(o -> o.getConEcoEnv().doubleValue()).sum());
        temp.put("year", tempYear);
        list.add(temp);
        return list;
    }

    public List<JSONObject> stats4WrbWaterConsumeByAdcd(Integer year) {
        List<AttAdcdBase> adcdList = this.adcdBaseMapper.adcdAdnmCountyOfWzExcludeCityLevel();
        adcdList = adcdList == null ? Collections.emptyList() : adcdList;
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_consume", null);
        }
        List<WrbWaterConsume> consumeList = wrbMapper.selectWrbWaterConsume(year, null);
        consumeList = consumeList == null ? Collections.emptyList() : consumeList;
        Integer tempYear = CollectionUtils.isNotBlank(consumeList) ? consumeList.get(0).getYear() : year;
        Map<String, List<WrbWaterConsume>> groupMap = consumeList.stream().collect(Collectors.groupingBy(WrbWaterConsume::getAdcd));
        List<JSONObject> list = new ArrayList<>(adcdList.size());
        for (AttAdcdBase adcd : adcdList) {
            List<WrbWaterConsume> tempList = groupMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
            JSONObject temp = new JSONObject();
            temp.put("areaName", adcd.getAdnm());
            temp.put("waterConsume", tempList.stream().filter(o -> o.getTotalConsume() != null).mapToDouble(o -> o.getTotalConsume().doubleValue()).sum());
            temp.put("year", tempYear);
            list.add(temp);
        }
        return list;
    }

    /**
     * 供水量公报C位统计
     *
     * @return
     */
    public JSONObject statsCwindow4WrbWaterConsume(Integer year) {
//        if(year==null){
//            year=this.wrbMapper.selectMaxYearByCondition("wrb_water_consume","330300000000");
//        }
//        List<WrbWaterConsume> consumeList= wrbMapper.selectWrbWaterConsume(year,"330300000000");
//        consumeList=consumeList==null?Collections.emptyList():consumeList;
//        BigDecimal totalConsume=BigDecimal.ZERO;
//        if (CollectionUtils.isNotBlank(consumeList)){
//            WrbWaterConsume record=consumeList.get(0);
//            totalConsume=record.getTotalConsume();
//        }
//        JSONObject result=new JSONObject();
//        result.put("totalConsume",totalConsume);
//        result.put("year",CollectionUtils.isNotBlank(consumeList)?consumeList.get(0).getYear():year);
//        return result;
        return JSONObject.parseObject(JSONObject.toJSONString(
                wraClient.unitCWindows4WrbWatCon(year).getData(),
                SerializerFeature.WriteMapNullValue));
    }
}
