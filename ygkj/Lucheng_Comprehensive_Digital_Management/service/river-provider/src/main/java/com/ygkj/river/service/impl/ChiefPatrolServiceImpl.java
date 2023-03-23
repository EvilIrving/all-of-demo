package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.entity.SingletonMap;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.RiverProblemMapper;
import com.ygkj.river.model.AttAdcdBase;
import com.ygkj.river.model.RiverProblem;
import com.ygkj.river.service.ChiefPatrolService;
import com.ygkj.river.vo.request.PatrolProblemQueryVo;
import com.ygkj.river.vo.response.ChiefPatrolProbStats;
import com.ygkj.utils.Builder;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/8/17
 */
@Service
public class ChiefPatrolServiceImpl implements ChiefPatrolService {
    @Resource
    RiverProblemMapper riverProblemMapper;
    @Resource
    AttAdcdBaseMaper adcdBaseMapper;

    // 轻微程度  0 轻微 1 一般问题 2重大问题
    Map<Integer, String> levelMap = Builder.of(HashMap<Integer, String>::new)
            .with(HashMap::put, 0, "轻微")
            .with(HashMap::put, 1, "一般问题")
            .with(HashMap::put, 2, "重大问题")
            .build();

    // 状态[1:未受理;2:处理中;3:已处理待评价;4:已处理已评价]
    Map<Integer, String> stateMap = Builder.of(HashMap<Integer, String>::new)
            .with(HashMap::put, 1, "未受理")
            .with(HashMap::put, 2, "处理中")
            .with(HashMap::put, 3, "已处理待评价")
            .with(HashMap::put, 4, "已处理已评价")
            .build();

    /**
     * 查询河长巡河信息
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<RiverProblem> queryPatrolProblem(PatrolProblemQueryVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageSize() != null)
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        return PageUtils.getPageVO(riverProblemMapper.selectByCondition(reqVo));
    }

    /**
     * 根据条件统计河长巡河问题
     *
     * @param reqVo
     * @return
     */
    @Override
    public ChiefPatrolProbStats statsByQueryCondition(PatrolProblemQueryVo reqVo) {
        ChiefPatrolProbStats result = new ChiefPatrolProbStats();
        List<SingletonMap<String, Long>> records = this.riverProblemMapper.countByLevelWithCondition(reqVo);
        Map<String, Long> recordMap = SingletonMap.list2Map(records);
        result.setLevel0Num(recordMap.getOrDefault("0", 0L));
        result.setLevel1Num(recordMap.getOrDefault("1", 0L));
        result.setLevel2Num(recordMap.getOrDefault("2", 0L));
        result.setTotalNum(this.riverProblemMapper.totalNum(reqVo));
        return result;
    }

    /**
     * 巡河问题统计
     *
     * @param type 1：根据行政区划统计；2. 根据处置状态统计；3. 根据问题程度统计；4.统计过去12个月每月问题数量曲线；5 大屏C位统计
     * @return
     */
    @Override
    public Object problemStats(int type) {
        switch (type) {
            case 1: {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                List<AttAdcdBase> adcdList = adcdBaseMapper.countyOfWenZhou();
                List<SingletonMap<String, Long>> records = this.riverProblemMapper.countByAreaCodeWithTimeInterval(DateUtils.localDateTime2Date(start), DateUtils.localDateTime2Date(now));
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = records.stream().collect(Collectors.toMap(SingletonMap::getKey, SingletonMap::getValue));
                for (AttAdcdBase adcd : adcdList) {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, adcd.getAdnm())
                            .with(SingletonMap::setValue, recordMap.getOrDefault(adcd.getAdcd(), 0L))
                            .build());
                }
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 2: {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                List<SingletonMap<String, Long>> records = this.riverProblemMapper.countByStateWithTimeInterval(DateUtils.localDateTime2Date(start), DateUtils.localDateTime2Date(now));
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = SingletonMap.list2Map(records);
                stateMap.forEach((level, name) -> {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, name)
                            .with(SingletonMap::setValue, recordMap.getOrDefault(level.toString(), 0L))
                            .build());
                });
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 3: {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                List<SingletonMap<String, Long>> records = this.riverProblemMapper.countByLevelWithCondition(Builder.of(PatrolProblemQueryVo::new)
                        .with(PatrolProblemQueryVo::setStartTime, DateUtils.localDateTime2Date(start))
                        .with(PatrolProblemQueryVo::setEndTime, DateUtils.localDateTime2Date(now))
                        .build());
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = SingletonMap.list2Map(records);
                levelMap.forEach((level, name) -> {
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, name)
                            .with(SingletonMap::setValue, recordMap.getOrDefault(level.toString(), 0L))
                            .build());
                });
                return result.stream().sorted(Comparator.comparing(SingletonMap<String, Long>::getValue).reversed()).collect(Collectors.toList());
            }
            case 4: {
                LocalDate now = LocalDate.now();
                LocalDate start = now.minusYears(1);
                List<SingletonMap<String, Long>> records = this.riverProblemMapper.countByMonthWithTimeInterval(DateUtils.localDate2Date(start), DateUtils.localDate2Date(now));
                List<SingletonMap<String, Long>> result = new ArrayList<>(records.size());
                Map<String, Long> recordMap = SingletonMap.list2Map(records);
                LocalDate temp = start;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM");
                while (!temp.isAfter(now)) {
                    String month = temp.format(dtf);
                    result.add(Builder.of(SingletonMap<String, Long>::new)
                            .with(SingletonMap::setKey, month)
                            .with(SingletonMap::setValue, recordMap.getOrDefault(month, 0L))
                            .build());
                    temp = temp.plusMonths(1);
                }
                return result;
            }
            case 5: {
                ChiefPatrolProbStats result = new ChiefPatrolProbStats();
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime start = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                List<SingletonMap<String, Long>> records = this.riverProblemMapper.countByLevelWithCondition(Builder.of(PatrolProblemQueryVo::new)
                        .with(PatrolProblemQueryVo::setStartTime, DateUtils.localDateTime2Date(start))
                        .with(PatrolProblemQueryVo::setEndTime, DateUtils.localDateTime2Date(now))
                        .build());
                Map<String, Long> recordMap = SingletonMap.list2Map(records);
                result.setUnHandledNum(recordMap.getOrDefault("1", 0L) + recordMap.getOrDefault("2", 0L));
                result.setHandledNum(recordMap.getOrDefault("3", 0L) + recordMap.getOrDefault("4", 0L));
                result.setTotalNum(result.getUnHandledNum() + result.getHandledNum());
                return result;
            }
            default:
                return Collections.emptyList();
        }
    }
}
