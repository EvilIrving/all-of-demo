package com.ygkj.river.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.river.mapper.AttAdcdBaseMaper;
import com.ygkj.river.mapper.AttWaterAreaMapper;
import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.service.AttBlueProjectService;
import com.ygkj.river.service.AttWaterAreaService;
import com.ygkj.river.service.AttWaterareaCompensationService;
import com.ygkj.river.vo.request.AttBlueProjectRecVo;
import com.ygkj.river.vo.request.AttWaterAreaReqVo;
import com.ygkj.river.vo.response.*;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author hucong
 * @description 水域面积表
 * @date 2021-08-12
 */
@Service
@Slf4j
public class AttWaterAreaServiceImpl implements AttWaterAreaService {

    @Resource
    private AttWaterAreaMapper attWaterAreaMapper;
    @Resource
    private AttWaterareaCompensationService attWaterareaCompensationService;
    @Resource
    private AttAdcdBaseMaper attAdcdBaseMaper;
    @Resource
    private AttBlueProjectService attBlueProjectService;

    @Override
    public OverallStatisticsWaterArea overallStatisticsWaterArea(Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        }
        BigDecimal waterAreaTotal = attWaterAreaMapper.totalStatisticsWaterAreaByYear(year);
        //水域面积除以温州市总面积
        BigDecimal divide = waterAreaTotal.divide(new BigDecimal(12110 * 10000 + ""), 4, BigDecimal.ROUND_HALF_UP);
        return new OverallStatisticsWaterArea(waterAreaTotal, new BigDecimal(divide.doubleValue() * 100 + "").setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Override
    public Map<Integer, List<HistogramWaterArea>> histogramWaterArea() {
        List<Integer> years = attWaterAreaMapper.calYears();
        List<HistogramWaterArea> histogramWaterAreas = null;
        Map<Integer, List<HistogramWaterArea>> listMap = new LinkedHashMap<>();
        for (Integer year : years) {
            histogramWaterAreas = attWaterAreaMapper.histogramWaterArea(year);
            double total = histogramWaterAreas.stream().mapToDouble(e -> e.getWaterArea().doubleValue()).sum();//总面就
            histogramWaterAreas.add(new HistogramWaterArea("6", new BigDecimal(total + "")));
            listMap.put(year, histogramWaterAreas);
        }
        return listMap;
    }

    @Override
    public PageVO<AttWaterAreaRespVo> loadPage(AttWaterAreaReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<AttWaterAreaRespVo> list = attWaterAreaMapper.loadPage(vo);
        for (AttWaterAreaRespVo attWaterAreaRespVo : list) {
            BigDecimal waterArea = attWaterAreaRespVo.getWaterArea();
            BigDecimal divide = waterArea.divide(new BigDecimal(12110 * 10000 + ""), 4, BigDecimal.ROUND_HALF_UP);
            attWaterAreaRespVo.setWaterRate(new BigDecimal(divide.doubleValue() * 100 + "").setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return PageUtils.getPageVO(list);
    }

    @Override
    public PositionStatisticsWaterArea cPositionStatisticsWaterArea(Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        }
        BigDecimal waterAreaTotal = attWaterAreaMapper.totalStatisticsWaterAreaByYear(year);
        AttWaterareaCompensationRespVo vo = attWaterareaCompensationService.statisticsReclaimedAreaWater(year);
        if (vo == null) {
            return new PositionStatisticsWaterArea(waterAreaTotal, new BigDecimal("0.0"), new BigDecimal("0.0"), new BigDecimal("0.0"));
        }
        BigDecimal divide = waterAreaTotal.divide(new BigDecimal(12110 * 10000 + ""), 4, BigDecimal.ROUND_HALF_UP);
        return new PositionStatisticsWaterArea(waterAreaTotal, vo.getOccupiedArea(), vo.getCompensationArea(), new BigDecimal(divide.doubleValue() * 100 + "").setScale(2, BigDecimal.ROUND_HALF_UP));
    }

    @Override
    public List<AttWaterAreaRespVo> waterAreaEachAdministrative(Integer year) {
        if (year == null) {
            year = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        }
        List<AttWaterAreaRespVo> areaRespVos = attWaterAreaMapper.waterAreaEachAdministrative(year);
        if (!areaRespVos.isEmpty() && areaRespVos.size() >= 2) {
            areaRespVos.get(0).setValueType("3");
            areaRespVos.get(areaRespVos.size() - 1).setValueType("1");
        }
        return areaRespVos;
    }

    @Override
    public void UpdateWwaterBodyData(AttBlueProjectRecVo attBlueProject) {
        try {
            if (StringUtils.isBlank(attBlueProject.getAdcd())) {
                return;
            }
            String areaName = attAdcdBaseMaper.selectNameOfAdcd(attBlueProject.getAdcd());
            AttWaterAreaRespVo attWaterAreaRespVo = attWaterAreaMapper.findByAreaNameAndYear(areaName, DateUtils.format(new Date(), "yyyy"));
            if (attWaterAreaRespVo != null && StringUtils.isNotBlank(attBlueProject.getFilleArea()) && StringUtils.isNotBlank(attBlueProject.getOccupyWaterArea())) {
                BigDecimal waterArea = attWaterAreaRespVo.getWaterArea();
                attWaterAreaRespVo.setWaterArea(waterArea.add(new BigDecimal(attBlueProject.getFilleArea())
                        .subtract(new BigDecimal(attBlueProject.getOccupyWaterArea())).divide(new BigDecimal("1000000"))));
                attWaterAreaMapper.updateData(attWaterAreaRespVo);
            }
        } catch (Exception e) {
            log.error("水域面积更新失败");
        }

    }

    /**
     * 批后监管更新、删除数据的水域面积实时更新操作
     *
     * @param attBlueProject
     * @param type           类型 1：更新 2：删除
     * @param id             主键id
     */
    @Override
    public void UpdateWwaterBodyDataTwo(AttBlueProject attBlueProject, String type, String id) {
        try {
            if ("1".equals(type)) {//减去之前的，加上现在修改的
                AttBlueProject attBlueProjectDb = attBlueProjectService.findById(attBlueProject.getId());
                if (attBlueProjectDb == null) {
                    return;
                }
                String areaName = attAdcdBaseMaper.selectNameOfAdcd(attBlueProjectDb.getAdcd());
                AttWaterAreaRespVo attWaterAreaRespVo = attWaterAreaMapper.findByAreaNameAndYear(areaName, DateUtils.format(new Date(), "yyyy"));
                if (attWaterAreaRespVo != null && StringUtils.isNotBlank(attBlueProject.getFilleArea()) && StringUtils.isNotBlank(attBlueProject.getOccupyWaterArea())) {
                    BigDecimal waterArea = attWaterAreaRespVo.getWaterArea();
                    BigDecimal bd1 = waterArea.subtract(new BigDecimal(attBlueProjectDb.getFilleArea())
                            .subtract(new BigDecimal(attBlueProjectDb.getOccupyWaterArea())).divide(new BigDecimal("1000000")));//之前的

                    BigDecimal bd2 = bd1.add(new BigDecimal(attBlueProject.getFilleArea())
                            .subtract(new BigDecimal(attBlueProject.getOccupyWaterArea())).divide(new BigDecimal("1000000")));//现在的
                    attWaterAreaRespVo.setWaterArea(bd2);
                    attWaterAreaMapper.updateData(attWaterAreaRespVo);
                }
            } else {//减去之前新增或修改的数据
                AttBlueProject attBlueProjectDb = attBlueProjectService.findById(id);
                if (attBlueProjectDb == null) {
                    return;
                }
                String areaName = attAdcdBaseMaper.selectNameOfAdcd(attBlueProjectDb.getAdcd());
                AttWaterAreaRespVo attWaterAreaRespVo = attWaterAreaMapper.findByAreaNameAndYear(areaName, DateUtils.format(new Date(), "yyyy"));
                if (attWaterAreaRespVo != null && StringUtils.isNotBlank(attBlueProjectDb.getFilleArea()) && StringUtils.isNotBlank(attBlueProjectDb.getOccupyWaterArea())) {
                    BigDecimal waterArea = attWaterAreaRespVo.getWaterArea();
                    attWaterAreaRespVo.setWaterArea(waterArea.add(new BigDecimal(attBlueProjectDb.getOccupyWaterArea())
                            .subtract(new BigDecimal(attBlueProjectDb.getFilleArea())).divide(new BigDecimal("1000000"))));
                    attWaterAreaMapper.updateData(attWaterAreaRespVo);
                }
            }
        } catch (Exception e) {
            log.error("UpdateWwaterBodyDataTwo出现错误:{}", e.getMessage());
        }
    }
}
