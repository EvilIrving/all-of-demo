package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.feign.WraClient;
import com.ygkj.big.screen.mapper.ProjectMapper;
import com.ygkj.big.screen.mapper.WaterResourceBulletinMapper;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.vo.request.RsvrStationWaterReqVo;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xq
 * @Description 大中型水库蓄水量
 * @Date 2021/7/29
 */
@Component
@Slf4j
public class WrbRsvrStoreManager extends MemStackCachedUnitManager {

    @Resource
    WaterResourceBulletinMapper wrbMapper;

    @Resource
    ProjectMapper projectMapper;

    @Resource
    ProjectManageClient projectManageClient;

    @Autowired
    WraClient wraClient;

    public WrbRsvrStoreManager(@Autowired ApplicationContext context) {
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
            case "stats4WrbRsvrStore": {
                Integer year = reqVo.getParam("year", Integer.class);
                return stats4WrbRsvrStore(year);
            }
            case "statsCwindow4WrbRsvrStore": {
//                Object result = null;
//                if ((result = cache.get(buildDefaultCacheKey("statsCwindow4WrbRsvrStore"))) == null) {
//                    result = statsCwindow4WrbRsvrStore();
//                }
//                return result;
                Integer year = reqVo.getParam("year", Integer.class);
                return statsCwindow4WrbRsvrStore(year);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
//        log.info("提交了一项水库蓄水量缓存任务");
//        cache.put(buildDefaultCacheKey("statsCwindow4WrbRsvrStore"), statsCwindow4WrbRsvrStore());
    }

//    @Override
//    public ExecutorService getExecutor() {
//        return getDefaultConfigExecutor();
//    }

    /**
     * 每5分钟提交一次任务
     */
//    @Scheduled(cron = "0 1/5 * * * ? ")
//    public void updateCacheTask() {
//        doBuild(null);
//    }


    /**
     * 水库蓄水量公报统计
     *
     * @param year
     * @return
     */
    public Object stats4WrbRsvrStore(Integer year) {
//        if (year == null) {
//            year = this.wrbMapper.selectMaxYearByCondition("wrb_store_rsvr", null);
//        }
//        List<WrbStoreRsvr> selectYearList = wrbMapper.selectWrbStoreRsvr(year, null);
//        selectYearList = selectYearList == null ? Collections.emptyList() : selectYearList;
//        List<WrbStoreRsvr> lastYearList = wrbMapper.selectWrbStoreRsvr(year - 1, null);
//        lastYearList = lastYearList == null ? Collections.emptyList() : lastYearList;
//
//        Map<String, List<WrbStoreRsvr>> selectYearGroupMap = selectYearList.stream().collect(Collectors.groupingBy(WrbStoreRsvr::getResCode));
//        Map<String, List<WrbStoreRsvr>> lastYearMap = lastYearList.stream().collect(Collectors.groupingBy(WrbStoreRsvr::getResCode));
//        Set<String> resCodeSet = new HashSet<>(selectYearGroupMap.size() + lastYearMap.size());
//        resCodeSet.addAll(selectYearGroupMap.keySet());
//        resCodeSet.addAll(lastYearMap.keySet());
//
//        List<AttResBase> rsvrList = projectMapper.selectCodeNameOfRsvrByCodes(String.join(",", resCodeSet));
//        rsvrList = rsvrList == null ? Collections.emptyList() : rsvrList;
//
//        List<JSONObject> selectYearResultList = new ArrayList<>(rsvrList.size());
//        List<JSONObject> lastYearResultList = new ArrayList<>(rsvrList.size());
//
//        JSONObject result = new JSONObject();
//        for (AttResBase rsvr : rsvrList) {
//            List<WrbStoreRsvr> selectYearTempList = selectYearGroupMap.getOrDefault(rsvr.getResCode(), Collections.emptyList());
//            BigDecimal selectYearWaterStore = BigDecimal.ZERO;
//            if (CollectionUtils.isNotBlank(selectYearTempList) && selectYearTempList.get(0).getEndYearStore() != null) {
//                selectYearWaterStore = selectYearTempList.get(0).getEndYearStore();
//            }
//            JSONObject selectYearTemp = new JSONObject();
//            selectYearTemp.put("resName", rsvr.getResName());
//            selectYearTemp.put("waterStore", selectYearWaterStore);
//            selectYearResultList.add(selectYearTemp);
//
//            List<WrbStoreRsvr> lastYearTempList = lastYearMap.getOrDefault(rsvr.getResCode(), Collections.emptyList());
//            BigDecimal lastYearWaterStore = BigDecimal.ZERO;
//            if (CollectionUtils.isNotBlank(lastYearTempList) && lastYearTempList.get(0).getEndYearStore() != null) {
//                lastYearWaterStore = lastYearTempList.get(0).getEndYearStore();
//            }
//            JSONObject lastYearTemp = new JSONObject();
//            lastYearTemp.put("resName", rsvr.getResName());
//            lastYearTemp.put("waterStore", lastYearWaterStore);
//            lastYearResultList.add(lastYearTemp);
//        }
//        result.put("selectYearList", selectYearResultList);
//        result.put("lastYearList", lastYearResultList);
//        result.put("year", CollectionUtils.isNotBlank(selectYearList) ? selectYearList.get(0).getYear() : year);
//        return result;
        return wraClient.unitCard4WrbStoreRsvr(year).getData();
    }

    /**
     * 水库蓄水量公报C位统计
     *
     * @return
     */
    public JSONObject statsCwindow4WrbRsvrStore(Integer year) {
//        if (year == null) {
//            year = this.wrbMapper.selectMaxYearByCondition("wrb_store_rsvr", null);
//        }
//        List<WrbStoreRsvr> selectYearList = wrbMapper.selectWrbStoreRsvr(year, null);
//        selectYearList = selectYearList == null ? Collections.emptyList() : selectYearList;
//        CommonResult<PageVO<LinkedHashMap>> resp=projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
//                .with(RsvrStationWaterReqVo::setEngScales, String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()))
//                .with(RsvrStationWaterReqVo::setWaterLevelStorage, true)
//                .build());
//        double totalStore=0D;
//        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
//            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
//            totalStore=data.stream()
//                    .filter(o->o.getWaterLevelCapacity()!=null)
//                    .mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity)
//                    .sum()/1000;//万m³转成亿m³
//        }
//        JSONObject result = new JSONObject();
//        result.put("totalStore", Double.parseDouble(String.format("%.2f", totalStore)));
//        result.put("year", LocalDate.now().getYear());
//        return result;
        return JSONObject.parseObject(JSONObject.toJSONString(wraClient.unitCWindows4WrbStoreRsvr(year).getData(),
                SerializerFeature.WriteMapNullValue));
    }
}
