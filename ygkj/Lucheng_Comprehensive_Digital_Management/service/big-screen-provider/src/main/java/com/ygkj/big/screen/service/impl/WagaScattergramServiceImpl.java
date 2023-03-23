package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.mapper.GiveAnAlarmMapper;
import com.ygkj.big.screen.service.WagaScattergramService;
import com.ygkj.big.screen.vo.response.AttWagaBaseVo;
import com.ygkj.big.screen.vo.response.WagaScattergramVo;
import com.ygkj.enums.StationType;
import com.ygkj.gragh.model.StRiverR;
import com.ygkj.gragh.model.StTideR;
import com.ygkj.gragh.model.StWasR;
import com.ygkj.gragh.rpc.GraphRpcService;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ygkj.com.util.DateUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wgf
 * @date 2021/12/24 10:24
 * @Description:
 */
@Service("wagaScattergramService")
public class WagaScattergramServiceImpl implements WagaScattergramService {
    @Reference
    private GraphRpcService graphRpcService;
    @Resource
    private GiveAnAlarmMapper giveAnAlarmMapper;

    private DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public CommonResult latestData() {
        //写死绑定的河道和潮位站
        //              鹤城      温溪     戍浦江下游  上戌（21年建） 岩门上游（21年建） 双屿康龙 划龙桥（水心殿） 七都内河 藤桥
        String[] hd = {"01653", "01332", "30032906", "30032908", "30032905", "06722", "06731", "06730", "02074"};
        //              郭公山    七都        龙湾
        String[] cw = {"06896", "014835", "70503800"};
        //全部要查的测站
        String[] qb = {"01653", "01332", "30032906", "30032908", "30032905", "06722", "06731", "06730", "02074", "06896", "014835", "70503800"};
        //查询相关测站信息
        List<WagaScattergramVo> stationData = giveAnAlarmMapper.getStationData(Arrays.asList(qb));

        String table1 = StationType.RIVER.table().concat(LocalDate.now().format(yyyyMM));
        List<StRiverR> stRiverRS = graphRpcService.selectRiverByTimeCode(table1, Arrays.asList(hd), null);
        String table2 = StationType.TIDE.table().concat(LocalDate.now().format(yyyyMM));
        List<StTideR> stTideRS = graphRpcService.selectTideByTimeCode(table2, Arrays.asList(cw), null);
        for (WagaScattergramVo stationDatum : stationData) {
            if (stationDatum.getStName().contains("21年建")){
                stationDatum.setStName(StringUtils.substringBefore(stationDatum.getStName(),"（"));
            }
            if (stRiverRS != null && stRiverRS.size() > 0) {
                for (StRiverR stRiverR : stRiverRS) {
                    double waterLevel = stRiverR.getZ().doubleValue();
                    String mgstcd = stRiverR.getMgstcd();
                    Date tm = stRiverR.getTm();
                    if (stationDatum.getStCode().equals(mgstcd)) {
                        stationDatum.setLevel(waterLevel);
                        stationDatum.setTm(DateUtil.format(tm, "yyyy-MM-dd HH:mm:ss"));
                    }
                }
            }
            if (stTideRS != null && stTideRS.size() > 0) {
                for (StTideR stTideR : stTideRS) {
                    double tdz = stTideR.getTdz().doubleValue();
                    String mgstcd = stTideR.getMgstcd();
                    Date tm = stTideR.getTm();
                    if (stationDatum.getStCode().equals(mgstcd)) {
                        stationDatum.setLevel(tdz);
                        stationDatum.setTm(DateUtil.format(tm, "yyyy-MM-dd HH:mm:ss"));
                    }
                }
            }
        }
        return CommonResult.success(stationData);
    }

    @Override
    public CommonResult newGateOvs(String stcd) {
        if (StringUtils.isNotBlank(stcd)){
            List<String> sixstCodes = Arrays.asList("2021101101", "2021101102", "2021101103", "2021101104", "2021101105", "2021101106");
            String table = StationType.GATE.table().concat(LocalDate.now().format(yyyyMM));
            List<StWasR> stWasRS = graphRpcService.selectWasByTimeCode(table, Arrays.asList(stcd), null);
            if (!CollectionUtils.isEmpty(stWasRS)){
                String ovs = stWasRS.get(0).getOvs();
                ArrayList<BigDecimal> list = new ArrayList<>();
                if (ovs != null) {
                    String[] split = ovs.split(",");
                    for (String s : split) {
                        BigDecimal valueOf = new BigDecimal(s);
                        if (sixstCodes.contains(stcd)) {
                            list.add(valueOf.divide(new BigDecimal(100),3,BigDecimal.ROUND_HALF_UP));
                        } else {
                            list.add(valueOf);
                        }
                    }
                }
                return CommonResult.success(list);
            }
        }
        return CommonResult.success();
    }

    @Override
    public CommonResult latestGatage() {
        String[] wagaCodes = new String[]{
                //      河口              十里              仰义                             岩门
                "3350ee98-1c7b-11ea-8760-6c92bf66b51e", "SZ330302000122", "仰义水闸", "SZ330302000121",
                //      卧旗              北排              广化              勤奋
                "SZ330301000007", "SZ330302000102", "SZ330301000001", "SZ330301000008",
                //      灰桥              三十六             黎明              二村
                "SZ330301000012", "SZ330302000126", "SZ330301000011", "SZ330302000127",
                //      杨府山             七都              老涂北             下段
                "SZ330301000058", "SZ330302000099", "SZ330302000100", "SZ330302000101"};
        List<String> wagaCodeList = Arrays.asList(wagaCodes);
        List<String> sixstCodes = Arrays.asList("2021101101", "2021101102", "2021101103", "2021101104", "2021101105", "2021101106");
        //查询所有的水闸和绑定的主测站
        List<AttWagaBaseVo> wageData = giveAnAlarmMapper.getWageData(wagaCodeList);
        Map<String, AttWagaBaseVo> map = wageData.stream().collect(Collectors.toMap(AttWagaBaseVo::getWagaCode, Function.identity()));
        wageData.clear();
        for (String wagaCode : wagaCodes) {
            if (map.containsKey(wagaCode)) {
                wageData.add(map.get(wagaCode));
            } else {
                AttWagaBaseVo attWagaBaseVo = new AttWagaBaseVo();
                attWagaBaseVo.setWagaName(wagaCode);
                attWagaBaseVo.setRiverName("仰义河");
                attWagaBaseVo.setGaorNum(3);
                wageData.add(attWagaBaseVo);
            }
        }
        List<String> list = wageData.stream().map(AttWagaBaseVo::getStcd).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(list)) {
            String table = StationType.GATE.table().concat(LocalDate.now().format(yyyyMM));
            List<StWasR> stWasRS = graphRpcService.selectWasByTimeCode(table, list, null);
            if (stWasRS != null && stWasRS.size() > 0) {
                //查询现在的控运水位
              List<JSONObject> kyWaterLevel = giveAnAlarmMapper.kyWaterLevel();
                for (AttWagaBaseVo wageDatum : wageData) {
                    String stcd = wageDatum.getStcd();
                    if (StringUtils.isEmpty(stcd)) {
                        continue;
                    }
                    for (StWasR stWasR : stWasRS) {
                        String ovs = stWasR.getOvs();
                        Date tm = stWasR.getTm();
                        String mgstcd = stWasR.getMgstcd();

                        if (mgstcd.equals(stcd)) {
                            if (ovs != null) {
                                String[] split = ovs.split(",");
                                Double value = Double.valueOf(split[0]);
                                for (String s : split) {
                                    Double valueOf = Double.valueOf(s);
                                    if (valueOf > value) {
                                        value = valueOf;
                                    }
                                }
                                value = new BigDecimal(value + "").setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                                wageDatum.setOvs(String.valueOf(value));
                                if (sixstCodes.contains(mgstcd) && wageDatum.getOvs() != null) {
                                    wageDatum.setOvs(String.valueOf(new BigDecimal(Double.valueOf(wageDatum.getOvs()) / 100).setScale(3, BigDecimal.ROUND_HALF_UP)));
                                }
                            }
                            wageDatum.setUpz(stWasR.getUpz());
                            wageDatum.setDwz(stWasR.getDwz());
                            wageDatum.setLl(stWasR.getLl());
                            wageDatum.setTm(DateUtil.format(tm, "yyyy-MM-dd HH:mm:ss"));
                            wageDatum.setMxgtq(stWasR.getMxgtq());
                            //负荷分析
                            load(wageDatum);
                            //控运分析、排水分析
                            analyse(wageDatum,kyWaterLevel);
                        }
                    }
                }
            }

        }
        return CommonResult.success(wageData);
    }

    /**
     * 负荷分析
     *
     * @param wageDatum
     */
    private void load(AttWagaBaseVo wageDatum) {
        try {
            String llStr = wageDatum.getLl();
            if (StringUtils.isEmpty(llStr)) {
                //流量为空
                wageDatum.setLlStatus(-1);
            } else {
                BigDecimal ll = new BigDecimal(llStr);
                //优先取测站的最大流量
                String maxStr = wageDatum.getMxgtq();
                BigDecimal max;
                if (StringUtils.isEmpty(maxStr)) {
                    //如果测站的最大流量为空则用水闸的最大流量
                    max = wageDatum.getDesLockDisc();
                } else {
                    max = new BigDecimal(maxStr);
                }
                if (max == null) {
                    wageDatum.setLlStatus(0);
                } else {
                    //计算流量和最大流量的比率
                    double rate = ll.divide(max, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (rate >= 1) {
                        wageDatum.setLlStatus(2);
                    } else if (rate >= 0.9) {
                        wageDatum.setLlStatus(1);
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    /**
     * 控运分析、排水分析
     *
     * @param wageDatum
     */
    private void analyse (AttWagaBaseVo wageDatum,List<JSONObject> kyWaterLevel) {
        String wagaCode = wageDatum.getWagaCode();
        //控运分析0正常、1超控运、2无控运      排水分析0正常、1强排
        List<JSONObject> list = kyWaterLevel.stream().filter(m -> m.getString("prcd").equals(wagaCode)).collect(Collectors.toList());
        if (list.size() > 0){
            JSONObject jsonObject = list.get(0);
            //控制水位
            Double waterLevel = jsonObject.getDoubleValue("water_level");
            int type = jsonObject.getIntValue("type");
            Double upz = wageDatum.getUpz();
            Double dwz = wageDatum.getDwz();
            if (waterLevel != null && upz != null && waterLevel< upz){
                wageDatum.setKyStatus(1);
            }
            //排水分析 主汛期闸上水位小于闸下水位，且闸上水位超过控制水位  其他情况都是正常
            if (type != 3){
                //汛期类型:1台汛期,2梅汛期,3非汛期（不是3的就是主汛期）
               if (waterLevel != null && upz != null && dwz != null && dwz > upz && upz > waterLevel){
                   wageDatum.setPsStatus(1);
               }
            }

        } else {
            wageDatum.setKyStatus(2);
        }
    }

    public static void main(String[] args) {
        String ovs = "0.3,0.6,0.7";
        String[] split = ovs.split(",");
        String valueStr = Arrays.stream(split).max(Comparator.comparingDouble(m -> Double.valueOf(m))).get();
        Double value = Double.valueOf(valueStr);
        System.out.println(value);
    }
}
