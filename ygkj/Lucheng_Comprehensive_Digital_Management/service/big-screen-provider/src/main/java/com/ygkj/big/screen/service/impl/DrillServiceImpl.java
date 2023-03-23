package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.service.DrillService;
import com.ygkj.big.screen.mapper.*;
import com.ygkj.big.screen.vo.request.DrillCaseResVo;
import com.ygkj.big.screen.vo.response.UnitDataResVo;
import com.ygkj.common.notify.NotifyCenter;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.DateUtils;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author xq
 * @Description
 * @Date 2021/7/10
 */
@Service
public class DrillServiceImpl implements DrillService {

    @Autowired
    BsDrillCaseMapper drillCaseMapper;

    @Autowired
    BsDrillAffairMapper drillAffairMapper;

    @Autowired
    BsUnitDataIndexMapper unitDataIndexMapper;

    @Autowired
    BsUnitDataMapper unitDataMapper;

    @Autowired
    GenerateDistributionIDService idService;

    @Resource
    BsInfluentRelMapper influentRelMapper;

    @Override
    public CommonResult startDrillAffair(String caseId) {
        BsDrillCase drillCase = drillCaseMapper.load(caseId);
        if (drillCase == null) {
            CommonResult.failed("演练方案不存在");
        }
        String affairId = idService.nextID();
        drillAffairMapper.insert(Builder.of(BsDrillAffair::new)
                .with(BsDrillAffair::setDrillAffairId, affairId)
                .with(BsDrillAffair::setDrillCaseId, drillCase.getId())
                .with(BsDrillAffair::setBeginTime, new Date())
                .with(BsDrillAffair::setDrillState, 0)
                .build());
//         通知水库更新数据
//        NotifyCenter.publishEvent(new RsvrDrillCaseEvent(caseId));
        return CommonResult.success(affairId);
    }

    @Override
    public CommonResult closeDrillAffair(String affairId) {
        drillAffairMapper.update(Builder.of(BsDrillAffair::new)
                .with(BsDrillAffair::setDrillAffairId, affairId)
                .with(BsDrillAffair::setDrillState, 0)
                .build());
        return CommonResult.success(affairId);
    }

    @Override
    public List<DrillCaseResVo> listDrillCase(String unitName, Integer year, String caseType) {
        List<BsDrillCase> drillCaseList = drillCaseMapper.selectByCondition(Builder.of(BsDrillCase::new)
                .with(BsDrillCase::setUnitName, unitName)
                .with(BsDrillCase::setYear, year)
                .with(BsDrillCase::setCaseType, caseType)
                .build());
        if (CollectionUtils.isBlank(drillCaseList)) {
            return Collections.emptyList();
        }
        List<DrillCaseResVo> result = new ArrayList<>(drillCaseList.size());
        for (BsDrillCase drillCase : drillCaseList) {
            DrillCaseResVo temp = new DrillCaseResVo();
            BeanUtils.copyProperties(drillCase, temp);
            LocalDateTime start = DateUtils.date2LocalDateTime(drillCase.getStartTime());
            LocalDateTime end = DateUtils.date2LocalDateTime(drillCase.getEndTime());
            Duration duration = Duration.between(start, end);
            LocalDateTime futureStart = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).plusHours(1);
            LocalDateTime futureEnd = futureStart.plusHours(duration.toHours());
            temp.setFutureStartTime(DateUtils.localDateTime2Date(futureStart));
            temp.setFutureEndTime(DateUtils.localDateTime2Date(futureEnd));
            result.add(temp);
        }
        return result;
    }

    @Override
    public BsDrillCase getDrillCaseByAffairId(String affairId) {
        return drillCaseMapper.loadByAffairId(affairId);
    }

    @Override
    public String getDrillCaseIdByAffairId(String affairId) {
        return drillCaseMapper.loadIdByAffairId(affairId);
    }

    @Override
    public String getDrillData(String caseId, String unitName, String dataApi, String dataParam, Date timestamp, Integer dataType) {
        String dataId = unitDataIndexMapper.selectIdByCondition(Builder
                .of(BsUnitDataIndex::new)
                .with(BsUnitDataIndex::setCaseId, caseId)
                .with(BsUnitDataIndex::setUnitName, unitName)
                .with(BsUnitDataIndex::setDataApi, dataApi)
                .with(BsUnitDataIndex::setDataParam, dataParam)
                .with(BsUnitDataIndex::setTimeStamp, timestamp)
                .with(BsUnitDataIndex::setDataType, dataType)
                .build());
        if (StringUtils.isNotBlank(dataId)) {
            return unitDataMapper.loadDataValue(dataId);
        } else {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String addOrUpdateDrillData(Map<String, List<UnitDataResVo>> dataMap,
                                       Map<Date, String> effectPreGroupIdMap, Date dataTime) {
        Set<String> dataGroupSet = new HashSet<>();
        dataMap.forEach((key, values) -> {
            values.forEach(o -> {
                BsUnitDataIndex dataIndex = unitDataIndexMapper.selectByUniqueIndex(o.getCaseId(), o.getUnitName(),
                        o.getDataApi(), o.getDataParam(), o.getDataType(), o.getTimeStamp());
                if (dataIndex == null) {
                    unitDataIndexMapper.insert(o);
                    dataGroupSet.add(o.getGroupId());
                    unitDataMapper.insert(o.buildUnitDataObj());
                } else {
                    dataGroupSet.add(dataIndex.getGroupId());
                    o.setDataId(dataIndex.getDataId());
                    unitDataMapper.update(o.buildUnitDataObj());
                }

            });
        });
        if (dataGroupSet.size() > 1) {
            throw new RuntimeException("数据重复");
        }
        String infGroupId = new ArrayList<>(dataGroupSet).get(0);
        if (effectPreGroupIdMap != null) {
            String effectGroupId = effectPreGroupIdMap.get(dataTime);
            if (StringUtils.isNotBlank(effectGroupId)) {
                BsInfluentRel rel = buildInfluence(idService.nextID(), effectGroupId, infGroupId);
                influentRelMapper.insert(rel);
            }
        }
        return infGroupId;
    }

    private BsInfluentRel buildInfluence(String id, String groupId, String infGroupId) {
        BsInfluentRel result = new BsInfluentRel();
        result.setId(id);
        result.setGroupDataId(groupId);
        result.setInfGroupDataId(infGroupId);
        return result;
    }
}
