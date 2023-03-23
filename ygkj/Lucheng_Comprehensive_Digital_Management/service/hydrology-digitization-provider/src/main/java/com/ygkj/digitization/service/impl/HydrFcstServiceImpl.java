package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.mapper.AttHydrFcstBaseMapper;
import com.ygkj.digitization.service.HydrFcstService;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.model.AttHydrFcstBase;
import com.ygkj.digitization.model.AttHydrFcstValBase;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.HydrFcstQueryVo;
import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.digitization.vo.response.HydrFcstResVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Service
public class HydrFcstServiceImpl implements HydrFcstService {

    @Resource
    GenerateDistributionIDService idService;

    @Resource
    AttHydrFcstBaseMapper hydrFcstBaseMapper;

    @Resource
    AttStBaseMapper stBaseMapper;

    /**
     * 查询水文预报列表
     *
     * @param queryVo
     * @return
     */
    @Override
    public PageVO<HydrFcstResVo> pageHydrFcst(HydrFcstQueryVo queryVo) {
        if (queryVo.getReleaseDate()!=null){
            queryVo.setReleaseStartTm(DateUtils.clearDateTimePrecision(queryVo.getReleaseDate(),Calendar.HOUR_OF_DAY,Calendar.MINUTE,Calendar.SECOND,Calendar.MILLISECOND));
            queryVo.setReleaseEndTm(new Date(queryVo.getReleaseStartTm().getTime()+24*3600*1000));
        }
        if (queryVo.isPaged()) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        PageVO<HydrFcstResVo> result = PageUtils.getPageVO(this.hydrFcstBaseMapper.selectFcstByCondition(queryVo));
        return result;
    }

    /**
     * 新增或更新水文预报
     *
     * @param resVo
     * @return
     */
    @Override
    public CommonResult addOrUpdateHydrFcst(HydrFcstResVo resVo) {

        if (StringUtils.isBlank(resVo.getStCode()) || resVo.getReleaseTime() == null) {
            return CommonResult.failed("入参为空");
        }
        AttStBase station = this.stBaseMapper.load(resVo.getStCode());
        if (station == null) {
            return CommonResult.failed("测站不存在");
        }
        resVo.setStType(station.getStType());
        resVo.setStName(station.getStName());
        resVo.setStType(station.getStType());

        maxLevel(resVo.getHydrFcstValList()).ifPresent(o -> {
            resVo.setPeakLevel(o.getLevel());
            resVo.setPeakLevelTime(o.getTm());
        });
        maxFlow(resVo.getHydrFcstValList()).ifPresent(o -> {
            resVo.setPeakFlow(o.getLevel());
            resVo.setPeakFlowTime(o.getTm());
        });
        if (StringUtils.isNotBlank(resVo.getId())) {
            updateFcst(resVo);
        } else {
            addFcst(resVo);
        }
        return CommonResult.success();
    }

    public void addFcst(HydrFcstResVo resVo) {
        String fcstId = idService.nextID();
        resVo.setId(fcstId);
        hydrFcstBaseMapper.insert(resVo);
        List<AttHydrFcstValBase> hydrFcstValList = resVo.getHydrFcstValList();
        if (CollectionUtils.isNotBlank(hydrFcstValList)) {
            hydrFcstValList.stream().filter(o -> o.getTm() != null).forEach(o -> {
//                try {
                o.setFcstId(fcstId);
                o.setStCode(resVo.getStCode());
                hydrFcstBaseMapper.replaceVal(o);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            });
        }
    }

    public void updateFcst(HydrFcstResVo resVo) {
        String fcstId = resVo.getId();
        hydrFcstBaseMapper.update(resVo);
        List<AttHydrFcstValBase> hydrFcstValList = resVo.getHydrFcstValList();
        if (CollectionUtils.isBlank(hydrFcstValList)) {
            this.hydrFcstBaseMapper.deleteValByByFcstId(fcstId, null);
        } else {
            // 先把要删除的查出来删掉
            List<Date> existTmList = this.hydrFcstBaseMapper.selectValTmByFcstId(fcstId);
            if (CollectionUtils.isNotBlank(existTmList)) {
                Map<Date, AttHydrFcstValBase> paramMap = hydrFcstValList.stream().filter(o -> o.getTm() != null).collect(Collectors.toMap(AttHydrFcstValBase::getTm, Function.identity()));
                List<Date> paramTmList = new ArrayList<>(paramMap.keySet());
                List<Date> deleteTmList = CollectionUtils.getExcept(existTmList, paramTmList);
                if (CollectionUtils.isNotBlank(deleteTmList)) {
                    this.hydrFcstBaseMapper.deleteValByByFcstId(fcstId, deleteTmList);
                }
            }
            hydrFcstValList.stream().filter(o -> o.getTm() != null).forEach(o -> {
//                try {
                o.setFcstId(fcstId);
                o.setStCode(resVo.getStCode());
                hydrFcstBaseMapper.replaceVal(o);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            });
        }
    }

    public Optional<AttHydrFcstValBase> maxLevel(List<AttHydrFcstValBase> hydrFcstValList) {
        if (CollectionUtils.isBlank(hydrFcstValList)) {
            return Optional.empty();
        }
        return hydrFcstValList.stream().filter(o -> o.getLevel() != null).max(Comparator.comparing(AttHydrFcstValBase::getLevel));
    }

    public Optional<AttHydrFcstValBase> maxFlow(List<AttHydrFcstValBase> hydrFcstValList) {
        if (CollectionUtils.isBlank(hydrFcstValList)) {
            return Optional.empty();
        }
        return hydrFcstValList.stream().filter(o -> o.getFlow() != null).max(Comparator.comparing(AttHydrFcstValBase::getFlow));
    }

    /**
     * 根据id水文预报
     *
     * @param id
     * @return
     */
    @Override
    public HydrFcstResVo getHydrFcst(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        AttHydrFcstBase temp=this.hydrFcstBaseMapper.load(id);
        HydrFcstResVo result=new HydrFcstResVo();
        BeanUtils.copyProperties(temp,result);
        List<AttHydrFcstValBase> hydrFcstValList= this.hydrFcstBaseMapper.selectValByFcstId(id);
        result.setHydrFcstValList(hydrFcstValList);
        return result;
    }

    /**
     * 新增或更新水文预报
     *
     * @param id
     * @return
     */
    @Override
    public CommonResult deleteHydrFcst(String id) {
        if (StringUtils.isBlank(id)) {
            return CommonResult.failed("参数为空");
        }
        this.hydrFcstBaseMapper.deleteValByByFcstId(id, null);
        this.hydrFcstBaseMapper.delete(id);
        return CommonResult.success();
    }

    /**
     * 最新水文预报
     *
     * @return
     */
    @Override
    public List<HydrFcstResVo> newestHydrFcst() {
        // 1. 将水文预报根据测站分组排序，按发布时刻取最新的预报
        // 2. 根据测站编码查询测站信息
        List<HydrFcstResVo> result = this.hydrFcstBaseMapper.selectNewestFcst();
        if (CollectionUtils.isBlank(result)) {
            return result;
        }
        fillStationInfo(result);
        for (HydrFcstResVo hydrFcstResVo : result) {
            hydrFcstResVo.buildWarnState();
        }
        return result;
    }

    /**
     * 列出水文测站
     *
     * @param stType 水库：RR,河道：ZZ
     * @param stName
     * @return
     */
    @Override
    public List<AttStBase> listHydrStation(String stType, String stName) {
        return this.stBaseMapper.selectByConditionT(Builder.of(StationRequestVo::new)
                .with(StationRequestVo::setStationType,stType)
                .with(StationRequestVo::setStationName,stName)
                .build());
    }

    private void fillStationInfo(List<HydrFcstResVo> fcstList){
        List<AttStBase> stationList = this.stBaseMapper.selectByStCodeList(fcstList.stream()
                .map(HydrFcstResVo::getStCode)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList()));
        fillStationInfo(stationList,fcstList);
    }

    private void fillStationInfo(List<AttStBase> stationList,List<HydrFcstResVo> fcstList){
        if (CollectionUtils.isBlank(fcstList)||CollectionUtils.isBlank(stationList)){
            return;
        }
        Map<String,AttStBase> stationMap=stationList.stream().collect(Collectors.toMap(AttStBase::getStCode,Function.identity()));
        for (HydrFcstResVo hydrFcstResVo : fcstList) {
            AttStBase station=stationMap.get(hydrFcstResVo.getStCode());
            if (station==null){
                continue;
            }
            hydrFcstResVo.setStLat(station.getStLat());
            hydrFcstResVo.setStLong(station.getStLong());
            hydrFcstResVo.setWarningLevel(station.getWarningLevel());
            hydrFcstResVo.setGuaranteeLevel(station.getGuaranteeLevel());
            hydrFcstResVo.setFloodLevel(station.getFloodLevel());
            hydrFcstResVo.setLimitLevel(station.getLimitLevel());
        }
    }
}
