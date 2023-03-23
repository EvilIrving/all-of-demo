package com.ygkj.gragh.service.impl;

import com.ygkj.gragh.mapper.WaterSecurityMapper;
import com.ygkj.gragh.model.WiuPlan;
import com.ygkj.gragh.model.WiuUse;
import com.ygkj.gragh.service.WaterSecurityService;
import com.ygkj.gragh.vo.request.KpakmaReqVo;
import com.ygkj.gragh.vo.request.WainReqVo;
import com.ygkj.gragh.vo.request.WiuReqVo;
import com.ygkj.gragh.vo.response.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("waterSecurityService")
public class WaterSecurityServiceImpl implements WaterSecurityService {

    private static final Double wiuRate = 0.75;

    @Resource
    private WaterSecurityMapper waterSecurityMapper;

    @Override
    public List<KpakmaResVo> listKpakma(KpakmaReqVo kpakmaReqVo) {
        if (kpakmaReqVo.getType() == 1) {
            kpakmaReqVo.setAreaCode(trimAdcd(kpakmaReqVo.getAreaCode()));
        }
        List<KpakmaResVo> result = null;
        //水土流失区基本信息列表
        List<KpakmaBaseResVo> list = waterSecurityMapper.listKpakma(kpakmaReqVo);
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, String> listMap = list.stream()
                    .collect(Collectors.toMap(KpakmaBaseResVo::getKpakmaCode, KpakmaBaseResVo::getKpakmaName, (k1, k2) -> k2));
            //水土流失面积信息
            result = waterSecurityMapper.listKpakmaYear(kpakmaReqVo.getYear());
            //去掉筛选条件没有命中的数据
            if (!CollectionUtils.isEmpty(result)) {
                Iterator<KpakmaResVo> it = result.iterator();
                while (it.hasNext()) {
                    KpakmaResVo kpakmaResVo = it.next();
                    if (listMap.containsKey(kpakmaResVo.getKpakmaCode())) {
                        kpakmaResVo.setKpakmaName(listMap.get(kpakmaResVo.getKpakmaCode()));
                    } else {
                        it.remove();
                    }
                }
            }
        }

        return result;
    }

    private String trimAdcd(String areaCode) {
        if (!StringUtils.isEmpty(areaCode)) {
            if (areaCode.endsWith("00000000")) {
                areaCode = areaCode.substring(0, 4);
            } else if (areaCode.endsWith("000000")) {
                areaCode = areaCode.substring(0, 6);
            } else if (areaCode.endsWith("0000")) {
                areaCode = areaCode.substring(0, 8);
            }
        }
        return areaCode;
    }

    @Override
    public List<WiuResVo> listWiu(WainReqVo wainReqVo) {
        if (wainReqVo.getType() == 1) {
            wainReqVo.setAreaCode(trimAdcd(wainReqVo.getAreaCode()));
        }
        //取水户基本信息
        List<WiuResVo> list = waterSecurityMapper.listWiu(wainReqVo);
        String year = String.valueOf(LocalDate.now().getYear());
        if (!CollectionUtils.isEmpty(list)) {
            List<String> codeList = list.stream().map(WiuResVo::getWiuCode).collect(Collectors.toList());
            //取水户累计取水量和计划取水量
            List<WiuUseResVo> wiuUseList = waterSecurityMapper.listWiuUse(codeList, year);
            //取水量和计划取水量按取水户编码分类
            Map<String, WiuUseResVo> useMap = wiuUseList.stream()
                    .collect(Collectors.toMap(WiuUseResVo::getWiuCode, Function.identity(), (k1, k2) -> k1));

//            //获取最新一条数据
//            wiuUseList.forEach(wiuUse -> {
//                String code = wiuUse.getWiuCode();
//                if (!useMap.containsKey(code)) {
//                    useMap.put(code, wiuUse.getWatUse());
//                }
//            });

            //取水状态
            list.forEach(wiuResVo -> {
                String code = wiuResVo.getWiuCode();
                if (useMap.containsKey(code)) {
                    WiuUseResVo wiuUseResVo = useMap.get(code);
                    BigDecimal watPlan = wiuUseResVo.getWatPlan();
                    BigDecimal watUse = wiuUseResVo.getWatUse();
                    wiuResVo.setWatUse(watUse);
                    wiuResVo.setWatPlan(watPlan);
                    if (watPlan != null && watUse != null) {
                        double dWatPlan = watPlan.doubleValue();
                        double dWatUse = watUse.doubleValue();
                        if (dWatUse > dWatPlan) {
                            wiuResVo.setType(3);
                        } else if (dWatUse > wiuRate * dWatPlan) {
                            wiuResVo.setType(2);
                        } else {
                            wiuResVo.setType(1);
                        }
                    }
                }
            });
        }
        return list;
    }

    @Override
    public List<WainResVo> listWain(WainReqVo wainReqVo) {
        if (wainReqVo.getType() == 1) {
            wainReqVo.setAreaCode(trimAdcd(wainReqVo.getAreaCode()));
        }
        return waterSecurityMapper.listWain(wainReqVo);
    }

    @Override
    public List<SwhsResVo> listSwhs(WainReqVo wainReqVo) {
        if (wainReqVo.getType() == 1) {
            wainReqVo.setAreaCode(trimAdcd(wainReqVo.getAreaCode()));
        }
        return waterSecurityMapper.listSwhs(wainReqVo);
    }

    @Override
    public WiuBaseResVo wiuBase(String wiuCode) {
        //首先查一些基本信息
        WiuBaseResVo baseResVo = waterSecurityMapper.wiuBase(wiuCode);
        if (baseResVo != null) {
            //判断许可证是否有效
            Date now = new Date();
            Date intDl = baseResVo.getIntDl();
            if (intDl != null) {
                if (now.before(intDl)) {
                    baseResVo.setIsWptValid(true);
                } else {
                    baseResVo.setIsWptValid(false);
                }
            }

            String year = String.valueOf(LocalDate.now().getYear());
            List<String> list = Arrays.asList(wiuCode);
            //查出今年的累计取水数据和计划取水数据
            List<WiuUseResVo> wiuUseResVos = waterSecurityMapper.listWiuUse(list, year);
            if (!CollectionUtils.isEmpty(wiuUseResVos)) {
                WiuUseResVo wiuUseResVo = wiuUseResVos.get(0);
                baseResVo.setWatPlan(wiuUseResVo.getWatPlan());
                baseResVo.setWatUse(wiuUseResVo.getWatUse());
            }
//            baseResVo.setWatUseYear(waterSecurityMapper.listWiuUse(year, wiuCode));
//            //第一条减去最后一条得到年累计取水量
//            if (!CollectionUtils.isEmpty(useList)) {
//                WiuUse first = useList.get(0);
//                WiuUse last = useList.get(useList.size() - 1);
//                baseResVo.setWatUseYear(last.getWatUse().subtract(first.getWatUse()));
//            }
        }

        return baseResVo;
    }

    @Override
    public List<WiuPlan> wiuPlan(String wiuCode) {
        return waterSecurityMapper.wiuPlan(wiuCode);
    }

    @Override
    public WiuWptResVo wiuWpt(String wiuCode) {
        return waterSecurityMapper.wiuWpt(wiuCode);
    }

    @Override
    public List<WiuRecordResVo> wiuRecord(String wiuCode) {
        return waterSecurityMapper.wiuRecord(wiuCode);
    }

    @Override
    public List<WiuUse> wiuUse(WiuReqVo wiuReqVo) {
        return waterSecurityMapper.wiuUse(wiuReqVo);
    }

    @Override
    public WainBaseResVo wainBase(String wainCode) {
        return waterSecurityMapper.wainBase(wainCode);
    }

    @Override
    public SwhsBaseResVo swhsBase(String swhsCode) {
        SwhsBaseResVo swhsBaseResVo = waterSecurityMapper.swhsBase(swhsCode);
        if (swhsBaseResVo != null) {
            //加上标示标牌
            List<String> fileIdList = waterSecurityMapper.listSwhsFile(swhsCode);
            swhsBaseResVo.setFileList(fileIdList);
        }
        return swhsBaseResVo;
    }

    @Override
    public KpakmaBaseResVo kpakmaBase(String kpakmaCode) {
        //基本信息
        KpakmaBaseResVo kpakmaBaseResVo = waterSecurityMapper.kpakmaBase(kpakmaCode);
        //实时信息
        KpakmaBaseResVo realTime = waterSecurityMapper.listKpakmaLoss(kpakmaCode, LocalDate.now().getYear());
        if (realTime != null) {
            kpakmaBaseResVo.setKpakmaArea(realTime.getKpakmaArea());
            kpakmaBaseResVo.setKpakmaDegree(realTime.getKpakmaDegree());
        }
        return kpakmaBaseResVo;
    }
}
