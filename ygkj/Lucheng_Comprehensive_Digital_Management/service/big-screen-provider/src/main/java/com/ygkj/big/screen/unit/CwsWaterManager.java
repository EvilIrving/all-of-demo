package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.WaterAffairsMapper;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.gragh.model.AttCwsBase;
import com.ygkj.project.model.AttCwsBaseWcp;
import com.ygkj.utils.Builder;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2021-08-05 17:27
 * @description 农饮水组件
 */
@Component
public class CwsWaterManager extends MemStackCachedUnitManager {


    @Autowired
    private AttAdcdBaseMapper adcdBaseMapper;
    @Autowired
    private WaterAffairsMapper waterAffairsMapper;

    @Autowired
    private ProjectManageClient projectManageClient;

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
//        //直接获取所有对应的农饮水工程
//        List<AttCwsBase> allCwsBase = waterAffairsMapper.getAllCwsBase();
//
//        // 判断为1 返回为统计内容
//        if (reqVo.getDataApi().equals("1")){
//            return getCwsWaterSupplyNum(reqVo.getStrategy(),reqVo.getDataParam(),allCwsBase);
//        }
//        //否则返回柱状图需求内容
//        return getCwsWaterSupplyNumByArea(reqVo.getStrategy(),reqVo.getDataParam(), allCwsBase);

        switch (reqVo.getStrategy()) {
            case "waterSupplyStats": {
                Integer type = reqVo.getParam("type", Integer.class);
                Object result = null;
                if (cache.get(buildDefaultCacheKey("waterSupplyStats", type)) == null) {
                    result = waterSupplyStats(type);
                }
                return result;
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }

    /**
     * 水厂统计
     *
     * @param type 1. 根据行政区划统计：城市、乡镇、联村、单村;
     *             2. 根据水厂规模统计：两百吨千人以下、两百吨千人以上、千吨万人、城市水厂；
     *             3. 根据水厂类型统计：城市、乡镇、联村、单村；
     *             4. 按行政区划统计: 正常供水，供水预警，水质预警，水质供水预警
     *             5. 按水厂的运行状态统计:正常供水，供水预警，水质预警，水质供水预警
     *             6. 统计日累计供水量，异常供水次数
     */
    public CommonResult waterSupplyStats(int type) {
        return projectManageClient.waterWorksStats(type);
    }


    /**
     * 获取柱状图统计内容  按行政区划拆分
     *
     * @param type
     * @param attCwsBases
     * @return
     */
    private Object getCwsWaterSupplyNumByArea(String type, String accord, List<AttCwsBase> attCwsBases) {

        Map<String, List<AttCwsBase>> adcdCollect = attCwsBases.stream().filter(a -> a.getCitycode() != null)
                .collect(Collectors.groupingBy(AttCwsBase::getCitycode));
        Set<String> cityCodes = adcdCollect.keySet();

        Map<String, Object> cityMap = new HashMap<>(cityCodes.size());
        // 1 按规模  2按类型
        if (type.equals("1")) {
            for (String cityCode : cityCodes) {
                List<AttCwsBase> bases = adcdCollect.get(cityCode);
                Map<String, List<AttCwsBase>> collect = bases.stream().filter(b -> b.getWasuRang() != null)
                        .collect(Collectors.groupingBy(AttCwsBase::getWasuRang));
                Set<String> wasuRangs = collect.keySet();
                Map<String, Object> wasuMap = new HashMap<>();
                for (String wasuRang : wasuRangs) {
                    List<AttCwsBase> cwsBases = collect.get(wasuRang);
                    if (accord.equals("1")) {
                        wasuMap.put(wasuRang, cwsBases.size());
                    } else {
                        BigDecimal total = cwsBases.stream().filter(a -> a.getDesWasuPop() != null)
                                .map(AttCwsBase::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                        wasuMap.put(wasuRang, total);
                    }
                }
                //将对应城市以及 对应的按规模集合
                cityMap.put(adcdBaseMapper.selectAdnmByAdcd(cityCode), wasuMap);
            }
        } else {
            for (String cityCode : cityCodes) {
                List<AttCwsBase> bases = adcdCollect.get(cityCode);
                Map<String, List<AttCwsBase>> collect = bases.stream().filter(b -> b.getEngType() != null)
                        .collect(Collectors.groupingBy(AttCwsBase::getEngType));
                Set<String> engTypes = collect.keySet();
                Map<String, Object> wasuMap = new HashMap<>();
                for (String engType : engTypes) {
                    List<AttCwsBase> cwsBases = collect.get(engType);
                    if (accord.equals("1")) {
                        wasuMap.put(engType, cwsBases.size());
                    } else {
                        BigDecimal total = cwsBases.stream().filter(a -> a.getDesWasuPop() != null)
                                .map(AttCwsBase::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                        wasuMap.put(engType, total);
                    }
                }
                //将对应城市以及 对应的按规模集合
                cityMap.put(adcdBaseMapper.selectAdnmByAdcd(cityCode), wasuMap);
            }
        }
        return cityMap;
    }


    /**
     * 根据类别统计对应的数据
     *
     * @param type       1 按规模  2按类型
     * @param accord     1提供数量 2提供人口
     * @param allCwsBase 提供的统计数据
     * @return
     */
    private Object getCwsWaterSupplyNum(String type, String accord, List<AttCwsBase> allCwsBase) {

        Map<String, Object> result = new HashMap<>(4);

        // 1 按规模  2按类型
        if (type.equals("1")) {
            //按照规模字段提取对应列表
            Map<String, List<AttCwsBase>> collect = allCwsBase.stream().filter(a -> a.getWasuRang() != null)
                    .collect(Collectors.groupingBy(AttCwsBase::getWasuRang));
            Set<String> wasuRangs = collect.keySet();
            for (String wasuRang : wasuRangs) {
                List<AttCwsBase> cwsBases = collect.get(wasuRang);
                //判断为1 按照数量统计 否则统计人口值
                if (accord.equals("1")) {
                    result.put(wasuRang, cwsBases.size());
                } else {
                    BigDecimal total = cwsBases.stream().filter(t -> t.getDesWasuPop() != null)
                            .map(AttCwsBase::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                    result.put(wasuRang, total);
                }
            }
        } else {
            //按照类型字段提取
            Map<String, List<AttCwsBase>> collect = allCwsBase.stream().filter(a -> a.getEngType() != null)
                    .collect(Collectors.groupingBy(AttCwsBase::getEngType));
            Set<String> engTypes = collect.keySet();
            for (String engType : engTypes) {
                List<AttCwsBase> cwsBases = collect.get(engType);
                if (accord.equals("1")) {
                    result.put(engType, cwsBases.size());
                } else {
                    BigDecimal total = cwsBases.stream().filter(t -> t.getDesWasuPop() != null)
                            .map(AttCwsBase::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                    result.put(engType, total);
                }
                result.put(engType, cwsBases.size());
            }
        }
        return result;
    }

}
