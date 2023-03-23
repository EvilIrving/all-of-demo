package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.WraClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.WaterResourceBulletinMapper;
import com.ygkj.big.screen.model.AttAdcdBase;
import com.ygkj.big.screen.model.WrbWaterResource;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/29
 */
@Component
public class WrbWraManager extends AbstractUnitManager {

    @Resource
    WaterResourceBulletinMapper wrbMapper;

    @Resource
    AttAdcdBaseMapper adcdBaseMapper;

    @Resource
    WraClient wraClient;

    public WrbWraManager(@Autowired ApplicationContext context) {
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
            case "stats4WrbWra": {
                Integer year = reqVo.getParam("year", Integer.class);
                Integer type = reqVo.getParam("type", Integer.class);
                return stats4WrbWra(year, type);
            }
            case "statsCwindow4WrbWra": {
                Integer year = reqVo.getParam("year", Integer.class);
                return statsCwindow4WrbWra(year);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    /**
     * 水资源量公报统计
     *
     * @param year
     * @param type 1：水资源总量，2：人均水资源量
     * @return
     */
    public List<JSONObject> stats4WrbWra(Integer year, Integer type) {
//        switch (type) {
//            case 1:
//                return stats4WrbWraWithTotalWra(year);
//            case 2:
//                return stats4WrbWraWithAvgWra(year);
//            default:
//                return null;
//        }

        return JSONArray.parseArray(JSONObject.toJSONString(
                wraClient.unitCard4WrbWatRes(year, type).getData(),
                SerializerFeature.WriteMapNullValue), JSONObject.class);
    }

    public List<JSONObject> stats4WrbWraWithTotalWra(Integer year) {
        List<AttAdcdBase> adcdList = this.adcdBaseMapper.adcdAdnmCountyOfWzExcludeCityLevel();
        adcdList = adcdList == null ? Collections.emptyList() : adcdList;
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_resource", null);
        }
        List<WrbWaterResource> wraList = wrbMapper.selectWrbWaterResource(year, null);
        wraList = wraList == null ? Collections.emptyList() : wraList;
        Integer tempYear = CollectionUtils.isNotBlank(wraList) ? wraList.get(0).getYear() : year;
        Map<String, List<WrbWaterResource>> groupMap = wraList.stream().collect(Collectors.groupingBy(WrbWaterResource::getAdcd));
        List<JSONObject> list = new ArrayList<>(adcdList.size());
        for (AttAdcdBase adcd : adcdList) {
            List<WrbWaterResource> tempList = groupMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
            JSONObject temp = new JSONObject();
            temp.put("areaName", adcd.getAdnm());
            temp.put("totalWra", tempList.stream().filter(o -> o.getTotalWaterResource() != null).mapToDouble(o -> o.getTotalWaterResource().doubleValue()).sum());
            temp.put("year", tempYear);
            list.add(temp);
        }
        return list;
    }

    public List<JSONObject> stats4WrbWraWithAvgWra(Integer year) {
        List<AttAdcdBase> adcdList = this.adcdBaseMapper.adcdAdnmCountyOfWzExcludeCityLevel();
        adcdList = adcdList == null ? Collections.emptyList() : adcdList;
        if (year == null) {
            year = this.wrbMapper.selectMaxYearByCondition("wrb_water_resource", null);
        }
        List<WrbWaterResource> wraList = wrbMapper.selectWrbWaterResource(year, null);
        wraList = wraList == null ? Collections.emptyList() : wraList;
        Integer tempYear = CollectionUtils.isNotBlank(wraList) ? wraList.get(0).getYear() : year;
        Map<String, List<WrbWaterResource>> groupMap = wraList.stream().collect(Collectors.groupingBy(WrbWaterResource::getAdcd));
        List<JSONObject> list = new ArrayList<>(adcdList.size());
        for (AttAdcdBase adcd : adcdList) {
            List<WrbWaterResource> tempList = groupMap.getOrDefault(adcd.getAdcd(), Collections.emptyList());
            JSONObject temp = new JSONObject();
            temp.put("areaName", adcd.getAdnm());
            BigDecimal avgWrUse = BigDecimal.ZERO;
            if (CollectionUtils.isNotBlank(tempList) && (tempList.get(0).getAvgWrUse() != null)) {
                avgWrUse = tempList.get(0).getAvgWrUse();
            }
            temp.put("avgWrUse", avgWrUse);
            temp.put("year", tempYear);
            list.add(temp);
        }
        return list;
    }

    /**
     * 水资源量公报C位统计
     *
     * @return
     */
    public JSONObject statsCwindow4WrbWra(Integer year) {
//        if(year==null){
//            year=this.wrbMapper.selectMaxYearByCondition("wrb_rainfall","330300000000");
//        }
//        List<WrbWaterResource> wraList= wrbMapper.selectWrbWaterResource(year,"330300000000");
//        wraList=wraList==null?Collections.emptyList():wraList;
//        BigDecimal totalWaterResource=BigDecimal.ZERO;
//        BigDecimal avgWrUseRate=BigDecimal.ZERO;
//        BigDecimal avgWrUse=BigDecimal.ZERO;
//        if (CollectionUtils.isNotBlank(wraList)){
//            WrbWaterResource record=wraList.get(0);
//            totalWaterResource=record.getTotalWaterResource();
//            avgWrUseRate=record.getAvgWrUseRate();
//            avgWrUse=record.getAvgWrUse();
//        }
//        JSONObject result=new JSONObject();
//        result.put("totalWaterResource",totalWaterResource);
//        result.put("avgWrUseRate",avgWrUseRate);
//        result.put("avgWrUse",avgWrUse);
//        result.put("year",CollectionUtils.isNotBlank(wraList)?wraList.get(0).getYear():year);
//        return result;
        return JSONObject.parseObject(JSONObject.toJSONString(wraClient.unitCWindows4WrbWatRes(year).getData(), SerializerFeature.WriteMapNullValue));
    }
}
