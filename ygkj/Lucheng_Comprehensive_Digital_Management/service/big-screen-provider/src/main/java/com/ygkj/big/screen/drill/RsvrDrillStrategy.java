package com.ygkj.big.screen.drill;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.BsDrillStdataMapper;
import com.ygkj.big.screen.mapper.ProjectMapper;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.BsDrillCase;
import com.ygkj.big.screen.model.BsDrillStdata;
import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.big.screen.vo.request.DrillParamReqVo;
import com.ygkj.big.screen.vo.request.ProjectManageQueryVo;
import com.ygkj.big.screen.vo.request.RsvrStationWaterReqVo;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import com.ygkj.big.screen.vo.response.UnitDataResVo;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.gragh.model.AttResBase;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
public class RsvrDrillStrategy extends AbstractDrillStrategy {

    //    @Autowired
    GenerateDistributionIDService idService;

    private ProjectMapper projectMapper;
    private ProjectManageClient projectManageClient;
    private BsDrillStdataMapper stdataMapper;

    public RsvrDrillStrategy(ApplicationContext context) {
        super(context);
        idService = context.getBean(GenerateDistributionIDService.class);
        projectMapper = context.getBean(ProjectMapper.class);
        projectManageClient = context.getBean(ProjectManageClient.class);
        stdataMapper = context.getBean(BsDrillStdataMapper.class);
    }


    @Override
    public Map<String, List<UnitDataResVo>> buildData(DrillParamReqVo reqVo, Date endTime) {
        BsDrillCase drillCase = reqVo.getDrillCase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tempEndTime = format.format(endTime);
        String dataGroupId = reqVo.getDataGroupId();
        int timeIndex = reqVo.getTimeIndex();
        CommonResult<List<LinkedHashMap>> resp = projectManageClient.getRsvrDrillByTime(tempEndTime);
        List<RsvrStationWaterResVo> lmData = Collections.emptyList();
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData())) {
            lmData = JSONArray.parseArray(JSONArray.toJSONString(resp.getData()), RsvrStationWaterResVo.class);
            for (RsvrStationWaterResVo rsvr : lmData) {
                String stData = JSONObject.toJSONString(rsvr, SerializerFeature.WriteMapNullValue);
                BsDrillStdata dataObj = stdataMapper.selectNoDataByUniqueIndex(rsvr.getResCode(), drillCase.getId(), endTime);

                if (dataObj != null) {
                    dataObj.setStData(stData);
                    stdataMapper.update(dataObj);
                } else {
                    dataObj = new BsDrillStdata();
                    dataObj.setId(idService.nextID());
                    dataObj.setStcaseId(rsvr.getResCode());
                    dataObj.setCaseId(drillCase.getId());
                    dataObj.setTimeIndex(timeIndex);
                    dataObj.setTimeStamp(endTime);
                    dataObj.setStData(stData);
                    dataObj.setDataType(0);
                    stdataMapper.insert(dataObj);
                }
            }
        }
        JSONObject rsvrProjStats1 = rsvrProjStats1(lmData);
        JSONObject rsvrProjStats2 = rsvrProjStats2(lmData);
        List<UnitDataResVo> rsvrProjStats1Result = Arrays.asList(buildUnitData(rsvrProjStats1,
                "waterDisasterPrevention/rsvrProjStats1",
                null,
                timeIndex,
                endTime,
                drillCase.getId(),
                reqVo.getUnitName(),
                dataGroupId,
                idService.nextID()
        ));
        List<UnitDataResVo> rsvrProjStats2Result = Arrays.asList(buildUnitData(rsvrProjStats2,
                "waterDisasterPrevention/rsvrProjStats2",
                null,
                timeIndex,
                endTime,
                drillCase.getId(),
                reqVo.getUnitName(),
                dataGroupId,
                idService.nextID()
        ));

        Map<String, List<UnitDataResVo>> resultMap = new HashMap<>();
        resultMap.put("rsvrProjStats1", rsvrProjStats1Result);
        resultMap.put("rsvrProjStats2", rsvrProjStats2Result);
        return resultMap;
    }

    public JSONObject rsvrProjStats1(List<RsvrStationWaterResVo> lmData) {
        JSONObject result = new JSONObject();
//        long allNormal = 0, allOverLimit = 0, allTotal = 0;
        long lmNormal = 0, lmOverLimit = 0, lmTotal = 0;
        long s1Normal = 0, s1OverLimit = 0, s1Total = 0;
        long s2Normal = 0, s2OverLimit = 0, s2Total = 0;
        // 大中型水库的演练数据
        Map<String, Long> lmMap = lmData.stream().collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                Collectors.counting()));
        Long zeroL = 0L;
        lmNormal = lmMap.getOrDefault("normal", zeroL);
        lmOverLimit = lmMap.getOrDefault("overLimit", zeroL);
        lmTotal = lmData.size();
        // 预演短信报警
        List<RsvrStationWaterResVo> lmOverLimitList = lmData.stream().filter(o -> o.getOverLimitVal() != null && o.getOverLimitVal() > 0).collect(Collectors.toList());
        if (CollectionUtils.isBlank(lmOverLimitList)) {
            lmOverLimitList = Collections.emptyList();
        }
        // 查询小型水库的实时数据
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setDataTime, new Date())
                .with(RsvrStationWaterReqVo::setEngScales, String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.S1.getDesc(), EngScalEnum.S2.getDesc()))
                .build());
        if (resp != null && resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<RsvrStationWaterResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList()), RsvrStationWaterResVo.class);
            Map<String, Long> s1Map = data.stream().filter(o -> (EngScalEnum.S1.getDesc().equals(o.getEngScal()))).collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                    Collectors.counting()));
            Map<String, Long> s2Map = data.stream().filter(o -> (EngScalEnum.S2.getDesc().equals(o.getEngScal()))).collect(Collectors.groupingBy(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0) ? "overLimit" : "normal",
                    Collectors.counting()));

            s1Normal = s1Map.getOrDefault("normal", zeroL);
            s1OverLimit = s1Map.getOrDefault("overLimit", zeroL);
            s1Total = s1Normal + s1OverLimit;
            s2Normal = s2Map.getOrDefault("normal", zeroL);
            s2OverLimit = s2Map.getOrDefault("overLimit", zeroL);
            s2Total = s2Normal + s2OverLimit;
        }
        result.put("allNormal", lmNormal + s1Normal + s2Normal);
        result.put("allOverLimit", lmOverLimit + s1OverLimit + s2OverLimit);
        result.put("allTotal", lmTotal + s1Total + s2Total);
        result.put("lmNormal", lmNormal);
        result.put("lmOverLimit", lmOverLimit);
        result.put("lmTotal", lmTotal);
        result.put("s1Normal", s1Normal);
        result.put("s1OverLimit", s1OverLimit);
        result.put("s1Total", s1Total);
        result.put("s2Normal", s2Normal);
        result.put("s2OverLimit", s2OverLimit);
        result.put("s2Total", s2Total);
        result.put("lmOverLimitList", lmOverLimitList);
        return result;
    }

    private JSONObject rsvrProjStats2(List<RsvrStationWaterResVo> data) {
        JSONObject result = new JSONObject();
        double avgStorageRatio = 0D;
        double totalStorage = 0D;
        double totalCurrentStorage = 0D;
        List<RsvrStationWaterResVo> storageList = Collections.emptyList();
        if (CollectionUtils.isNotBlank(data)) {
            totalStorage = data.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
            totalCurrentStorage = data.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
            if (totalStorage > 0) {
                avgStorageRatio = totalCurrentStorage * 100 / totalStorage;
            }
            storageList = data.stream()
                    .sorted((o1, o2) -> {
                        if (o1.getFloodStorageAbility() != null && o2.getFloodStorageAbility() != null) {
                            return o1.getFloodStorageAbility().compareTo(o2.getFloodStorageAbility());
                        } else if (o1.getFloodStorageAbility() == null && o2.getFloodStorageAbility() != null) {
                            return 1;
                        } else if (o1.getFloodStorageAbility() != null && o2.getFloodStorageAbility() == null) {
                            return -1;
                        } else {
                            return 0;
                        }
                    })
                    .collect(Collectors.toList());
        }
        result.put("avgStorageRatio", Double.parseDouble(String.format("%.1f", avgStorageRatio)));
        result.put("totalStorage", Double.parseDouble(String.format("%.1f", totalStorage)));
        result.put("totalCurrentStorage", Double.parseDouble(String.format("%.1f", totalCurrentStorage)));
        result.put("storageList", storageList);
        return result;
    }
}
