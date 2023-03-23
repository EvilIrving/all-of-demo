package com.ygkj.big.screen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.big.screen.feign.AuthenticationClient;
import com.ygkj.big.screen.mapper.AttAdcdBaseMapper;
import com.ygkj.big.screen.mapper.AttHystBaseMapper;
import com.ygkj.big.screen.service.HystService;
import com.ygkj.big.screen.mapper.HystStatisticalMapper;
import com.ygkj.big.screen.model.AttHystBase;
import com.ygkj.big.screen.model.HystStatistical;
import com.ygkj.big.screen.vo.request.HystReqVo;
import com.ygkj.big.screen.vo.request.HystStatisticalReqVo;
import com.ygkj.big.screen.vo.response.AttHystBaseResVo;
import com.ygkj.big.screen.vo.response.HystDropPoint;
import com.ygkj.big.screen.vo.response.RateResVo;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HystServiceImpl implements HystService {

    private AttHystBaseMapper attHystBaseMapper;
    private AttAdcdBaseMapper adcdBaseMapper;
    private HystStatisticalMapper hystStatisticalMapper;
    private AuthenticationClient authenticationClient;

    public HystServiceImpl(@Autowired AttHystBaseMapper attHystBaseMapper,
                           @Autowired AttAdcdBaseMapper adcdBaseMapper,
                           @Autowired HystStatisticalMapper hystStatisticalMapper,
                           @Autowired AuthenticationClient authenticationClient) {
        this.attHystBaseMapper = attHystBaseMapper;
        this.adcdBaseMapper = adcdBaseMapper;
        this.hystStatisticalMapper = hystStatisticalMapper;
        this.authenticationClient = authenticationClient;
    }

    @Override
    public Object countNumByType(Byte type) {
        // 1-容量 2-行政区划 3-开发方式
        List<AttHystBase> list = attHystBaseMapper.selectList(Wrappers.lambdaQuery(AttHystBase.class)
                .eq(AttHystBase::getDelFlag, 0).isNotNull(AttHystBase::getTotInsCap));
        Map<String, Object> result = new HashMap<>();
        if (type.equals((byte) 1)) {
            Long moreTenThousandCount = list.stream().filter(h -> h.getTotInsCap() > 10000 && h.getTotInsCap() <= 50000).count();
            Long moreThousandCount = list.stream().filter(h -> h.getTotInsCap() >= 1000 && h.getTotInsCap() <= 10000).count();
            Long lessThousandCount = list.stream().filter(h -> h.getTotInsCap() < 1000).count();
            result.put("moreTenThousand", moreTenThousandCount);
            result.put("moreThousand", moreThousandCount);
            result.put("lessThousand", lessThousandCount);
        } else if (type.equals((byte) 2)) {
            //获取 行政区划编码下的 水电站列表
            Map<String, List<AttHystBase>> collect = list.stream().filter(a -> a.getAreaCode() != null)
                    .collect(Collectors.groupingBy(AttHystBase::getAreaCode));
            Set<String> areaCodes = collect.keySet();
            for (String areaCode : areaCodes) {
                List<AttHystBase> hystBases = collect.get(areaCode);
                //行政区划更换名称
                String byAdcd = adcdBaseMapper.selectAdnmByAdcd(areaCode);
                result.put(StringUtils.isNotBlank(byAdcd) ? byAdcd : areaCode, hystBases.size());
            }
        } else {
            Map<String, List<AttHystBase>> collect = list.stream().filter(a -> a.getHystType() != null)
                    .collect(Collectors.groupingBy(AttHystBase::getHystType));
            Set<String> hystTypes = collect.keySet();
            for (String hystType : hystTypes) {
                List<AttHystBase> hystBases = collect.get(hystType);
                result.put(hystType, hystBases.size());
            }
        }

        return result;
    }

    @Override
    public Object rateOrderByMoon(Byte type, Integer moon, Integer year) {
        //达标率或者在线率排序取前三计算 1-达标率 2-在线率
        Calendar beginInstance = Calendar.getInstance();
        Calendar endInstance = Calendar.getInstance();
        if (ObjectUtils.isEmpty(moon)) {
            moon = beginInstance.get(Calendar.MONTH);
        }
        if (ObjectUtils.isEmpty(year)) {
            year = beginInstance.get(Calendar.YEAR);
        }
        beginInstance.set(year, moon, 1);
        endInstance.set(moon == 12 ? year + 1 : year, moon == 12 ? 1 : moon + 1, 1);
        //获取对应时间内 各个水电站 实时数据
        List<HystStatistical> list = hystStatisticalMapper.selectList(Wrappers.lambdaQuery(HystStatistical.class)
                .isNotNull(HystStatistical::getHystCode)
                .gt(HystStatistical::getRecTime, beginInstance.getTime())
                .lt(HystStatistical::getRecTime, endInstance.getTime()));
        Map<String, List<HystStatistical>> collect = list.stream().filter(h -> h.getHystCode() != null)
                .collect(Collectors.groupingBy(HystStatistical::getHystCode));
        List<RateResVo> rateList = new ArrayList<>();
        Set<String> hystCodes = collect.keySet();
        Map<String, Object> result = new HashMap<>(2);
        if (type.equals((byte) 1)) {
            //计算达标率
            for (String hystCode : hystCodes) {
                List<HystStatistical> hystStatisticals = collect.get(hystCode);
                Long count = hystStatisticals.stream().filter(a -> a.getMaxFlow() >= a.getAprvEcoFlowYear()).count();
                Double rate = CollectionUtils.isBlank(hystStatisticals) ? 0 : count.doubleValue() / hystStatisticals.size();
                RateResVo rateResVo = new RateResVo();
                //水电站编码替换成名称
                AttHystBase attHystBase = attHystBaseMapper.selectById(hystCode);
                rateResVo.setKey(ObjectUtils.isEmpty(attHystBase) ? hystCode : attHystBase.getHystName());
                rateResVo.setValue(rate);
                rateList.add(rateResVo);
            }
            //返回前三
            rateList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            List<RateResVo> returnList = rateList.size() > 3 ? rateList.subList(0, 3) : rateList;
            //计算总达标率
            Double sum = CollectionUtils.isBlank(rateList) ?
                    0 : rateList.stream().mapToDouble(RateResVo::getValue).sum() / rateList.size();
            result.put("standardArg", sum);
            result.put("standardTopThree", returnList);
        } else if (type.equals((byte) 2)) {
            //计算在线率
            for (String hystCode : hystCodes) {
                List<HystStatistical> hystStatisticals = collect.get(hystCode);
                Long count = hystStatisticals.stream().filter(a -> a.getEquipmentOnline().equals("1")).count();
                Double rate = CollectionUtils.isBlank(hystStatisticals) ? 0 : count.doubleValue() / hystStatisticals.size();
                RateResVo rateResVo = new RateResVo();
                //水电站编码替换成名称
                AttHystBase attHystBase = attHystBaseMapper.selectById(hystCode);
                rateResVo.setKey(ObjectUtils.isEmpty(attHystBase) ? hystCode : attHystBase.getHystName());
                rateResVo.setValue(rate);
                rateList.add(rateResVo);
            }
            //返回前三
            rateList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            List<RateResVo> returnList = rateList.size() > 3 ? rateList.subList(0, 3) : rateList;
            Double sum = CollectionUtils.isBlank(rateList) ?
                    0 : rateList.stream().mapToDouble(RateResVo::getValue).sum() / rateList.size();
            result.put("onlineArg", sum);
            result.put("onlineTopThree", returnList);
        }
        return result;
    }

    @Override
    public Object mainTotalNum() {

        //本月达标率 + 本月在线率
        Map<String, Object> result = (Map<String, Object>) rateOrderByMoon((byte) 1, null, null);
        Map<String, Object> add = (Map<String, Object>) rateOrderByMoon((byte) 2, null, null);
        result.putAll(add);
        List<AttHystBase> attHystBases = attHystBaseMapper.selectList(Wrappers.lambdaQuery(AttHystBase.class)
                .isNotNull(AttHystBase::getAreaCode).eq(AttHystBase::getDelFlag, 0));
        Map<String, List<AttHystBase>> collect = attHystBases.stream().collect(Collectors.groupingBy(AttHystBase::getAreaCode));
        List<List<AttHystBase>> lists = attHystBases.stream().collect(Collectors.groupingBy(AttHystBase::getAreaCode)).values().stream()
                .sorted((a, b) -> b.size() - a.size()).collect(Collectors.toList());
        if (CollectionUtils.isNotBlank(lists)) {
            if (CollectionUtils.isNotBlank(lists.get(0))) {
                String areaCode = lists.get(0).get(0).getAreaCode();
                List<AttHystBase> maxBaseList = collect.get(areaCode);
                result.put("maxNum", maxBaseList.size());
                String byAdcd = adcdBaseMapper.selectAdnmByAdcd(areaCode);
                result.put("maxAreaName", StringUtils.isNotBlank(byAdcd) ? byAdcd : areaCode);
            }
        }
        result.put("totalHyst", attHystBases.size());
        return result;
    }

    @Override
    public PageVO hystDetailListByCondition(HystReqVo reqVo) {
        QueryWrapper<AttHystBase> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(reqVo.getAreaCode())) {
            wrapper.eq("area_code", reqVo.getAreaCode()).eq("del_flag", 0);
        }
        if (StringUtils.isNotBlank(reqVo.getHystType())) {
            wrapper.eq("hyst_type", reqVo.getHystType());
        }
        if (StringUtils.isNotBlank(reqVo.getKey())) {
            wrapper.like("hyst_name", reqVo.getKey());
        }
        if (ObjectUtils.isNotEmpty(reqVo.getPageNum()) && ObjectUtils.isNotEmpty(reqVo.getPageSize())) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        List<AttHystBase> attHystBase = attHystBaseMapper.selectList(wrapper);
        Integer total = attHystBaseMapper.selectTotal(reqVo);
        Calendar beginInstance = Calendar.getInstance();
        Calendar endInstance = Calendar.getInstance();
        int moon = beginInstance.get(Calendar.MONTH);
        int year = beginInstance.get(Calendar.YEAR);
        beginInstance.set(year, moon, 1);
        endInstance.set(moon == 12 ? year + 1 : year, moon == 12 ? 1 : moon + 1, 1);
        List<AttHystBaseResVo> resultList = new ArrayList<>();
        for (AttHystBase base : attHystBase) {
            //获取本月 当条数据 的实时数据
            List<HystStatistical> statisticals = hystStatisticalMapper.selectList(Wrappers.lambdaQuery(HystStatistical.class)
                    .gt(HystStatistical::getRecTime, beginInstance.getTime())
                    .lt(HystStatistical::getRecTime, endInstance.getTime())
                    .eq(HystStatistical::getHystCode, base.getHystCode()));
            AttHystBaseResVo resVo = new AttHystBaseResVo();
            BeanUtils.copyProperties(base, resVo);
            if (CollectionUtils.isNotBlank(statisticals)) {
                resVo.setAprvEcoFlowYear(statisticals.get(0).getAprvEcoFlowYear());
            }
            //计算对应达标率
            Long standardCount = statisticals.stream().filter(a -> a.getMaxFlow() >= a.getAprvEcoFlowYear()).count();
            Double standardRate = CollectionUtils.isBlank(statisticals) ? 0 : standardCount.doubleValue() / statisticals.size();
            resVo.setStandardRate(standardRate);
            //计算对应在线率
            Long count = statisticals.stream().filter(a -> a.getEquipmentOnline().equals("1")).count();
            Double rate = CollectionUtils.isBlank(statisticals) ? 0 : count.doubleValue() / statisticals.size();
            resVo.setOnlineRate(rate);
            resultList.add(resVo);
        }
        PageVO<AttHystBaseResVo> pageVO = PageUtils.getPageVO(resultList);
        pageVO.setTotalRows(total.longValue());
        return pageVO;
    }


    @Override
    public Object hystDetailInfoByCode(String hystCode) {

        AttHystBase attHystBase = attHystBaseMapper.selectById(hystCode);
        String adnm = adcdBaseMapper.selectAdnmByAdcd(attHystBase.getAreaCode());
        attHystBase.setAdcd(StringUtils.isNotBlank(adnm) ? adnm : null);
        if (StringUtils.isNotBlank(attHystBase.getGovermentChargeId())) {
            SysUser sysUser = authenticationClient.detailInfo(attHystBase.getGovermentChargeId());
            attHystBase.setGovermentChargeId(sysUser.getUsername());
        }
        if (StringUtils.isNotBlank(attHystBase.getWaterDepartmentChargeId())) {
            SysUser sysUser = authenticationClient.detailInfo(attHystBase.getWaterDepartmentChargeId());
            attHystBase.setWaterDepartmentChargeId(sysUser.getUsername());
        }
        if (StringUtils.isNotBlank(attHystBase.getCompetentDepartmentChargeId())) {
            SysUser sysUser = authenticationClient.detailInfo(attHystBase.getCompetentDepartmentChargeId());
            attHystBase.setCompetentDepartmentChargeId(sysUser.getUsername());
        }
        if (StringUtils.isNotBlank(attHystBase.getManageDepartmentChargeId())) {
            SysUser sysUser = authenticationClient.detailInfo(attHystBase.getManageDepartmentChargeId());
            attHystBase.setManageDepartmentChargeId(sysUser.getUsername());
        }
        if (StringUtils.isNotBlank(attHystBase.getTechnologyChargeId())) {
            SysUser sysUser = authenticationClient.detailInfo(attHystBase.getTechnologyChargeId());
            attHystBase.setTechnologyChargeId(sysUser.getUsername());
        }
        if (StringUtils.isNotBlank(attHystBase.getPatrolChargeId())) {
            SysUser sysUser = authenticationClient.detailInfo(attHystBase.getPatrolChargeId());
            attHystBase.setPatrolChargeId(sysUser.getUsername());
        }

        return attHystBase;
    }

    @Override
    public Object getFlowCurve(String hystCode, String startTime, String endTime) {
        List<HystStatistical> hystStatisticals = new ArrayList<>();
        Date startDate = null;
        Date endDate = null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                startDate = sdf.parse(startTime);
                endDate = sdf.parse(endTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            Calendar calendar = Calendar.getInstance();
            endDate = calendar.getTime();
            calendar.add(Calendar.HOUR_OF_DAY,-24);
            startDate = calendar.getTime();
        }
        String tableName = getTableName(startDate);
        try {
            hystStatisticals = hystStatisticalMapper.selectRealTimeData(Builder.of(HystStatisticalReqVo::new)
                    .with(HystStatisticalReqVo::setStartDate,startDate)
                    .with(HystStatisticalReqVo::setEndDate,endDate)
                    .with(HystStatisticalReqVo::setHystCode,hystCode)
                    .with(HystStatisticalReqVo::setTableName,tableName)
                    .build());
        }catch (BadSqlGrammarException e){
            e.printStackTrace();
        }

        return hystStatisticals;
    }

    /**
     * 根据日期获取对应应该查询的实时表表名
     * @param time
     * @return
     */
    public String getTableName(Date time){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        String tableName = "hyst_statistical_" + calendar.get(Calendar.YEAR) + getSeason(calendar.get(Calendar.MONTH) + 1);

        return tableName;
    }

    /**
     * 获取当前季度
     * @param month
     * @return
     */
    private String getSeason(Integer month){
        if(month > 0 && month<4){
            month = 1;
        }else if (month > 3 && month <7){
            month = 2;
        }else if (month > 6 && month <10){
            month = 3;
        }else {
            month = 4;
        }
        return "0" + month ;
    }


    @Override
    public Object dropPointHyst() {

        List<HystDropPoint> dropPoints = attHystBaseMapper.selectDropPoint();
        return dropPoints;
    }
}
