package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.WaterAffairsMapper;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.gragh.vo.request.WaterOperateReqVo;
import com.ygkj.gragh.vo.response.WaterOperateResVo;
import com.ygkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2021-08-06 15:19
 * @description 水厂运行状态统计 农饮水组件接口
 */
//@Component
@Deprecated
public class WaterOperateManager extends AbstractUnitManager {

    @Autowired
    private AttAdcdBaseMapper adcdBaseMapper;
    @Autowired
    private WaterAffairsMapper waterAffairsMapper;


    public WaterOperateManager(ApplicationContext context) {
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
        //查询到对应的统计数据
        WaterOperateReqVo vo = new WaterOperateReqVo();
        List<WaterOperateResVo> waterOperateNum = waterAffairsMapper.getWaterOperateNum(vo);

        // 判断为1 返回为统计内容
        if (reqVo.getDataApi().equals("1")) {
            return getWaterOperateNum(reqVo.getDataParam(), waterOperateNum);
        }
        //否则返回柱状图需求内容
        return getWaterOperateNumByArea(reqVo.getDataParam(), waterOperateNum);

    }


    private Object getWaterOperateNum(String dataParam, List<WaterOperateResVo> waterOperateNum) {

        Map<String, Object> result = new HashMap<>();
        //供水预警
        List<WaterOperateResVo> waterSupplyWarn = waterOperateNum.stream().filter(w -> w.getSupplyStatus() == 0).collect(Collectors.toList());
        //水质预警
        List<WaterOperateResVo> waterQualityWarn = waterOperateNum.stream().filter(w -> w.getIsNormal() == 0).collect(Collectors.toList());
        //水质供水预警
        List<WaterOperateResVo> warnTo = waterOperateNum.stream().filter(w -> w.getSupplyStatus() == 0 && w.getIsNormal() == 0).collect(Collectors.toList());

        Integer totalNum = waterOperateNum.size();
        Integer waterSupplyWarnNum = waterSupplyWarn.size();
        Integer waterQualityWarnNum = waterQualityWarn.size();
        Integer warnToNum = warnTo.size();
        result.put("waterSupplyWarn", waterSupplyWarnNum);
        result.put("waterQualityWarn", waterQualityWarnNum);
        result.put("warnTo", warnToNum);
        result.put("normal", totalNum - waterQualityWarnNum - waterQualityWarnNum + warnToNum);
        // 1 时候统计数量   2 统计人口
        if (dataParam.equals("1")) {
            result.put("total", totalNum);
        } else {
            BigDecimal total = waterOperateNum.stream().filter(t -> t.getDesWasuPop() != null)
                    .map(WaterOperateResVo::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("total", total);
        }
        return result;
    }


    private Object getWaterOperateNumByArea(String dataParam, List<WaterOperateResVo> waterOperateNum) {

        Map<String, List<WaterOperateResVo>> collect = waterOperateNum.stream().filter(a -> a.getAdcd() != null)
                .collect(Collectors.groupingBy(WaterOperateResVo::getAdcd));
        Set<String> adcds = collect.keySet();
        Map<String, Object> cityMap = new HashMap<>(adcds.size());

        for (String adcd : adcds) {
            List<WaterOperateResVo> operateResVos = collect.get(adcd);
            //供水异常
            List<WaterOperateResVo> waterSupply = operateResVos.stream().filter(o -> o.getSupplyStatus() == 0)
                    .collect(Collectors.toList());
            //水质异常
            List<WaterOperateResVo> waterQuality = operateResVos.stream().filter(o -> o.getIsNormal() == 0)
                    .collect(Collectors.toList());
            //两者都异常
            List<WaterOperateResVo> allAbNormal = operateResVos.stream()
                    .filter(o -> o.getSupplyStatus() == 0 && o.getIsNormal() == 0).collect(Collectors.toList());
            Map<String, Object> adcdMap = new HashMap<>(4);

            if (dataParam.equals("1")) {
                //计算对应的数值
                Integer totalNum = operateResVos.size();
                Integer waterSupplyNum = waterSupply.size();
                Integer waterQualityNum = waterQuality.size();
                Integer warnTo = allAbNormal.size();
                Integer normalNum = totalNum - waterQualityNum - waterSupplyNum + warnTo;

                adcdMap.put("waterSupplyWarn", (double) waterSupplyNum / totalNum);
                adcdMap.put("waterQualityWarn", (double) waterQualityNum / totalNum);
                adcdMap.put("warnTo", (double) warnTo / totalNum);
                adcdMap.put("normal", (double) normalNum / totalNum);
            } else {
                //总供应人数
                BigDecimal totalPop = operateResVos.stream().filter(a -> a.getDesWasuPop() != null)
                        .map(WaterOperateResVo::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                //水量异常供应人数
                BigDecimal waterSupplyPop = waterSupply.stream().filter(a -> a.getDesWasuPop() != null)
                        .map(WaterOperateResVo::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                //水质异常供应人数
                BigDecimal waterQualityPop = waterQuality.stream().filter(a -> a.getDesWasuPop() != null)
                        .map(WaterOperateResVo::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
                //异常供应人数
                BigDecimal abNormalPop = allAbNormal.stream().filter(a -> a.getDesWasuPop() != null)
                        .map(WaterOperateResVo::getDesWasuPop).reduce(BigDecimal.ZERO, BigDecimal::add);
//                BigDecimal warnTo =

                //TODO 供应人数计算
                adcdMap.put("waterSupplyPop", (totalPop.doubleValue() - waterSupplyPop.doubleValue() -
                        waterQualityPop.doubleValue() + abNormalPop.doubleValue()) / totalPop.doubleValue());
                adcdMap.put("volumeWarnPop", waterSupplyPop.doubleValue() / totalPop.doubleValue());
                adcdMap.put("qualityWarnPop", waterQualityPop.doubleValue() / totalPop.doubleValue());
                adcdMap.put("abNormalPop", abNormalPop.doubleValue() / totalPop.doubleValue());

            }
            String byAdcd = adcdBaseMapper.selectAdnmByAdcd(adcd);
            cityMap.put(StringUtils.isNotBlank(byAdcd) ? byAdcd : adcd, adcdMap);
        }

        return cityMap;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {

    }
}
