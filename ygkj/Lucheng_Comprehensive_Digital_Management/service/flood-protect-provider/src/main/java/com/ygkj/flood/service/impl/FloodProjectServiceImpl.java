package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.enums.StationType;
import com.ygkj.flood.feign.WaterLevelClient;
import com.ygkj.flood.mapper.FloodProjectMapper;
import com.ygkj.flood.mapper.StationMapper;
import com.ygkj.flood.mapper.WaterAndRainMapper;
import com.ygkj.flood.model.*;
import com.ygkj.flood.service.FloodProjectService;
import com.ygkj.flood.vo.request.ProjectListReqVo;
import com.ygkj.flood.vo.request.StQueryReqVo;
import com.ygkj.flood.vo.response.AllStorageResVo;
import com.ygkj.flood.vo.response.PustListResVo;
import com.ygkj.flood.vo.response.StorageResVo;
import com.ygkj.flood.vo.response.WagaListResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("floodProjectService")
public class FloodProjectServiceImpl implements FloodProjectService {

    private final String rsvrTableName = "st_rsvr_r_";
    private final String wasTableName = "st_was_r_";

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");

    @Resource
    private WaterLevelClient waterLevelClient;

    @Resource
    private FloodProjectMapper floodProjectMapper;

    @Resource
    StationMapper stationMapper;

    @Resource
    WaterAndRainMapper waterAndRainMapper;

    /**
     * 可蓄水量/纳蓄能力统计
     *
     * @param type 1可蓄水量 2纳蓄能力
     * @return
     */
    @Override
    public List<StorageResVo> countStorage(Integer type, Integer resScal) {
        if (resScal == null) {
            resScal = 4;
        }
        //查出大中型水库的总库容和集水面积
        List<StorageResVo> result = floodProjectMapper.listRes(resScal);

        if (!CollectionUtils.isEmpty(result)) {

            Map<String, BigDecimal> currentCatMap = selectCurrentCat(result);

            //计算可蓄水量或者纳蓄能力
            boolean flag = type == 1;
            result.forEach(storageResVo -> {
                Double totCat = storageResVo.getTotCap();
                //现在没有实时库容都默认是0先
                BigDecimal currentCat = currentCatMap.get(storageResVo.getStCode());
                if (totCat != null && currentCat != null) {
                    Double dif = totCat - currentCat.doubleValue();
                    if (flag) {
                        //可蓄水量
                        storageResVo.setAmount(dif);
                    } else {
                        //纳蓄能力
                        Double watShedArea = storageResVo.getWatShedArea();
                        if (watShedArea != null) {
                            storageResVo.setAmount(dif / (watShedArea * 0.1));
                        }
                    }
                }
            });
            // 倒序
            List<StorageResVo> finalResult = result.stream().sorted((o1, o2) -> {
                Double val1 = o1.getAmount() == null ? 0D : o1.getAmount();
                Double val2 = o2.getAmount() == null ? 0D : o2.getAmount();
                return val1.compareTo(val2);
            }).collect(Collectors.toList());
            return finalResult;
        }
        return result;
    }

    /**
     * 查询水库当前库容
     *
     * @param result
     * @return
     */
    private Map<String, BigDecimal> selectCurrentCat(List<StorageResVo> result) {
        //查出最新水位,为了计算实时库容
        Set<String> stCodeSet = result.stream().map(storageResVo -> storageResVo.getStCode()).collect(Collectors.toSet());
        String tableName = rsvrTableName + dtf.format(LocalDate.now());
        List<StorageResVo> waterLevelList = floodProjectMapper.listLatestRsvr(stCodeSet, tableName);
        Map<String, BigDecimal> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(waterLevelList)) {
            List<ReservoirCapacityCurve> list = new ArrayList<>();
            waterLevelList.forEach(storageResVo -> {
                ReservoirCapacityCurve reservoirCapacityCurve = new ReservoirCapacityCurve();
                reservoirCapacityCurve.setCode(storageResVo.getStCode());
                reservoirCapacityCurve.setWaterLevel(new BigDecimal(storageResVo.getRz()));
                list.add(reservoirCapacityCurve);
            });
            map = waterLevelClient.calculationCapacity(list).getData();
        }
        return map;
    }


    /**
     * 根据水位计算库容
     *
     * @param rsvrLevel
     * @return
     */
    private Map<String, BigDecimal> caculateCapactiy(Map<String, Double> rsvrLevel) {
        Map<String, BigDecimal> map = new HashMap<>();
        if (com.ygkj.utils.CollectionUtils.isNotBlank(rsvrLevel)) {
            List<ReservoirCapacityCurve> list = new ArrayList<>();
            rsvrLevel.forEach((key, value) -> {
                ReservoirCapacityCurve reservoirCapacityCurve = new ReservoirCapacityCurve();
                reservoirCapacityCurve.setCode(key);
                reservoirCapacityCurve.setWaterLevel(new BigDecimal(value));
                list.add(reservoirCapacityCurve);
            });
            map = waterLevelClient.calculationCapacity(list).getData();
        }
        return map;
    }

    /**
     * 大中型水库蓄水率
     *
     * @return
     */
    @Override
    public AllStorageResVo countAllStorageRate() {
        AllStorageResVo allStorageResVo = new AllStorageResVo();
        //查出大中型水库的总库容和集水面积
        List<StorageResVo> result = floodProjectMapper.listRes(4);

        Map<String, BigDecimal> currentCatMap = selectCurrentCat(result);

        //算出总蓄水率
        Double totalTotCat = 0d;
        Double totalCurrentCat = 0d;
        for (StorageResVo storageResVo : result) {
            Double totCat = storageResVo.getTotCap();
            if (totCat != null) {
                totalTotCat += totCat;
            }
            BigDecimal currentCat = currentCatMap.get(storageResVo.getStCode());
            if (currentCat != null) {
                totalCurrentCat += currentCat.doubleValue();
            }
        }
        allStorageResVo.setCurrentCap(totalCurrentCat);
        allStorageResVo.setTotCap(totalTotCat);
        allStorageResVo.setResidueCap(totalTotCat - totalCurrentCat);

        if (totalTotCat == 0) {
            allStorageResVo.setRate("0%");
        } else {
            NumberFormat num = NumberFormat.getPercentInstance();
            num.setMaximumFractionDigits(1);
            allStorageResVo.setRate(num.format(totalCurrentCat / totalTotCat));
        }

        return allStorageResVo;
    }

    /**
     * 水闸列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<WagaListResVo> listWaga(ProjectListReqVo reqVo) {
        //根据筛选条件获取水闸列表
        List<WagaListResVo> result = floodProjectMapper.listWaga(reqVo);
        if (!CollectionUtils.isEmpty(result)) {
            Set<String> set = result.stream().map(wagaListResVo -> wagaListResVo.getStCode()).collect(Collectors.toSet());
            String tableName = wasTableName + dtf.format(LocalDate.now());
            //最新水位
            List<WagaListResVo> waterList = floodProjectMapper.listLatestWas(tableName, set);
            if (!CollectionUtils.isEmpty(waterList)) {
                Map<String, WagaListResVo> map = waterList.stream().collect(Collectors.toMap(WagaListResVo::getStCode, Function.identity()));
                result.forEach(wagaListResVo -> {
                    String stCode = wagaListResVo.getStCode();
                    if (map.containsKey(stCode)) {
                        WagaListResVo st = map.get(stCode);

                        //加上一些实时信息
                        wagaListResVo.setTm(st.getTm());
                        wagaListResVo.setUpz(st.getUpz());
                        wagaListResVo.setDwz(st.getDwz());
                        wagaListResVo.setMxgtq(st.getMxgtq());
                        wagaListResVo.setTgtq(st.getTgtq());
                        wagaListResVo.setOvs(st.getOvs());
                    }
                });
            }
        }
        return result;
    }

    /**
     * 水闸列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<PustListResVo> listPust(ProjectListReqVo reqVo) {
        List<PustListResVo> result = floodProjectMapper.listPust(reqVo);
        return result;
    }

    @Override
    public PageVO<AttWagaBase> listWagaProj(Integer pageSize, Integer pageNum) {
        if (pageNum != null && pageNum > 0) {

        } else {
            pageNum = 1;
        }
        if (pageSize != null && pageSize > 0) {

        } else {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        return PageUtils.getPageVO(floodProjectMapper.listWagaProj());
    }

    @Override
    public PageVO<AttPustBase> listPustProj(Integer pageSize, Integer pageNum, String pustCode) {
        if (pageNum != null && pageNum > 0) {

        } else {
            pageNum = 1;
        }
        if (pageSize != null && pageSize > 0) {

        } else {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        return PageUtils.getPageVO(floodProjectMapper.listPustProj(pustCode));
    }

    @Override
    public List<AttResBase> listRsvrStorageWaterLevel() {
        // 查询所有工程
        List<AttResBase> rsvrList = this.floodProjectMapper.listRsvrProj();
        rsvrList = rsvrList == null ? Collections.emptyList() : rsvrList;

        // 工程编码
        String projCode = rsvrList.stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getResCode()))
                .map(AttResBase::getResCode)
                .collect(Collectors.joining(","));

        // 工程关联的所有测站
        List<StBase> stList = this.stationMapper.loadStByCondtion(Builder.of(StQueryReqVo::new)
                .with(StQueryReqVo::setProjCodes, projCode)
                .build());
        stList = stList == null ? Collections.emptyList() : stList;

        // 工程和测站对应关系
        Map<String, List<String>> projStMap = stList.stream()
                .collect(Collectors.groupingBy(StBase::getProjCode,
                        Collectors.mapping(StBase::getStCode,
                                Collectors.toList())));
        // 所有的测站编码
        List<String> stcds = stList.stream()
                .filter(o -> o != null && StringUtils.isNotBlank(o.getStCode()))
                .map(StBase::getStCode)
                .collect(Collectors.toList());
        // 根据测站查实时水位
        LocalDateTime nowLdt = LocalDateTime.now();
        List<StRsvrR> nowCapcity = waterAndRainMapper.selectNewestRsvrWaterLevel(Arrays.asList(StationType.RESERVOIR.table() + nowLdt.format(dtf),
                StationType.RESERVOIR.table() + nowLdt.minusHours(1).format(dtf))
                .stream().distinct().collect(Collectors.toList()), stcds);
        // 计算实时库容
        Map<String, Double> nowLevelMap = nowCapcity.stream().collect(Collectors.toMap(StRsvrR::getStcd, StRsvrR::getRz));
        Map<String, BigDecimal> nowCapacityMap = caculateCapactiy(nowLevelMap);

        for (AttResBase rsvr : rsvrList) {
            List<String> stcdOfRsvr = projStMap.getOrDefault(rsvr.getResCode(), Collections.emptyList());
            if (com.ygkj.utils.CollectionUtils.isNotBlank(stcdOfRsvr)) {
                // 取水库所有测站的库容和水位
                List<BigDecimal> tempCapacities = new ArrayList<>(stcdOfRsvr.size());
                List<Double> tempWaterLevels = new ArrayList<>(stcdOfRsvr.size());
                for (String stcd : stcdOfRsvr) {
                    tempCapacities.add(nowCapacityMap.getOrDefault(stcd, BigDecimal.ZERO));
                    tempWaterLevels.add(nowLevelMap.getOrDefault(stcd, 0D));
                }
                // 取水库所有测站中值最大的那个水位和库容
                Optional<BigDecimal> nowCapacity = tempCapacities.stream().filter(Objects::nonNull).max(BigDecimal::compareTo);
                Optional<Double> nowWaterLevel = tempWaterLevels.stream().filter(Objects::nonNull).max(Double::compareTo);
                if (nowCapacity.isPresent() && rsvr.getTotCap() != null) {
                    // 剩余库容
                    BigDecimal leftCapacity = rsvr.getTotCap().subtract(nowCapacity.get());
                    rsvr.setLeftCapacity(leftCapacity);
                    // 计算纳蓄能力
                    BigDecimal watShedArea = rsvr.getWatShedArea();
                    if (watShedArea != null) {
                        // 纳蓄能力=剩余库容/(集雨面积*0.1)
                        rsvr.setStorageAbility(leftCapacity.doubleValue() / (watShedArea.doubleValue() * 0.1));
                    }
                }
                if (nowWaterLevel.isPresent()) {
                    rsvr.setWaterLevel(nowWaterLevel.get());
                }
            }
        }
        return rsvrList;
    }
}
