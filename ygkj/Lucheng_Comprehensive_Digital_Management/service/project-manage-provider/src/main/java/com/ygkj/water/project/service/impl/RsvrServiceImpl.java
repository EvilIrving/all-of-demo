package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.enums.EngScalEnum;
import com.ygkj.enums.StationType;
import com.ygkj.flood.model.ReservoirCapacityCurve;
import com.ygkj.gragh.model.AttResBase;
import com.ygkj.gragh.model.AttStBase;
import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.project.model.RsvrFloodPredict;
import com.ygkj.project.vo.request.ProjectManageQueryVo;
import com.ygkj.project.vo.request.RsvrReplayReqVo;
import com.ygkj.project.vo.request.RsvrStationWaterReqVo;
import com.ygkj.project.vo.request.RsvrWatSrcQueryVo;
import com.ygkj.project.vo.response.RsvrReplayResVo;
import com.ygkj.project.vo.response.RsvrStationWaterResVo;
import com.ygkj.project.vo.response.WatSrcResVo;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.CommonServiceClient;
import com.ygkj.water.project.mapper.ProjectMapper;
import com.ygkj.water.project.mapper.StationMapper;
import com.ygkj.water.project.mapper.WaterAndRainMapper;
import com.ygkj.water.project.service.AdcdBaseService;
import com.ygkj.water.project.service.RsvrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/8/11
 */
@Service
public class RsvrServiceImpl implements RsvrService, InitializingBean {

    @Resource
    ProjectMapper projectMapper;

    @Autowired
    private CommonServiceClient commonServiceClient;

    @Resource
    private AdcdBaseService adcdBaseService;

    @Resource
    private StationMapper stationMapper;

    @Resource
    private WaterAndRainMapper waterAndRainMapper;

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
    /**
     * 永嘉测站前缀
     */
    private final static String yjStcdPrefix = "YJ_";
    private final static String yjRsvrTable = "st_rsvr_r_yongjia";

    private final static String damType1Key = "一类";
    private final static String damType2Key = "二类";

    @Value("${dict.meiLimitRange}")
    private String meiLimitRange;

    @Value("${dict.typhoonLimitRange}")
    private String typhoonLimitRange;

    private LocalDate meiLimitStart;
    private LocalDate meiLimitEnd;
    private LocalDate typhoonLimitStart;
    private LocalDate typhoonLimitEnd;

    @Override
    public void afterPropertiesSet() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Assert.isTrue(StringUtils.isNotBlank(meiLimitRange), "梅汛区间不能为空");
        Assert.isTrue(StringUtils.isNotBlank(typhoonLimitRange), "台汛区间不能为空");
        LocalDate now = LocalDate.now();
        String[] meiRanges = meiLimitRange.split(com.ygkj.utils.StringUtils.JOIN_DELIMITER);
        try {
            meiLimitStart = LocalDate.parse(now.getYear() + "-" + meiRanges[0], dtf).withYear(now.getYear());
            meiLimitEnd = LocalDate.parse(now.getYear() + "-" + meiRanges[1], dtf).withYear(now.getYear());
        } catch (Exception e) {
            Assert.isTrue(true, "梅汛区间解析错误");
        }
        String[] typhoonRanges = typhoonLimitRange.split(com.ygkj.utils.StringUtils.JOIN_DELIMITER);
        try {
            typhoonLimitStart = LocalDate.parse(now.getYear() + "-" + typhoonRanges[0], dtf).withYear(now.getYear());
            typhoonLimitEnd = LocalDate.parse(now.getYear() + "-" + typhoonRanges[1], dtf).withYear(now.getYear());
        } catch (Exception e) {
            Assert.isTrue(true, "台汛区间解析错误");
        }
    }

    /**
     * 列出安全鉴定结论
     *
     * @return
     */
    @Override
    public List<String> listSafetyConclusion() {
        return projectMapper.rsvrSafetyConclusion();
    }

    @Override
    public List<RsvrStationWaterResVo> rsvrNewestWaterLevel(RsvrStationWaterReqVo reqVo) {
        return rsvrWaterLevel(reqVo).getList();
    }

    /**
     * 水库/大中型组件详表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<RsvrStationWaterResVo> rsvrUnitList(RsvrStationWaterReqVo reqVo) {
        if (reqVo == null) {
            reqVo = new RsvrStationWaterReqVo();
        }
        reqVo.setFloodStorageAbility(true);
        reqVo.setLimitStorageAbility(true);
        reqVo.setStorageRatio(true);
        // 大中型、小型
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            if ("大中型".equals(reqVo.getEngScales())) {
                reqVo.setEngScales(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()));
            } else if ("小型".equals(reqVo.getEngScales())) {
                reqVo.setEngScales(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.S1.getDesc(), EngScalEnum.S2.getDesc()));
            }
        }
        PageVO<RsvrStationWaterResVo> result = rsvrWaterLevel(reqVo);
        List<RsvrStationWaterResVo> data = result.getList();

        RsvrStationWaterReqVo finalReqVo = reqVo;
        if (finalReqVo.getDrainage24H() == null) {
            finalReqVo.setDrainage24H(false);
        }
        if (finalReqVo.getDrainage48H() == null) {
            finalReqVo.setDrainage48H(false);
        }
        if (finalReqVo.getDrainage72H() == null) {
            finalReqVo.setDrainage72H(false);
        }
        if (finalReqVo.getDrainage24H() || finalReqVo.getDrainage48H() || finalReqVo.getDrainage72H()) {
            data.forEach(o -> {
                if (finalReqVo.getDrainage24H() && o.getDrainage24H() != null) {
                    o.setDrainage24H(Double.parseDouble(String.format("%.1f", o.getDrainage24H())));
                }
                if (finalReqVo.getDrainage48H() && o.getDrainage48H() != null) {
                    o.setDrainage48H(Double.parseDouble(String.format("%.1f", o.getDrainage48H())));
                }
                if (finalReqVo.getDrainage72H() && o.getDrainage72H() != null) {
                    o.setDrainage72H(Double.parseDouble(String.format("%.1f", o.getDrainage72H())));
                }
            });
        }
//        List<RsvrStationWaterResVo> tempList = data.stream().filter(Objects::nonNull).peek(o -> Optional.ofNullable(EngScalEnum.getByLevel(o.getEngScal()))
//                .ifPresent(o1 -> o.setEngScal(o1.getDesc())))
//                .collect(Collectors.toList());
//        // 默认情况下按超汛限值降序
//        if (StringUtils.isBlank(reqVo.getOrderColumn())) {
//            tempList = tempList.stream().sorted(((o1, o2) -> {
//                if (o1.getOverLimitVal() != null && o2.getOverLimitVal() != null) {
//                    return o2.getOverLimitVal().compareTo(o1.getOverLimitVal());
//                } else if (o1.getOverLimitVal() != null && o2.getOverLimitVal() == null) {
//                    return -1;
//                } else if (o1.getOverLimitVal() == null && o2.getOverLimitVal() != null) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            })).collect(Collectors.toList());
//        }
        return result;
    }

    /**
     * 水库/大中型组件详表统计
     *
     * @param reqVo
     * @return
     */
    @Override
    public JSONObject stats4RsvrUnitList(RsvrStationWaterReqVo reqVo) {
        // 统计不要分页
        reqVo.noPage();

        // 一类坝数量
        long damType1 = 0L;
        // 二类坝数量
        long damType2 = 0L;

        JSONObject result = new JSONObject();
        // 列表
        result.put("list", Collections.emptyList());
        // 平均蓄水率
        result.put("avgStorageRatio", 0D);
        // 平均至汛限纳蓄水能力
        result.put("avgLimitStorageAbility", 0D);
        // 超汛限水库数量
        result.put("overLimitNum", 0);
        // 水库总数
        result.put("total", 0);
        // 大中型、小型
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            if ("大中型".equals(reqVo.getEngScales())) {
                reqVo.setEngScales(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.L1.getDesc(), EngScalEnum.L2.getDesc(), EngScalEnum.MID.getDesc()));
            } else if ("小型".equals(reqVo.getEngScales())) {
                reqVo.setEngScales(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, EngScalEnum.S1.getDesc(), EngScalEnum.S2.getDesc()));
            }
        }
        List<RsvrStationWaterResVo> data = rsvrUnitList(reqVo).getList();
        data = data == null ? Collections.emptyList() : data;
//        damType1 = data.stream().filter(o -> StringUtils.isNotBlank(o.getSafetyConclusion()) && o.getSafetyConclusion().contains(damType1Key)).count();
//        damType2 = data.stream().filter(o -> StringUtils.isNotBlank(o.getSafetyConclusion()) && o.getSafetyConclusion().contains(damType2Key)).count();
//            OptionalDouble avgStorageRatio = data.stream().filter(o -> o.getStorageRatio() != null).mapToDouble(RsvrStationWaterResVo::getStorageRatio).average();
        OptionalDouble avgLimitStorageAbility = data.stream().filter(o -> o.getLimitStorageAbility() != null).mapToDouble(RsvrStationWaterResVo::getLimitStorageAbility).average();
        long overLimitNum = data.stream().filter(o -> (o.getOverLimitVal() != null && o.getOverLimitVal() > 0)).count();
//            if (avgStorageRatio.isPresent()) {
//                result.put("avgStorageRatio", Double.parseDouble(String.format("%.1f", avgStorageRatio.getAsDouble())));
//            }
        // 平均蓄水率
        double totalStorage = data.stream().filter(o -> o.getLimitLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getLimitLevelCapacity).sum();
        double totalCurrentStorage = data.stream().filter(o -> o.getWaterLevelCapacity() != null).mapToDouble(RsvrStationWaterResVo::getWaterLevelCapacity).sum();
        if (totalStorage > 0) {
            result.put("avgStorageRatio", Double.parseDouble(String.format("%.1f", totalCurrentStorage * 100 / totalStorage)));
        }
        if (avgLimitStorageAbility.isPresent()) {
            result.put("avgLimitStorageAbility", Double.parseDouble(String.format("%.1f", avgLimitStorageAbility.getAsDouble())));
        }
        result.put("overLimitNum", overLimitNum);
        result.put("total", data.size());
        result.put("list",data);
//        result.put("damType1", damType1);
//        result.put("damType2", damType2);
        return result;
    }

    @Override
    public PageVO rsvrWaterLevel(RsvrStationWaterReqVo reqVo) {
        // 设置查询条件
        ProjectManageQueryVo queryVo = new ProjectManageQueryVo();
        if (StringUtils.isNotBlank(reqVo.getResCodes())) {
            queryVo.setCodes(Arrays.asList(reqVo.getResCodes().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            queryVo.setLevels(Arrays.stream(reqVo.getEngScales().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER))
                    .map(o -> Optional.ofNullable(EngScalEnum.getByDesc(o)))
                    .filter(Optional::isPresent)
                    .map(o -> o.get().getEngScal())
                    .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        queryVo.setBas(reqVo.getResBas());
        queryVo.setAreaCode(reqVo.getResAdcd());
        queryVo.setProjectName(reqVo.getResName());
        queryVo.setSafetyConclusion(reqVo.getSafetyConclusion());

        queryVo.setOrderSql(adcdBaseService.adcdOrderSql("area_code", "res_name", true));
        if (reqVo.isPaged()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }

        // 查询所有的水库
        PageVO rsvrPageVo = PageUtils.getPageVO(this.projectMapper.selectReservoirByCondition(queryVo));

        List<AttResBase> rsvrList = rsvrPageVo.getList();
        if (org.springframework.util.CollectionUtils.isEmpty(rsvrList)) {
            return rsvrPageVo;
        }
        List<RsvrStationWaterResVo> result = fillWater2Rsvr(rsvrList, reqVo);
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<RsvrStationWaterResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            result = result.stream().sorted(comparator).collect(Collectors.toList());
        }
        rsvrPageVo.setList(result);
        return rsvrPageVo;
    }

    /**
     * 为水库填充水位信息
     *
     * @param rsvrList
     * @param reqVo
     * @return
     */
    @Override
    public List<RsvrStationWaterResVo> fillWater2Rsvr(List<AttResBase> rsvrList, RsvrStationWaterReqVo reqVo) {

        // 填充行政区划编码并组成返回值
        List<RsvrStationWaterResVo> result = new ArrayList<>(rsvrList.size());

        List<String> adcdList = rsvrList.stream().filter(o -> StringUtils.isNotBlank(o.getAreaCode())).map(AttResBase::getAreaCode).distinct().collect(Collectors.toList());
        Map<String, String> adcdMap = adcdBaseService.selectAdnmByAdcd(adcdList);

//        LocalDate now = LocalDate.now();
        LocalDate now = reqVo.getDataTime() == null ? LocalDate.now() : DateUtils.date2LocalDate(reqVo.getDataTime());
        // 0 为正常蓄水水位 1为梅汛限水位  2为台汛限水位
        int limitStatus = 0;
        if (!now.isBefore(meiLimitStart.withYear(now.getYear())) && !now.isAfter(meiLimitEnd.withYear(now.getYear()))) {
            limitStatus = 1;
        } else if (!now.isBefore(typhoonLimitStart.withYear(now.getYear())) && !now.isAfter(typhoonLimitEnd.withYear(now.getYear()))) {
            limitStatus = 2;
        }
        for (AttResBase attResBase : rsvrList) {
            RsvrStationWaterResVo temp = new RsvrStationWaterResVo();
            BeanUtils.copyProperties(attResBase, temp);
            if (StringUtils.isNotBlank(temp.getAreaCode())) {
                temp.setAreaName(adcdMap.get(temp.getAreaCode()));
            }
            EngScalEnum engScalEnum = EngScalEnum.getByLevel(attResBase.getEngScal());
            if (engScalEnum != null) {
                temp.setEngScal(engScalEnum.getDesc());
                if (engScalEnum.getEngScalAsInt() < 4 && engScalEnum.getEngScalAsInt() > 0) {
                    switch (limitStatus) {
                        case 1:
                            temp.setCurrentLimitLevel(attResBase.getMeiFloodLimitWaterLevel());
                            break;
                        case 2:
                            temp.setCurrentLimitLevel(attResBase.getYphoonFloodLlimitWaterLevel());
                            break;
                        default:
                            temp.setCurrentLimitLevel(attResBase.getNormWatLev());
                            break;
                    }
                } else {
                    // 小型水库判断超汛限根据溢洪道高程
//                    temp.setCurrentLimitLevel(attResBase.getNormalSpillwayCrestElevation());
//                    if (limitStatus != 0) {
//                        temp.setSmallRsvrCurLimitLevel(attResBase.getFlLowLimLev());
//                    } else {
//                        temp.setSmallRsvrCurLimitLevel(attResBase.getNormWatLev());
//                    }

//                    2021年7月21日，上面决定，小型水库  用于判断超讯的水位  和  当前汛限水位  为  正常蓄水水位   ， 且不分是否为汛期
                    temp.setSmallRsvrCurLimitLevel(attResBase.getNormWatLev());
                    temp.setCurrentLimitLevel(attResBase.getNormWatLev());
                }
            }
            result.add(temp);
        }

        rsvrList = null; // help gc

        computeRsvrWaterLevel(result, reqVo.getDataTime());
        computeRsvrOverLimitInfo(result);
        computeRsvrExternalInfo(result, reqVo);
        return result;
    }

    public void computeRsvrWaterLevel(List<RsvrStationWaterResVo> result, Date dataTime) {
        dataTime = dataTime == null ? new Date() : dataTime;
        Map<String, AttStBase> waterLevelMap = getRsvrWaterLevelWithStation(result, dataTime);

        for (RsvrStationWaterResVo temp : result) {
            AttStBase stBase = waterLevelMap.get(temp.getResCode());
            if (stBase == null) {
                continue;
            }
            // 设置测站信息
            temp.setStCode(stBase.getStCode());
            temp.setStType(stBase.getStType());
            temp.setStLat(stBase.getStLat());
            temp.setStLong(stBase.getStLong());
            temp.setStName(stBase.getStName());

            // 由于waterLevel 为值类型，只能用Double.MIN_VALUE 表示无实时水位
            if (stBase.getWaterLevel() == Double.MIN_VALUE) {
                continue;
            }
            // 设置实时水位
            temp.setWaterLevel(stBase.getWaterLevel());
            temp.setDataTime(stBase.getTime());

        }
    }

    /**
     * 计算水库的超汛限信息
     *
     * @param rsvrList
     */
    @Override
    public void computeRsvrOverLimitInfo(List<RsvrStationWaterResVo> rsvrList) {
        for (RsvrStationWaterResVo temp : rsvrList) {
            if (temp.getWaterLevel() == null) {
                continue;
            }
            // 超汛限
            if (temp.getCurrentLimitLevel() != null && temp.getWaterLevel() > temp.getCurrentLimitLevel().doubleValue()) {
                temp.setOverLimitVal(Double.parseDouble(String.format("%.2f", temp.getWaterLevel() - temp.getCurrentLimitLevel().doubleValue())));
            }

            // 超防洪高
            if (temp.getUppLevFlco() != null && temp.getWaterLevel() > temp.getUppLevFlco().doubleValue()) {
                temp.setOverFloodVal(Double.parseDouble(String.format("%.2f", temp.getWaterLevel() - temp.getUppLevFlco().doubleValue())));
            }
        }
    }

    @Override
    public void computeRsvrExternalInfo(List<RsvrStationWaterResVo> rsvrList, RsvrStationWaterReqVo reqVo) {
        if (reqVo.isWaterLevelStorage() || reqVo.isStorageRatio() || reqVo.isStorageAbility() || reqVo.isLimitStorageAbility() || reqVo.isFloodStorageAbility()
//                || reqVo.isDrainage()
                || reqVo.isDrainage24H() || reqVo.isDrainage48H() || reqVo.isDrainage72H()
                || reqVo.isCompareHisAvgCapStat()) {
            setCapacity4Rsvr(rsvrList, o -> BigDecimal.valueOf(o.getWaterLevel()), RsvrStationWaterResVo::setWaterLevelCapacity);
        }
        // 计算蓄水率
        if (reqVo.isStorageRatio()) {
//            computeStorageRatio(rsvrList);
        }
        // 计算至汛限纳蓄能力
        if (reqVo.isLimitStorageAbility()) {
            computeLimitStorageAbility(rsvrList);
        }
        // 计算防洪高纳蓄水能力
        if (reqVo.isFloodStorageAbility()) {
            computeFloodStorageAbility(rsvrList);
        }
        // 计算正常水位纳蓄水能力
        if (reqVo.isNormStorageAbility()) {
            computeNormStorageAbility(rsvrList);
        }
        // 计算历史平均水位蓄水量
        if (reqVo.isHisAvgCapacity() || reqVo.isCompareHisAvgCapStat()) {
            setCapacity4Rsvr(rsvrList, RsvrStationWaterResVo::getHisAvgLevel, RsvrStationWaterResVo::setHisAvgCapacity);
        }
        if (reqVo.isCompareHisAvgCapStat()) {
            for (RsvrStationWaterResVo rsvrStationWaterResVo : rsvrList) {
                rsvrStationWaterResVo.doCompareHisAvgCapStat();
            }
        }
//        // 计算泄流量
//        if (reqVo.isDrainage()) rsvrStorageStats{
//            computeRsvrDrainage(rsvrList, reqVo.getDrainageMinutes(), reqVo.getDataTime());
//        }
        // 计算24小时泄流量
        if (reqVo.isDrainage24H()) {
            Date endTime = reqVo.getDataTime() == null ? new Date() : reqVo.getDataTime();
            Date startTime = new Date(endTime.getTime() - 24 * 3600 * 1000);
            computeRsvrDrainageV3(rsvrList, startTime, endTime, RsvrStationWaterResVo::setDrainage24H);
        }
        // 计算泄流量
        if (reqVo.isDrainage48H()) {
            Date endTime = reqVo.getDataTime() == null ? new Date() : reqVo.getDataTime();
            Date startTime = new Date(endTime.getTime() - 48 * 3600 * 1000);
            computeRsvrDrainageV3(rsvrList, startTime, endTime, RsvrStationWaterResVo::setDrainage48H);
        }
        // 计算泄流量
        if (reqVo.isDrainage72H()) {
            Date endTime = reqVo.getDataTime() == null ? new Date() : reqVo.getDataTime();
            Date startTime = new Date(endTime.getTime() - 72 * 3600 * 1000);
            computeRsvrDrainageV3(rsvrList, startTime, endTime, RsvrStationWaterResVo::setDrainage72H);
        }
    }

    /**
     * 查询水库水位
     * 约定waterLevel=Double.MIN_VALUE 为无水位数据
     *
     * @param rsvrList 水库列表
     * @param dataTime 复盘时刻
     * @return
     */
    @Override
    public Map<String, AttStBase> getRsvrWaterLevelWithStation(List<? extends AttResBase> rsvrList, Date dataTime) {
        if (org.springframework.util.CollectionUtils.isEmpty(rsvrList)) {
            return Collections.emptyMap();
        }
        List<String> mainStcdList = rsvrList.stream().map(AttResBase::getMainStcd).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (org.springframework.util.CollectionUtils.isEmpty(mainStcdList)) {
            return Collections.emptyMap();
        }
        Map<String, AttStBase> stationMap = getRsvrWaterLevelByMainStcd(mainStcdList, dataTime);
        Map<String, AttStBase> resultMap = new HashMap<>(rsvrList.size());

        // 将水库编码与测站信息关联起来
        for (AttResBase rsvr : rsvrList) {
            if (StringUtils.isBlank(rsvr.getMainStcd())) {
                continue;
            }
            AttStBase stBase = stationMap.get(rsvr.getMainStcd());
            if (stBase == null) {
                stBase = Builder.of(AttStBase::new).with(AttStBase::setStCode, rsvr.getMainStcd()).build();
            }
            resultMap.put(rsvr.getResCode(), stBase);
        }
        return resultMap;
    }

    /**
     * 查询水库水位
     * 约定waterLevel=Double.MIN_VALUE 为无水位数据
     *
     * @param mainStcdList 水库主测站列表
     * @param dataTime     复盘时刻
     * @return
     */
    @Override
    public Map<String, AttStBase> getRsvrWaterLevelByMainStcd(List<String> mainStcdList, Date dataTime) {
        if (org.springframework.util.CollectionUtils.isEmpty(mainStcdList)) {
            return Collections.emptyMap();
        }
        // 根据工程编码查询所有的测站
        List<AttStBase> stationList = this.stationMapper.candidateStation(String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, mainStcdList));
        stationList = stationList == null ? Collections.emptyList() : stationList;
        Map<String, AttStBase> stationMap = stationList.stream().collect(Collectors.toMap(AttStBase::getStCode, o -> o));

        Map<String, StRsvrR> allWaterLevelMap = getRsvrWaterLevelByStcd(mainStcdList, dataTime);
        Map<String, AttStBase> resultMap = new HashMap<>(mainStcdList.size());
        for (String mainStcd : mainStcdList) {
            AttStBase stBase = stationMap.get(mainStcd);
            if (stBase == null) {
                stBase = Builder.of(AttStBase::new).with(AttStBase::setStCode, mainStcd).build();
            }
            StRsvrR tempWaterLevel = allWaterLevelMap.get(mainStcd);
            if (tempWaterLevel == null) {
                // waterLevel 是值类型，约定waterLevel=Double.MIN_VALUE 为无水位数据。
                stBase.setWaterLevel(Double.MIN_VALUE);
            } else {
                // waterLevel 是值类型，约定waterLevel=Double.MIN_VALUE 为无水位数据。
                stBase.setWaterLevel(tempWaterLevel.getRz() == null ? Double.MIN_VALUE : tempWaterLevel.getRz());
                stBase.setTime(tempWaterLevel.getTm());
            }
            resultMap.put(mainStcd, stBase);
        }
        return resultMap;
    }

    /**
     * @param stcdList 水库主测站列表
     * @param dataTime 数据时刻
     * @return
     */
    @Override
    public Map<String, StRsvrR> getRsvrWaterLevelByStcd(List<String> stcdList, Date dataTime) {
        if (org.springframework.util.CollectionUtils.isEmpty(stcdList)) {
            return Collections.emptyMap();
        }
        List<String> stcds = stcdList.stream().filter(o -> !o.startsWith(yjStcdPrefix)).collect(Collectors.toList());
        List<String> yjstcds = stcdList.stream().filter(o -> o.startsWith(yjStcdPrefix)).collect(Collectors.toList());
        List<StRsvrR> waterLevel = Collections.emptyList();
        List<StRsvrR> yjwaterLevel = Collections.emptyList();
        if (dataTime == null) {
            // 查询最新的水位
            LocalDateTime nowLdt = LocalDateTime.now();
            if (!org.springframework.util.CollectionUtils.isEmpty(stcds)) {
                waterLevel = waterAndRainMapper.candidateReservoirWaterLevel(StationType.RESERVOIR.table() + nowLdt.format(yyyyMM), stcds, null);
            }
            if (!org.springframework.util.CollectionUtils.isEmpty(yjstcds)) {
                yjwaterLevel = waterAndRainMapper.candidateReservoirWaterLevel(yjRsvrTable, yjstcds, null);
            }
        } else {
            // 查询某个时刻左右的水位
            if (!org.springframework.util.CollectionUtils.isEmpty(stcds)) {
                waterLevel = waterAndRainMapper.candidateReservoirWaterLevelWithTime(StationType.RESERVOIR.table() + DateUtils.date2LocalDate(dataTime).format(yyyyMM), stcds, null, dataTime);
            }
            if (!org.springframework.util.CollectionUtils.isEmpty(yjstcds)) {
                yjwaterLevel = waterAndRainMapper.candidateYjReservoirWaterLevelWithTime(dataTime);
            }
        }
        Map<String, StRsvrR> waterLevelMap = waterLevel.stream().collect(Collectors.toMap(StRsvrR::getMgstcd, o -> o));
        Map<String, StRsvrR> yjwaterLevelMap = yjwaterLevel.stream().collect(Collectors.toMap(StRsvrR::getMgstcd, o -> o));

        Map<String, StRsvrR> resultMap = new HashMap<>(stcdList.size());
        resultMap.putAll(yjwaterLevelMap);
        resultMap.putAll(waterLevelMap);
        return resultMap;
    }

    /**
     * 将水位代入库容曲线获取对应的库容
     *
     * @param rsvrList 水库列表
     * @param origin   源数据字段
     * @param consumer 目标数据字段
     */
    private void setCapacity4Rsvr(List<? extends RsvrStationWaterResVo> rsvrList,
                                  Function<RsvrStationWaterResVo, BigDecimal> origin,
                                  BiConsumer<RsvrStationWaterResVo, Double> consumer) {
        if (!org.springframework.util.CollectionUtils.isEmpty(rsvrList)) {
            List<ReservoirCapacityCurve> orgList = rsvrList.stream().filter(o -> {
                try {
                    return origin.apply(o) != null
                            && StringUtils.isNotBlank(o.getStCode());
                } catch (Exception ignore) {
                    return false;
                }
            }).map(o -> Builder.of(ReservoirCapacityCurve::new)
                    .with(ReservoirCapacityCurve::setCode, o.getStCode())
                    .with(ReservoirCapacityCurve::setWaterLevel, origin.apply(o))
                    .build()).collect(Collectors.toList());
//            if (!org.springframework.util.CollectionUtils.isEmpty(orgList)) {
//                CommonResult<Map<String, BigDecimal>> resp = commonServiceClient.calculationCapacity(orgList);
//                if (resp.getCode() == CommonCode.SUCCESS.code() && resp.getData() != null) {
//                    Map<String, BigDecimal> map = resp.getData();
//                    rsvrList.forEach(o -> {
//                        BigDecimal capacity = map.get(o.getStCode());
//                        if (capacity != null) {
//                            capacity = capacity.setScale(1, BigDecimal.ROUND_HALF_UP);
//                            consumer.accept(o, capacity.doubleValue());
//                        } else {
//                            consumer.accept(o, null);
//                        }
//                    });
//                }
//            }
        }
    }

    /**
     * 将水位代入库容曲线获取对应的库容
     *
     * @param rsvrList 水库列表
     * @param origin   源数据字段
     * @param consumer 目标数据字段
     */
    private void setCapacity4Rsvr1(List<? extends RsvrReplayResVo> rsvrList,
                                   Function<RsvrReplayResVo, BigDecimal> origin,
                                   BiConsumer<RsvrReplayResVo, Double> consumer) {
        if (!org.springframework.util.CollectionUtils.isEmpty(rsvrList)) {
            List<ReservoirCapacityCurve> orgList = rsvrList.stream().filter(o -> {
                try {
                    return origin.apply(o) != null
                            && StringUtils.isNotBlank(o.getMainStcd());
                } catch (Exception ignore) {
                    return false;
                }
            }).map(o -> Builder.of(ReservoirCapacityCurve::new)
                    .with(ReservoirCapacityCurve::setCode, o.getMainStcd())
                    .with(ReservoirCapacityCurve::setWaterLevel, origin.apply(o))
                    .build()).collect(Collectors.toList());
            if (!org.springframework.util.CollectionUtils.isEmpty(orgList)) {
                CommonResult<Map<String, BigDecimal>> resp = commonServiceClient.calculationCapacity(orgList);
                if (resp.getCode() == CommonCode.SUCCESS.code() && resp.getData() != null) {
                    Map<String, BigDecimal> map = resp.getData();
                    rsvrList.forEach(o -> {
                        BigDecimal capacity = map.get(o.getMainStcd());
                        if (capacity != null) {
                            capacity = capacity.setScale(1, BigDecimal.ROUND_HALF_UP);
                            consumer.accept(o, capacity.doubleValue());
                        } else {
                            consumer.accept(o, null);
                        }
                    });
                }
            }
        }
    }

    /**
     * 计算蓄水率
     * 蓄水率=实时水位对应库容/汛限库容
     *
     * @param result
     */
    public void computeStorageRatio(List<? extends RsvrStationWaterResVo> result) {
        setCapacity4Rsvr(result, RsvrStationWaterResVo::getCurrentLimitLevel, RsvrStationWaterResVo::setLimitLevelCapacity);
        result.forEach(o -> {
            if (o.getWaterLevelCapacity() != null
                    && o.getLimitLevelCapacity() != null) {
                o.setStorageRatio(Double.parseDouble(String.format("%.2f", HydrologyUtils.calculateStorageRatio(BigDecimal.valueOf(o.getWaterLevelCapacity()), BigDecimal.valueOf(o.getLimitLevelCapacity())).doubleValue())));
            }
        });
    }

    /**
     * 计算至汛限纳蓄能力
     * 至汛限纳蓄能力=(汛限水位对应库容-实时水位对应库容)/(集雨面积*径流系数)
     *
     * @param result
     */
    public void computeLimitStorageAbility(List<? extends RsvrStationWaterResVo> result) {
        // 2021-05-120 14:00 左右 张瑞涛说通过汛限水位带入库容曲线获得汛限库容，原因是不同时间汛限水位也不同
        setCapacity4Rsvr(result, RsvrStationWaterResVo::getCurrentLimitLevel, RsvrStationWaterResVo::setLimitLevelCapacity);
        result.forEach(o -> {
            if (o.getWaterLevelCapacity() != null
                    && o.getLimitLevelCapacity() != null//汛限水位对应库容
                    && o.getWatShedArea() != null//集雨面积
            ) {
                o.setLimitStorageAbility(HydrologyUtils.calculateStorageAbility(BigDecimal.valueOf(o.getWaterLevelCapacity()), BigDecimal.valueOf(o.getLimitLevelCapacity()), o.getWatShedArea()).intValue());
            }
        });
    }

    /**
     * 计算防洪高纳蓄水能力
     * 防洪高纳蓄水能力=(防洪高水位对应库容-实时水位对应库容)/(集雨面积*径流系数)
     *
     * @param result
     */
    public void computeFloodStorageAbility(List<? extends RsvrStationWaterResVo> result) {
        setCapacity4Rsvr(result, RsvrStationWaterResVo::getUppLevFlco, RsvrStationWaterResVo::setUppLevFlcoCapacity);
        result.forEach(o -> {
            if (o.getWaterLevelCapacity() != null
                    && o.getUppLevFlcoCapacity() != null//防洪高水位对应库容
                    && o.getWatShedArea() != null//集雨面积
            ) {
                o.setFloodStorageAbility(HydrologyUtils.calculateStorageAbility(BigDecimal.valueOf(o.getWaterLevelCapacity()), BigDecimal.valueOf(o.getUppLevFlcoCapacity()), o.getWatShedArea()).intValue());
            }
        });
    }

    /**
     * 计算正常纳蓄水能力
     * 正常纳蓄水能力=(正常水位对应库容-实时水位对应库容)/(集雨面积*径流系数)
     *
     * @param result
     */
    public void computeNormStorageAbility(List<? extends RsvrStationWaterResVo> result) {
        setCapacity4Rsvr(result, RsvrStationWaterResVo::getNormWatLev, RsvrStationWaterResVo::setNormWatLevCapacity);
        result.forEach(o -> {
            if (o.getWaterLevelCapacity() != null
                    && o.getNormWatLevCapacity() != null//正常水位对应库容
                    && o.getWatShedArea() != null//集雨面积
            ) {
                o.setNormStorageAbility(HydrologyUtils.calculateStorageAbility(BigDecimal.valueOf(o.getWaterLevelCapacity()), BigDecimal.valueOf(o.getNormWatLevCapacity()), o.getWatShedArea()).intValue());
            }
        });
    }

//    /**
//     * 求水库泄洪量
//     * 2021-05-27 14:00 左右 张瑞涛电话里说，算前后一小时的水库库容差值，就是泄洪量，私以为，这里有很大的逻辑漏洞
//     *
//     * @param rsvrList
//     * @param drainageMinutes 往前推的分钟数，默认为60
//     * @param dataTime        复盘时刻
//     */
//    public void computeRsvrDrainage(List<? extends RsvrStationWaterResVo> rsvrList, Integer drainageMinutes, Date dataTime) {
//        if (CollectionUtils.isEmpty(rsvrList)) {
//            return;
//        }
//        drainageMinutes = drainageMinutes == null ? 60 : drainageMinutes;
//        dataTime = dataTime == null ? new Date() : dataTime;
//        // 1. 先求出上一个时刻水位
//        Map<String, StRsvrR> tempMap = getRsvrWaterLevelByStcd(rsvrList.stream().map(AttResBase::getMainStcd).filter(StringUtils::isNotBlank).collect(Collectors.toList()), new Date(dataTime.getTime() - drainageMinutes * 60 * 1000));
//        Map<String, Double> waterLevelMap = tempMap.entrySet().stream().filter(o->o.getValue()!=null).collect(Collectors.toMap(Map.Entry::getKey, o->o.getValue().getRz()));
//
//        List<RsvrStationWaterResVo> tempList = new ArrayList<>(rsvrList.size());
//        for (RsvrStationWaterResVo temp : rsvrList) {
//            tempList.add(Builder.of(RsvrStationWaterResVo::new)
//                    .with(RsvrStationWaterResVo::setResCode, temp.getResCode())
//                    .with(RsvrStationWaterResVo::setWaterLevel, waterLevelMap.get(temp.getResCode()))
//                    .with(RsvrStationWaterResVo::setStCode, temp.getStCode())
//                    .build());
//        }
//        // 2. 将水位代入库容曲线 求的库容
//        setCapacity4Rsvr(tempList, o -> BigDecimal.valueOf(o.getWaterLevel()), RsvrStationWaterResVo::setWaterLevelCapacity);
//        Map<String, Double> lastTimeCapacityMap = tempList.stream().filter(o -> o.getWaterLevelCapacity() != null).collect(Collectors.toMap(RsvrStationWaterResVo::getResCode, RsvrStationWaterResVo::getWaterLevelCapacity));
//        // 3. 求差值
//        for (RsvrStationWaterResVo temp : rsvrList) {
//            Double cCapacity = temp.getWaterLevelCapacity();
//            Double lastTimeCapacity = lastTimeCapacityMap.get(temp.getResCode());
//            if (cCapacity != null && lastTimeCapacity != null) {
//                double sub = lastTimeCapacity - cCapacity;
//                if (sub > 0) {
//                    temp.setDrainage(sub);
//                }
//            }
//        }
//    }

    /**
     * 求水库泄洪量
     * 2021-06-01 11:45 张仁贡说，用洪水预报系统中每个水库的每小时的泄流量*时间 之和 +水库前后水位差的正值，如果为负值，说明关库了，返回0
     * 2021-07-04 尝试将洪水预报数据持久化到数据库中，提高访问速度
     *
     * @param rsvrList
     * @param startTime 起始时刻
     * @param endTime   结束时刻
     */
    public void computeRsvrDrainageV3(List<? extends RsvrStationWaterResVo> rsvrList, Date startTime, Date endTime,
                                      BiConsumer<RsvrStationWaterResVo, Double> consumer) {
        if (org.springframework.util.CollectionUtils.isEmpty(rsvrList) || startTime == null || endTime == null) {
            return;
        }
        // 1. 先求出起始时刻水位
        Map<String, StRsvrR> tempMap = getRsvrWaterLevelByStcd(rsvrList.stream().map(AttResBase::getMainStcd).filter(StringUtils::isNotBlank).collect(Collectors.toList()), startTime);
        Map<String, Double> waterLevelMap = tempMap.entrySet().stream().filter(o -> o.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, o -> o.getValue().getRz()));

        List<RsvrStationWaterResVo> tempList = new ArrayList<>(rsvrList.size());
        for (RsvrStationWaterResVo temp : rsvrList) {
            tempList.add(Builder.of(RsvrStationWaterResVo::new)
                    .with(RsvrStationWaterResVo::setResCode, temp.getResCode())
                    .with(RsvrStationWaterResVo::setWaterLevel, waterLevelMap.get(temp.getMainStcd()))
                    .with(RsvrStationWaterResVo::setStCode, temp.getStCode())
                    .build());
        }
        // 2. 将水位代入库容曲线 求的库容
        setCapacity4Rsvr(tempList, o -> BigDecimal.valueOf(o.getWaterLevel()), RsvrStationWaterResVo::setWaterLevelCapacity);
        Map<String, Double> lastTimeCapacityMap = tempList.stream().filter(o -> o.getWaterLevelCapacity() != null).collect(Collectors.toMap(RsvrStationWaterResVo::getResCode, RsvrStationWaterResVo::getWaterLevelCapacity));

        Map<String, Double> floodPredictMap = requestFlood2(rsvrList, startTime, endTime);
        // 3. 求差值
        for (RsvrStationWaterResVo temp : rsvrList) {
            Double cCapacity = temp.getWaterLevelCapacity();
            Double lastTimeCapacity = lastTimeCapacityMap.get(temp.getResCode());
            if (cCapacity != null && lastTimeCapacity != null) {
                double sub = lastTimeCapacity - cCapacity;
                Double floodPredict = floodPredictMap.get(temp.getResCode());
                if (sub > 0 && floodPredict != null) {
                    sub += floodPredict;
                    consumer.accept(temp, Double.parseDouble(String.format("%.1f", sub)));
                } else {
                    consumer.accept(temp, 0D);
                }
            }
        }
    }

//    /**
//     * 求水库泄洪量
//     * 2021-06-01 11:45 张仁贡说，用洪水预报系统中每个水库的每小时的泄流量*时间 之和 +水库前后水位差的正值，如果为负值，说明关库了，返回0
//     * 2021-07-04 尝试将洪水预报数据持久化到数据库中，提高访问速度
//     *
//     * @param rsvrList
//     * @param startTime 起始时刻
//     * @param endTime   结束时刻
//     */
//    public void computeRsvrDrainageV2(List<? extends RsvrStationWaterResVo> rsvrList, Date startTime, Date endTime,
//                                      BiConsumer<RsvrStationWaterResVo, Double> consumer) {
//        if (CollectionUtils.isEmpty(rsvrList) || startTime == null || endTime == null) {
//            return;
//        }
//        // 1. 先求出起始时刻水位
//        Map<String, StRsvrR> tempMap = getRsvrWaterLevelByStcd(rsvrList.stream().map(AttResBase::getMainStcd).filter(StringUtils::isNotBlank).collect(Collectors.toList()), startTime);
//        Map<String, Double> waterLevelMap = tempMap.entrySet().stream().filter(o->o.getValue()!=null).collect(Collectors.toMap(Map.Entry::getKey, o->o.getValue().getRz()));
//
//        List<RsvrStationWaterResVo> tempList = new ArrayList<>(rsvrList.size());
//        for (RsvrStationWaterResVo temp : rsvrList) {
//            tempList.add(Builder.of(RsvrStationWaterResVo::new)
//                    .with(RsvrStationWaterResVo::setResCode, temp.getResCode())
//                    .with(RsvrStationWaterResVo::setWaterLevel, waterLevelMap.get(temp.getMainStcd()))
//                    .with(RsvrStationWaterResVo::setStCode, temp.getStCode())
//                    .build());
//        }
//        // 2. 将水位代入库容曲线 求的库容
//        setCapacity4Rsvr(tempList, o -> BigDecimal.valueOf(o.getWaterLevel()), RsvrStationWaterResVo::setWaterLevelCapacity);
//        Map<String, Double> lastTimeCapacityMap = tempList.stream().filter(o -> o.getWaterLevelCapacity() != null).collect(Collectors.toMap(RsvrStationWaterResVo::getResCode, RsvrStationWaterResVo::getWaterLevelCapacity));
//        // 3. 求差值
//        for (RsvrStationWaterResVo temp : rsvrList) {
//            Double cCapacity = temp.getWaterLevelCapacity();
//            Double lastTimeCapacity = lastTimeCapacityMap.get(temp.getResCode());
//            if (cCapacity != null && lastTimeCapacity != null) {
//                double sub = lastTimeCapacity - cCapacity;
//                if (sub > 0) {
//                    sub += requestFlood4Drainage(temp, startTime, endTime);
//                    consumer.accept(temp, Double.parseDouble(String.format("%.1f", sub)));
//                } else {
//                    consumer.accept(temp, 0D);
//                }
//            }
//        }
//    }


    @Override
    public int getLimitStatus(LocalDate dataTime) {
        // 0 为正常蓄水水位 1为梅汛限水位  2为台汛限水位
        int limitStatus = 0;
        if (!dataTime.isBefore(meiLimitStart.withYear(dataTime.getYear())) && !dataTime.isAfter(meiLimitEnd.withYear(dataTime.getYear()))) {
            limitStatus = 1;
        } else if (!dataTime.isBefore(typhoonLimitStart.withYear(dataTime.getYear())) && !dataTime.isAfter(typhoonLimitEnd.withYear(dataTime.getYear()))) {
            limitStatus = 2;
        }
        return limitStatus;
    }

    /**
     * 获取水库的当前汛限水位
     *
     * @param rsvrBase 水库信息
     * @param dataTime
     * @return
     */
    @Override
    public BigDecimal getCurrentLimitWaterLevel(AttResBase rsvrBase, LocalDate dataTime) {
        EngScalEnum engScalEnum = EngScalEnum.getByLevel(rsvrBase.getEngScal());
        if (engScalEnum != null) {
            int limitStatus = getLimitStatus(dataTime);
            if (engScalEnum.getEngScalAsInt() < 4 && engScalEnum.getEngScalAsInt() > 0) {
                switch (limitStatus) {
                    case 1:
                        return rsvrBase.getMeiFloodLimitWaterLevel();
                    case 2:
                        return rsvrBase.getYphoonFloodLlimitWaterLevel();
                    default:
                        return rsvrBase.getNormWatLev();
                }
            } else {
                return rsvrBase.getNormalSpillwayCrestElevation();
            }
        }
        return rsvrBase.getNormWatLev();
    }

    /**
     * 水库工程统计，统计大中型水库24小时的泄流量
     *
     * @param dataTime 复盘时刻：格式是"yyyy-MM-dd HH:mm:ss"，不传则为最新值
     * @return
     */
    @Override
    public JSONObject rsvrProjDrainageStats(Date dataTime) {
        JSONObject result = new JSONObject();
        long drainageRsvrNum = 0;
        double drainageTotal = 0D;
        List<RsvrStationWaterResVo> list = Collections.emptyList();
        String engScales = String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, com.ygkj.enums.EngScalEnum.L1.getDesc(), com.ygkj.enums.EngScalEnum.L2.getDesc(), com.ygkj.enums.EngScalEnum.MID.getDesc());
        PageVO<RsvrStationWaterResVo> resp = rsvrWaterLevel(Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setDataTime, dataTime)
                .with(RsvrStationWaterReqVo::setEngScales, engScales)
                .with(RsvrStationWaterReqVo::setDrainage24H, true)
                .build());
        if (com.ygkj.utils.CollectionUtils.isNotBlank(resp.getList())) {
            List<RsvrStationWaterResVo> data = resp.getList();
            List<RsvrStationWaterResVo> drainageList = data.stream().filter(o -> o.getDrainage24H() != null && o.getDrainage24H() > 0).collect(Collectors.toList());
            drainageRsvrNum = drainageList.size();
            drainageTotal = drainageList.stream().mapToDouble(RsvrStationWaterResVo::getDrainage24H).sum();
            drainageList.forEach(o -> {
                if (o.getDrainage24H() != null) {
                    o.setDrainage24H(Double.parseDouble(String.format("%.1f", o.getDrainage24H())));
                }
            });
            list = data.stream().sorted(new BeanComparator<>("drainage24H").reversed()).collect(Collectors.toList());
        }
        result.put("drainageTotal", Double.parseDouble(String.format("%.1f", drainageTotal)));
        result.put("drainageRsvrNum", drainageRsvrNum);
        result.put("list", list);
        return result;
    }

    @Override
    public List<RsvrStationWaterResVo> getRsvrDrillByTime(LocalDateTime dataTime) {
        /**
         * 1. 先拿到所有的大中型水库数据
         * 2. 第一步，先去洪水预报拿到预报水位
         * 3. 然后根据预报水位计算库容曲线，超汛限信息
         * 4. 入库
         */
        List<AttResBase> rsvrTempList = projectMapper.selectReservoirByCondition(Builder.of(ProjectManageQueryVo::new).with(ProjectManageQueryVo::setLevels, String.join(com.ygkj.utils.StringUtils.JOIN_DELIMITER, com.ygkj.enums.EngScalEnum.L1.getEngScal(), com.ygkj.enums.EngScalEnum.L2.getEngScal(), com.ygkj.enums.EngScalEnum.MID.getEngScal())).build());
        rsvrTempList = rsvrTempList == null ? Collections.emptyList() : rsvrTempList;
        Date startTime = DateUtils.localDateTime2Date(dataTime.minusHours(2));
        Date endTime = DateUtils.localDateTime2Date(dataTime);
        List<RsvrStationWaterResVo> rsvrList = new ArrayList<>(rsvrTempList.size());
        for (AttResBase resBase : rsvrTempList) {
            RsvrStationWaterResVo temp = new RsvrStationWaterResVo();
            BeanUtils.copyProperties(resBase, temp);
            rsvrList.add(temp);
            temp.setStCode(temp.getMainStcd());
            temp.setDataTime(endTime);
            temp.setCurrentLimitLevel(getCurrentLimitWaterLevel(resBase, dataTime.toLocalDate()));
        }
        computeRsvrWaterLevel(rsvrList, null);
        for (RsvrStationWaterResVo temp : rsvrList) {
            Double realWateLevel = temp.getWaterLevel();
            Double waterLevelTemp = null;
            JSONObject forecastData = requestFlood4Drill(temp, startTime, endTime);
            if (forecastData != null) {
                waterLevelTemp = forecastData.getDouble("water");
                if (waterLevelTemp != null && waterLevelTemp >= temp.getDeadLev().doubleValue() && waterLevelTemp <= temp.getCheckFloodLevel().doubleValue()) {

                } else {
                    waterLevelTemp = null;
                }
            }
            if (waterLevelTemp == null) {
                temp.setWaterLevel(null);
                forecastData = requestFlood4Drill(temp, startTime, endTime);
                if (forecastData != null) {
                    waterLevelTemp = forecastData.getDouble("water");
                    if (waterLevelTemp != null && waterLevelTemp >= temp.getDeadLev().doubleValue() && waterLevelTemp <= temp.getCheckFloodLevel().doubleValue()) {

                    } else {
                        waterLevelTemp = realWateLevel;
                    }
                }
            }
            temp.setWaterLevel(waterLevelTemp);

        }
        computeRsvrOverLimitInfo(rsvrList);
        computeRsvrExternalInfo(rsvrList, Builder.of(RsvrStationWaterReqVo::new)
                .with(RsvrStationWaterReqVo::setStorageRatio, true)
                .with(RsvrStationWaterReqVo::setLimitStorageAbility, true)
                .with(RsvrStationWaterReqVo::setFloodStorageAbility, true)
                .build());
        rsvrList.forEach(o -> {
            o.setDataTime(endTime);
        });
        return rsvrList;
    }


    private JSONObject requestFlood4Drill(RsvrStationWaterResVo rsvr, Date startTime, Date endTime) {
        if (rsvr.getCurrentLimitLevel() == null || org.apache.commons.lang3.StringUtils.isBlank(rsvr.getFloodId()) || startTime == null || endTime == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String url = "http://10.36.160.145:8888/mgt/bus/floodDispatch/dispatch";
        String token = "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw";
        Map<String, Object> param = Builder.of(HashMap<String, Object>::new)
                .with(HashMap::put, "token", token)
                .with(HashMap::put, "startTime", sdf.format(startTime))
                .with(HashMap::put, "endTime", sdf.format(endTime))
                .with(HashMap::put, "flowZoom", 1)
                .with(HashMap::put, "limitWater", rsvr.getCurrentLimitLevel())
                .with(HashMap::put, "jsonFloodArray", Collections.emptyList())
                .with(HashMap::put, "jsonWaveArray", Collections.emptyList())
                .with(HashMap::put, "jsonSplitWaveArray", Collections.emptyList())
                .with(HashMap::put, "outflowZoom", 1)
                .with(HashMap::put, "outflowArray", Collections.emptyList())
                .with(HashMap::put, "projectId", rsvr.getFloodId())
                .build();
        String result = null;
        try {
            if (rsvr.getWaterLevel() != null) {
                param.put("candidateWaterLevel", rsvr.getWaterLevel());
            }
            result = HttpClientUtil.sendGet(url, new HashMap<>(), param, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return null;
        }
        try {
            JSONObject resp = JSONObject.parseObject(result);
            JSONArray data = null;
            if (resp != null && !org.springframework.util.CollectionUtils.isEmpty((data = resp.getJSONArray("data")))) {
                return data.getJSONObject(data.size() - 1);
            }
        } catch (Exception e) {
            System.out.println(param);
            e.printStackTrace();
        }
        return null;
    }

    private double requestFlood4Drainage(RsvrStationWaterResVo rsvr, Date startTime, Date endTime) {

        double totalOutRsvrDrainage = 0D;
        if (rsvr.getCurrentLimitLevel() == null || StringUtils.isBlank(rsvr.getFloodId()) || startTime == null || endTime == null) {
            return totalOutRsvrDrainage;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String url = "http://10.36.160.145:8888/mgt/bus/floodDispatch/dispatch";
        String token = "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.gcGB1bClPatg2GZv2RR66iwdG6R5UsX0kmgkMHEf3wWVrKQIFmIA1Yg16xJgmeGfNbD3A9CTHXiRkVr8gXLtoRtlspBrhwL8OfhK_yAHnsCdaBTJDctWP_Ia7y9UnzmRMYMuTFzredeMIzQikxFSzAMWAyGN0WlHE2bMze_gNOQ.b0ffbanUZTskgR_2.eYyCDUOGbfsBThAJwASO6_wT2OC7vMYIWGaSGCuaNVJlAR0EXX0NDSZvGR8rfH-rUZyJyQgfqZTb80BRjRKX5N67su-1SVyVtu2-lvcfyyyVDtBiqtMKN7z9jDlL2Jvs-pQFBv5QbQpevUZaoSecDReuHovAleQ.ZMO_T18465VDneqtm4K1vw";
        Map<String, Object> param = Builder.of(HashMap<String, Object>::new)
                .with(HashMap::put, "token", token)
                .with(HashMap::put, "startTime", sdf.format(startTime))
                .with(HashMap::put, "endTime", sdf.format(endTime))
                .with(HashMap::put, "flowZoom", 1)
                .with(HashMap::put, "limitWater", rsvr.getCurrentLimitLevel())
                .with(HashMap::put, "jsonFloodArray", Collections.emptyList())
                .with(HashMap::put, "jsonWaveArray", Collections.emptyList())
                .with(HashMap::put, "jsonSplitWaveArray", Collections.emptyList())
                .with(HashMap::put, "outflowZoom", 1)
                .with(HashMap::put, "outflowArray", Collections.emptyList())
                .with(HashMap::put, "projectId", rsvr.getFloodId())
                .build();
        String result = HttpClientUtil.sendGet(url, new HashMap<>(), param, StandardCharsets.UTF_8.name());
        try {
            JSONObject resp = JSONObject.parseObject(result);
            JSONArray data = null;
            if (resp != null && !org.springframework.util.CollectionUtils.isEmpty((data = resp.getJSONArray("data")))) {
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    JSONObject c = data.getJSONObject(i);
                    Double cFlow = c.getDouble("flow");
                    Date tempStart = c.getDate("forecastTime");
                    if (cFlow == null || tempStart == null) {
                        continue;
                    }
                    Date tempEnd = null;
                    if (i + 1 < size) {
                        JSONObject n = data.getJSONObject(i + 1);
                        tempEnd = n.getDate("forecastTime");
                    }
                    if (tempEnd != null) {
                        // 流量 （m³/s）* 时间（s）/1000 =流量（万m³）
                        totalOutRsvrDrainage += (cFlow * (tempEnd.getTime() - tempStart.getTime()) / 1000) / 10000;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalOutRsvrDrainage;
    }

    private Map<String, Double> requestFlood2(List<? extends RsvrStationWaterResVo> rsvrList, Date startTime, Date endTime) {
        if (!org.springframework.util.CollectionUtils.isEmpty(rsvrList) && startTime != null && endTime != null) {

        } else {
            return Collections.emptyMap();
        }
        String resCodes = rsvrList.stream().map(RsvrStationWaterResVo::getResCode).collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
        List<RsvrFloodPredict> allFloodPredictList = this.waterAndRainMapper.selectRsvrFloodPredict(resCodes, startTime, endTime);
        Map<String, List<RsvrFloodPredict>> floodPredictMap = allFloodPredictList.stream().collect(Collectors.groupingBy(RsvrFloodPredict::getResCode));
        Map<String, Double> result = new HashMap<>(rsvrList.size());
        for (RsvrStationWaterResVo rsvr : rsvrList) {
            List<RsvrFloodPredict> floodPredictList = floodPredictMap.get(rsvr.getResCode());
            double totalOutRsvrDrainage = 0D;
            if (!org.springframework.util.CollectionUtils.isEmpty(floodPredictList)) {
                int size = floodPredictList.size();
                for (int i = 0; i < size; i++) {
                    RsvrFloodPredict c = floodPredictList.get(i);
                    Double cFlow = null;
                    try {
                        cFlow = new BigDecimal(c.getFlow()).doubleValue();
                    } catch (Exception ignore) {

                    }
                    Date tempStart = c.getForecastTime();
                    if (cFlow == null || tempStart == null) {
                        continue;
                    }
                    Date tempEnd = null;
                    if (i + 1 < size) {
                        RsvrFloodPredict n = floodPredictList.get(i + 1);
                        tempEnd = n.getForecastTime();
                    }
                    if (tempEnd != null) {
                        // 流量 （m³/s）* 时间（s）/1000 =流量（万m³）
                        totalOutRsvrDrainage += (cFlow * (tempEnd.getTime() - tempStart.getTime()) / 1000) / 10000;
                    }
                }
            }
            result.put(rsvr.getResCode(), totalOutRsvrDrainage);
        }
        return result;
    }

    /**
     * 水源保证列表和柱状图
     *
     * @param watSrcName 水源地名称
     * @return
     */
    @Override
    public List<WatSrcResVo> rsvrWatSrcTable(String watSrcName) {
        List<WatSrcResVo> result = projectMapper.selectRsvrWatSrc(watSrcName);
        if (CollectionUtils.isBlank(result)) {
            return Collections.emptyList();
        }
        // 1. 先查找实时水位
        Map<String, StRsvrR> waterLevelMap = getRsvrWaterLevelByStcd(result.stream().map(WatSrcResVo::getMainStcd).filter(StringUtils::isNotBlank).collect(Collectors.toList()), null);
        for (WatSrcResVo watSrcResVo : result) {
            if (StringUtils.isBlank(watSrcResVo.getMainStcd())) {
                continue;
            }
            StRsvrR waterLevel = waterLevelMap.get(watSrcResVo.getMainStcd());
            if (waterLevel == null) {
                continue;
            }
            watSrcResVo.setWaterLevel(waterLevel.getRz());
        }

        // 2. 计算实时库容
        setCapacity4WatSrcResVo(result, o -> BigDecimal.valueOf(o.getWaterLevel()), WatSrcResVo::setCurrentCapacity);
//        // 2. 计算死水位对应库容，而不是死库容
        setCapacity4WatSrcResVo(result, WatSrcResVo::getDeadLev, WatSrcResVo::setDeadLevCapacity);

        // 3. 计算水源可用水量
        // 4. 计算供水能力
        result.forEach(o -> {
            o.computeSupportWater();
            o.computeSupplyAbility();
        });

        // 降序排序
        result.sort((o1, o2) -> {
            if (o1.getSupplyAbility() != null && o2.getSupplyAbility() != null) {
                return o2.getSupplyAbility().compareTo(o1.getSupplyAbility());
            } else if (o1.getSupplyAbility() != null && o2.getSupplyAbility() == null) {
                return -1;
            } else if (o1.getSupplyAbility() == null && o2.getSupplyAbility() != null) {
                return 1;
            } else {
                return 0;
            }
        });
        return result;
    }

    /**
     * 水源保证列表 统计
     *
     * @param watSrcName
     * @return
     */
    @Override
    public JSONObject stats4rsvrWatSrcTable(String watSrcName) {
        JSONObject result = new JSONObject();
        WatSrcResVo maxSupply = null;
        WatSrcResVo minSupply = null;
        List<WatSrcResVo> watSrcList = rsvrWatSrcTable(watSrcName);
        if (CollectionUtils.isNotBlank(watSrcList)) {
            maxSupply = watSrcList.stream().filter(o -> o.getSupplyAbility() != null).max(Comparator.comparing(WatSrcResVo::getSupplyAbility)).orElse(null);
            minSupply = watSrcList.stream().filter(o -> o.getSupplyAbility() != null).min(Comparator.comparing(WatSrcResVo::getSupplyAbility)).orElse(null);
        }
        result.put("maxSupply", maxSupply);
        result.put("minSupply", minSupply);
        return result;
    }

    /**
     * 水库复盘统计
     *
     * @return
     */
    @Override
    public JSONObject rsvrReplayStats(RsvrReplayReqVo reqVo) {
        /*
        1. 先查出所有的水库
        2. 然后再查出所有水库的主测站查出此期间的水位时间升序列表
        3. 然后再根据水库当前汛限水位，和判断是否超警，只要有一次超警，那么就算超警了
        4. 然后根据超警次数，算得超警时常 times*5minutes
         */
        Date start = null;
        Date end = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start = sdf.parse(reqVo.getStart());
            end = sdf.parse(reqVo.getEnd());
        } catch (Exception ignore) {

        }
        JSONObject result = new JSONObject();
        if (start == null || end == null) {
            return result;
        }

        ProjectManageQueryVo queryVo = new ProjectManageQueryVo();
        if (StringUtils.isNotBlank(reqVo.getResCodes())) {
            queryVo.setCodes(Arrays.asList(reqVo.getResCodes().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        if (StringUtils.isNotBlank(reqVo.getEngScales())) {
            queryVo.setLevels(Arrays.stream(reqVo.getEngScales().split(com.ygkj.utils.StringUtils.JOIN_DELIMITER))
                    .map(o -> Optional.ofNullable(EngScalEnum.getByDesc(o)))
                    .filter(Optional::isPresent)
                    .map(o -> o.get().getEngScal())
                    .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)));
        }
        queryVo.setBas(reqVo.getResBas());
        queryVo.setAreaCode(reqVo.getResAdcd());
        queryVo.setProjectName(reqVo.getResName());
        List<AttResBase> rsvrList = projectMapper.selectReservoirByCondition(queryVo);
        if (org.springframework.util.CollectionUtils.isEmpty(rsvrList) || reqVo.getStart() == null || reqVo.getEnd() == null) {
            return result;
        }
        List<RsvrReplayResVo> tempList = new ArrayList<>(rsvrList.size());
        LocalDate now = LocalDate.now();
        int limitStatus = getLimitStatus(now);
        List<String> tables = echoWaterRainTableByDateInternal(StationType.RESERVOIR.table(), start, end);
        List<StRsvrR> waterLevelTempList = Collections.emptyList();
        if (!org.springframework.util.CollectionUtils.isEmpty(tables)) {
            waterLevelTempList = waterAndRainMapper.candidateRsvrWaterLevelWithTimeInternal(
                    tables,
                    rsvrList.stream()
                            .map(AttResBase::getMainStcd)
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.toList()),
                    start,
                    end);
        }
        waterLevelTempList = waterLevelTempList == null ? Collections.emptyList() : waterLevelTempList;
        Map<String, List<StRsvrR>> waterLevelMap = waterLevelTempList.stream()
                .collect(Collectors.groupingBy(StRsvrR::getMgstcd));

        Comparator<StRsvrR> waterLevelComparator = (o1, o2) -> {
            if (o1.getRz() != null && o2.getRz() != null) {
                return o1.getRz().compareTo(o2.getRz());
            } else if (o1.getRz() == null & o2.getRz() != null) {
                return -1;
            } else if (o1.getRz() != null & o2.getRz() == null) {
                return 1;
            } else {
                return 0;
            }
        };
        for (AttResBase attResBase : rsvrList) {
            RsvrReplayResVo temp = new RsvrReplayResVo();
            BeanUtils.copyProperties(attResBase, temp);
            tempList.add(temp);
            List<StRsvrR> waterLevelList = waterLevelMap.get(temp.getMainStcd());
            if (org.springframework.util.CollectionUtils.isEmpty(waterLevelList)) {
                temp.setWaterLevelList(Collections.emptyList());
                continue;
            }
            waterLevelList = waterLevelList.parallelStream().sorted(Comparator.comparing(StRsvrR::getTm)).collect(Collectors.toList());
            temp.setWaterLevelList(waterLevelList);

            waterLevelList.parallelStream().max(waterLevelComparator).ifPresent(o -> {
                temp.setHighestWaterLevel(o.getRz());
                temp.setHighestWaterLevelTm(o.getTm());
            });

            waterLevelList.parallelStream().min(waterLevelComparator).ifPresent(o -> {
                temp.setLowestWaterLevel(o.getRz());
                temp.setLowestWaterLevelTm(o.getTm());
            });

            temp.setStartWaterLevel(waterLevelList.get(0).getRz());
            temp.setStartWaterLevelTm(waterLevelList.get(0).getTm());

            temp.setEndWaterLevel(waterLevelList.get(waterLevelList.size() - 1).getRz());
            temp.setEndWaterLevelTm(waterLevelList.get(waterLevelList.size() - 1).getTm());

            EngScalEnum engScalEnum = EngScalEnum.getByLevel(attResBase.getEngScal());
            if (engScalEnum != null) {
                temp.setEngScal(engScalEnum.getDesc());
                if (engScalEnum.getEngScalAsInt() < 4 && engScalEnum.getEngScalAsInt() > 0) {
                    switch (limitStatus) {
                        case 1:
                            temp.setCurrentLimitLevel(temp.getMeiFloodLimitWaterLevel());
                            break;
                        case 2:
                            temp.setCurrentLimitLevel(temp.getYphoonFloodLlimitWaterLevel());
                            break;
                        default:
                            temp.setCurrentLimitLevel(temp.getNormWatLev());
                            break;
                    }
                } else {
                    temp.setCurrentLimitLevel(attResBase.getNormalSpillwayCrestElevation());
                }
            }
            if (temp.getCurrentLimitLevel() != null) {
                double cll = temp.getCurrentLimitLevel().doubleValue();
                long overLimitTimes = waterLevelList.parallelStream().filter(o -> o.getRz() > cll).count();
                if (overLimitTimes > 0) {
                    temp.setOverWarn(true);
                    temp.setOverWarnMinutes((int) (5 * overLimitTimes));
                }
            }
        }
        if (reqVo.isHighestWaterLevelCapacity()) {
            setCapacity4Rsvr1(tempList, o -> BigDecimal.valueOf(o.getHighestWaterLevel()), RsvrReplayResVo::setHighestCapacity);
        }
        if (reqVo.isLowestWaterLevelCapacity()) {
            setCapacity4Rsvr1(tempList, o -> BigDecimal.valueOf(o.getLowestWaterLevel()), RsvrReplayResVo::setLowestCapacity);
        }
        if (reqVo.isStartWaterLevelCapacity()) {
            setCapacity4Rsvr1(tempList, o -> BigDecimal.valueOf(o.getStartWaterLevel()), RsvrReplayResVo::setStartCapacity);
        }
        if (reqVo.isEndWaterLevelCapacity()) {
            setCapacity4Rsvr1(tempList, o -> BigDecimal.valueOf(o.getEndWaterLevel()), RsvrReplayResVo::setEndCapacity);
        }

        int warnNum = 0;
        RsvrReplayResVo maxWarnTimeRsvr = null;
        double totalStorage = 0D;
        RsvrReplayResVo maxStorageRsvr = null;
        double totalDrainage = 0D;
        int drainageNum = 0;
        RsvrReplayResVo maxDrainageRsvr = null;

        for (RsvrReplayResVo temp : tempList) {
            if (temp.getHighestCapacity() != null && temp.getLowestCapacity() != null) {
                temp.setCapacitySub(Double.parseDouble(String.format("%.2f", temp.getHighestCapacity() - temp.getLowestCapacity())));
            }
            if (temp.getStartCapacity() != null && temp.getEndCapacity() != null) {
                temp.setCapacitySub(Double.parseDouble(String.format("%.2f", temp.getEndCapacity() - temp.getStartCapacity())));
            }
            if (temp.isOverWarn()) {
                warnNum++;
                if (maxWarnTimeRsvr == null) {
                    maxWarnTimeRsvr = temp;
                } else if (maxWarnTimeRsvr.getOverWarnMinutes() < temp.getOverWarnMinutes()) {
                    maxWarnTimeRsvr = temp;
                }
            }
            if (temp.getCapacitySub() != null) {
                totalStorage += temp.getCapacitySub();
                if (maxStorageRsvr == null) {
                    maxStorageRsvr = temp;
                } else if (maxStorageRsvr.getCapacitySub() < temp.getCapacitySub()) {
                    maxStorageRsvr = temp;
                }
            }
        }
        if (StringUtils.isNotBlank(reqVo.getOrderColumn())) {
            Comparator<RsvrReplayResVo> comparator = new BeanComparator<>(reqVo.getOrderColumn());
            if (reqVo.isAscOrDesc()) {
                comparator = comparator.reversed();
            }
            tempList = tempList.stream().sorted(comparator).collect(Collectors.toList());
        }
        result.put("warnNum", warnNum);
        result.put("maxWarnTimeRsvr", maxWarnTimeRsvr == null ? new RsvrReplayResVo() : maxWarnTimeRsvr);
        result.put("totalStorage", Double.parseDouble(String.format("%.2f", totalStorage)));
        result.put("maxStorageRsvr", maxStorageRsvr == null ? new RsvrReplayResVo() : maxStorageRsvr);
        result.put("totalDrainage", Double.parseDouble(String.format("%.2f", totalDrainage)));
        result.put("maxDrainageRsvr", maxDrainageRsvr == null ? new RsvrReplayResVo() : maxDrainageRsvr);
        result.put("drainageNum", drainageNum);
        if (!reqVo.isWaterLevelCurve()) {
            tempList.forEach(o -> {
                o.setWaterLevelList(Collections.emptyList());
            });
        }
        result.put("list", tempList);
        return result;
    }

    /**
     * 导出水库数据
     *
     * @param response
     * @param reqVo
     */
    @Override
    public void exportRsvr(HttpServletResponse response, RsvrStationWaterReqVo reqVo) {
        // 禁止分页
        reqVo.setPageNum(null);
        reqVo.setPageSize(null);
        List<RsvrStationWaterResVo> rsvrList = rsvrUnitList(reqVo).getList();
        if (reqVo.getExportStrategy() == null) {
            reqVo.setExportStrategy(0);
        }
        switch (reqVo.getExportStrategy()) {
            case 0: {
                if (EngScalEnum.SMALL.getDesc().equals(reqVo.getEngScales())) {
                    ExportUtils.exportExcel(rsvrList, "水库",
                            new String[]{"名称", "流域", "行政区划", "水位(m)", "更新时间", "鉴定时间", "鉴定结论", "有无加固", "溢出洪水道高程(m)", "实时库容(万m³)", "总库容(万m³)", "蓄水率", "正常蓄水位(m)"},
                            new String[]{"resName", "bas", "areaName", "waterLevel", "dataTime", "safetyTime", "safetyConclusion", "reinforce", "normalSpillwayCrestElevation", "waterLevelCapacity", "totCap", "storageRatio", "normWatLev"}, response, null, Collections.emptyMap());

                } else {
                    ExportUtils.exportExcel(rsvrList, "水库",
                            new String[]{"名称", "流域", "行政区划", "水位(m)", "汛限水位(m)", "防洪高水位(m)", "更新时间", "鉴定时间", "鉴定结论", "有无加固", "蓄水率", "实时库容(万m³)", "汛限水位库容(万m³)", "防洪高水位库容(万m³)", "至汛限纳蓄水能力(mm)", "至防洪高纳蓄能力(mm)"},
                            new String[]{"resName", "bas", "areaName", "waterLevel", "currentLimitLevel", "uppLevFlco", "dataTime", "safetyTime", "safetyConclusion", "reinforce", "storageRatio", "waterLevelCapacity", "limitLevelCapacity", "uppLevFlcoCapacity", "limitStorageAbility", "floodStorageAbility"}, response, null, Collections.emptyMap());


                }
            }
            break;
            case 1: {
                ExportUtils.exportExcel(rsvrList, "大中型水库",
                        new String[]{"名称", "流域", "行政区划", "水位(m)", "汛限水位(m)", "防洪高水位(m)", "更新时间", "鉴定时间", "鉴定结论", "有无加固", "24累计出库量(万m³)", "48累计出库量(万m³)", "72累计出库量(万m³)", "蓄水率", "实时库容(万m³)", "汛限水位库容(万m³)", "防洪高水位库容(万m³)", "至汛限纳蓄水能力(mm)", "至防洪高纳蓄能力(mm)"},
                        new String[]{"resName", "bas", "areaName", "waterLevel", "currentLimitLevel", "uppLevFlco", "dataTime", "safetyTime", "safetyConclusion", "reinforce", "drainage24H", "drainage48H", "drainage72H", "storageRatio", "waterLevelCapacity", "limitLevelCapacity", "uppLevFlcoCapacity", "limitStorageAbility", "floodStorageAbility"}, response, null, Collections.emptyMap());

            }
            break;
        }

    }

    /**
     * 将水位代入库容曲线获取对应的库容
     *
     * @param rsvrList 水库列表
     * @param origin   源数据字段
     * @param consumer 目标数据字段
     */
    private void setCapacity4WatSrcResVo(List<WatSrcResVo> rsvrList,
                                         Function<WatSrcResVo, BigDecimal> origin,
                                         BiConsumer<WatSrcResVo, BigDecimal> consumer) {
        if (!org.springframework.util.CollectionUtils.isEmpty(rsvrList)) {
            List<ReservoirCapacityCurve> orgList = rsvrList.stream().filter(o -> {
                try {
                    return origin.apply(o) != null
                            && StringUtils.isNotBlank(o.getMainStcd());
                } catch (Exception ignore) {
                    return false;
                }
            }).map(o -> Builder.of(ReservoirCapacityCurve::new)
                    .with(ReservoirCapacityCurve::setCode, o.getMainStcd())
                    .with(ReservoirCapacityCurve::setWaterLevel, origin.apply(o))
                    .build()).collect(Collectors.toList());
            if (!org.springframework.util.CollectionUtils.isEmpty(orgList)) {
//                CommonResult<Map<String, BigDecimal>> resp = commonServiceClient.calculationCapacity(orgList);
//                CommonResult<Map<String, BigDecimal>> resp =
//                if (resp.getCode() == CommonCode.SUCCESS.code() && resp.getData() != null) {
//                    Map<String, BigDecimal> map = resp.getData();
//                    rsvrList.forEach(o -> {
//                        BigDecimal capacity = map.get(o.getMainStcd());
//                        if (capacity != null) {
//                            capacity = capacity.setScale(1, BigDecimal.ROUND_HALF_UP);
//                            consumer.accept(o, capacity);
//                        } else {
//                            consumer.accept(o, null);
//                        }
//                    });
//                }
            }
        }
    }

    /**
     * 水源保障c位
     *
     * @return
     */
    @Override
    public JSONObject rsvrWatSrc4CWindow() {
        List<WatSrcResVo> tempList = rsvrWatSrcTable(null);
        JSONObject result = new JSONObject();
//        综合供水能力=Σ（水源可用水量*水源供水能力）/Σ水源可用水量
        int compositeSupplyAbility = 0;
        BigDecimal a = tempList.stream()
                .filter(o -> o.getSupplyAbility() != null && o.getSupportWater() != null)// 去空
                .map(o -> o.getSupportWater().multiply(new BigDecimal(o.getSupplyAbility())))//相乘
                .reduce(BigDecimal.ZERO, BigDecimal::add);// 求和
        BigDecimal b = tempList.stream()
                .map(WatSrcResVo::getSupportWater)// 去空
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);// 求和
        if (b.compareTo(BigDecimal.ZERO) > 0) {
            compositeSupplyAbility = a.divide(b, 0, BigDecimal.ROUND_HALF_UP).intValue();
        }
        WatSrcResVo min = null;
        Optional<WatSrcResVo> minOpt = tempList.stream().filter(o -> o.getSupplyAbility() != null).min(Comparator.comparing(WatSrcResVo::getSupplyAbility));
        if (minOpt.isPresent()) {
            min = minOpt.get();
        }
        result.put("min", min);
        result.put("compositeSupplyAbility", compositeSupplyAbility);
        return result;
    }

    /**
     * 导出水源保障列表
     *
     * @param queryVo
     * @param response
     */
    @Override
    public void exportRsvrWatSrc(RsvrWatSrcQueryVo queryVo, HttpServletResponse response) {
        List<WatSrcResVo> rsvrList = rsvrWatSrcTable(queryVo.getWatSrcName());
        ExportUtils.exportExcel(rsvrList, "大中型水库",
                new String[]{"水源地名称", "水源可用水量", "日平均供水量", "供水能力(天)"},
                new String[]{"name", "supportWater", "dayWatSupply", "supplyAbility"},
                response, null, Collections.emptyMap());
    }

    List<String> echoWaterRainTableByDateInternal(String tablePrefix, Date start, Date end) {
        if (StringUtils.isBlank(tablePrefix) || start == null || end == null) {
            return Collections.emptyList();
        }
        LocalDate startTemp = DateUtils.date2LocalDate(start);
        LocalDate endTemp = DateUtils.date2LocalDate(end);
        LinkedHashSet<String> sequenceSet = new LinkedHashSet<>();
        do {
            String table = tablePrefix + startTemp.format(yyyyMM);
            if (StringUtils.isNotBlank(this.waterAndRainMapper.checkTableExist(table))) {
                sequenceSet.add(table);
            }
            startTemp = startTemp.plusDays(1);
        } while (startTemp.isBefore(endTemp));
        return new ArrayList<>(sequenceSet);
    }
}
