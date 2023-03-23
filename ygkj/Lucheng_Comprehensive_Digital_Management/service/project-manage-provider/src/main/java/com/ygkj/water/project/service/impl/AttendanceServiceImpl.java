package com.ygkj.water.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.project.model.AttendanceDataBase;
import com.ygkj.project.model.AttendancePersonnelBase;
import com.ygkj.project.vo.request.AttendanceReqVo;
import com.ygkj.project.vo.response.AttendanceDataResVo;
import com.ygkj.utils.*;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.enums.AttendanceApiEnum;
import com.ygkj.water.project.mapper.AttendanceDataBaseMapper;
import com.ygkj.water.project.mapper.AttendancePersonnelBaseMapper;
import com.ygkj.water.project.service.AttendanceService;
import com.ygkj.water.project.utils.AttendanceTokenUtil;
import com.ygkj.water.project.utils.ExportUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-07-05 14:43
 * @description
 */
@Service
public class AttendanceServiceImpl implements AttendanceService {


    @Resource
    private AttendanceDataBaseMapper attendanceMapper;
    @Resource
    private AttendanceTokenUtil attendanceTokenUtil;
    @Resource
    private AttendancePersonnelBaseMapper personnelBaseMapper;


    @Override
    public JSONObject attendanceCalendar(AttendanceReqVo reqVo) {

        String loginToken = attendanceTokenUtil.getLoginToken();
        Map<String,String> headers = new HashMap<>(2);
        headers.put("Authorization",loginToken);
        Map<String,Object> params = new HashMap<>(8);
        params.put("pageSize", reqVo.getPageSize());
        params.put("pageNum", reqVo.getPageNum());
        if (StringUtils.isNotBlank(reqVo.getYearMonth())){
            reqVo.setStartTime(reqVo.getYearMonth() + "-01 00:00:00");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate("yyyy-MM-dd 00:00:00",reqVo.getStartTime()));
            calendar.add(Calendar.MONTH,1);
            reqVo.setEndTime(DateUtils.format(calendar.getTime(),"yyyy-MM-dd 00:00:00"));
        }

        if (StringUtils.isNotBlank(reqVo.getStartTime())) {
            params.put("startTime", reqVo.getStartTime());
        }
        if (StringUtils.isNotBlank(reqVo.getEndTime())) {
            params.put("endTime", reqVo.getEndTime());
        }
        if (StringUtils.isNotBlank(reqVo.getCorporationName())) {
            params.put("corporationName", reqVo.getCorporationName());
        }
        if (StringUtils.isNotBlank(reqVo.getDepartmentName())) {
            params.put("departmentName", reqVo.getDepartmentName());
        }
        if (StringUtils.isNotBlank(reqVo.getName())) {
            params.put("name", reqVo.getName());
        }
        String result = HttpClientUtil.sendGet(AttendanceApiEnum.ATTENDANCE_DATA.getUrl(), headers, params, "UTF-8");
        if (StringUtils.isNotBlank(result)){
            return JSONObject.parseObject(result);
        }
        return null;
    }


    @Override
    public void exportAttendanceCalendar(HttpServletResponse response, AttendanceReqVo reqVo) {
        //设置分页为 1000 导出前1000 数据
        reqVo.setPageSize(1000);
        reqVo.setPageNum(1);
        JSONObject jsonObject = attendanceCalendar(reqVo);
        if (jsonObject.getInteger("code") == 1) {
            //请求成功
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray records = data.getJSONArray("records");
            List<AttendanceDataBase> bases = records.toJavaList(AttendanceDataBase.class);

            ExportUtils.exportExcel(bases,"考勤列表",
                    new String[]{"姓名","公司","部门","设备名称","考勤方式","温度","考勤时间","描述"},
                    new String[]{"name","corporationName","departmentName","deviceName","checkingType","temperature","attendanceTime","checkingDesc"},response);
        }

    }

    @Override
    public List<AttendanceDataBase> attendancePeople(AttendanceReqVo reqVo) {
        if (StringUtils.isBlank(reqVo.getYearMonth())){
            Calendar calendar = Calendar.getInstance();
            reqVo.setYear(calendar.get(Calendar.YEAR) + "");
            reqVo.setMonth((calendar.get(Calendar.MONTH) + 1) + "");
        }else {
            reqVo.setYear(reqVo.getYearMonth().substring(0,4));
            reqVo.setMonth(reqVo.getYearMonth().substring(5,7));
        }

        if (reqVo.getMonth().length() < 2){
            reqVo.setMonth("0" + reqVo.getMonth());
        }
        return  attendanceMapper.selectAttendaceData(reqVo);
    }

    @Override
    public List<JSONObject> curveAnalysis(AttendanceReqVo reqVo) {
        List<JSONObject> result = new LinkedList<>();
        if (StringUtils.isBlank(reqVo.getYearMonth())){
            Calendar calendar = Calendar.getInstance();
            reqVo.setYear(calendar.get(Calendar.YEAR) + "");
            reqVo.setMonth((calendar.get(Calendar.MONTH) + 1) + "");
        }else {
            reqVo.setYear(reqVo.getYearMonth().substring(0,4));
            reqVo.setMonth(reqVo.getYearMonth().substring(5,7));
        }

        if (reqVo.getMonth().length() < 2){
            reqVo.setMonth("0" + reqVo.getMonth());
        }
        List<AttendanceDataBase> list = attendanceMapper.curveAnalysis(reqVo);
        if (CollectionUtils.isNotBlank(list)){
            Map<Date, List<AttendanceDataBase>> map = list.stream().collect(Collectors.groupingBy(AttendanceDataBase::getAttendanceTime,TreeMap::new,Collectors.toList()));
            map.forEach((k,v)->{
                JSONObject object = new JSONObject();
                object.put("attendanceTime",DateUtils.format(k,"yyyy-MM-dd"));
                List<AttendanceDataBase> normalNum = v.stream().filter(o -> "体温正常".equals(o.getCheckingDesc())).collect(Collectors.toList());
                List<AttendanceDataBase> abnormalNum = v.stream().filter(o -> "体温异常".equals(o.getCheckingDesc())).collect(Collectors.toList());
                object.put("normalNum",normalNum.size());
                object.put("abnormalNum",abnormalNum.size());
                result.add(object);
            });
        }
        return result;
    }

    @Override
    public PageVO<AttendanceDataResVo> attendanceAnalysis(AttendanceReqVo reqVo) {

        if (StringUtils.isBlank(reqVo.getYearMonth())){
            Calendar calendar = Calendar.getInstance();
            reqVo.setYear(calendar.get(Calendar.YEAR) + "");
            reqVo.setMonth((calendar.get(Calendar.MONTH) + 1) + "");
        }else {
            reqVo.setYear(reqVo.getYearMonth().substring(0,4));
            reqVo.setMonth(reqVo.getYearMonth().substring(5,7));
        }

        if (reqVo.getMonth().length() < 2){
            reqVo.setMonth("0" + reqVo.getMonth());
        }
        // 工作日
        List<String> wolkdayInMonth = getWorkNowDayInMonth(Integer.parseInt(reqVo.getYear()), Integer.parseInt(reqVo.getMonth()));
        Integer wolkdayNum = wolkdayInMonth.size();
        //获取对应日期（月份）的考勤记录
        List<AttendanceDataResVo> analysis = attendanceMapper.findAnalysis(reqVo);
        //筛选名称
        if (StringUtils.isNotBlank(reqVo.getName())){
            analysis = analysis.stream().filter( a ->  a.getName().contains(reqVo.getName())).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(reqVo.getCorporationName())){
            analysis = analysis.stream().filter( a ->  a.getCorporationName().contains(reqVo.getCorporationName())).collect(Collectors.toList());
        }
        Map<String, List<AttendanceDataResVo>> listMap = analysis.stream().collect(Collectors.groupingBy(AttendanceDataResVo::getName));
        Set<String> keySet = listMap.keySet();
        List<AttendanceDataResVo> results = new ArrayList<>();
        for (String name : keySet) {
            List<AttendanceDataResVo> resVos = listMap.get(name);
            AttendanceDataResVo resVo = new AttendanceDataResVo();
            resVo.setName(name);
            BeanUtil.copyProperties(resVos.get(0),resVo);
            resVo.setAttendanceNum(resVos.size());
            resVo.setNeedNum(wolkdayNum);
            // ps: 如果是根据当前日期获取的，多加一个传参修改工具方法为计算到 日 的工作日有多少天即可
            resVo.setMissNum( (wolkdayNum - resVos.size()) > 0 ? (wolkdayNum - resVos.size()) : 0);
            results.add(resVo);
        }
        results = results.stream().sorted(Comparator.comparing(AttendanceDataResVo::getAttendanceNum).reversed()).collect(Collectors.toList());
        if (reqVo.isPaged()){
            results = reqVo.limitPage(results);
        }
        return PageUtils.getPageVO(results);
    }


    /**
     * 获取当月目前的所有工作日
     * @param year
     * @param month
     * @return
     */
    public List<String> getWorkNowDayInMonth(int year, int month) {
        List<String> list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        // 不设置的话默认为当年
        calendar.set(Calendar.YEAR, year);
        int daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);;
//        if (month == (calendar.get(Calendar.MONTH) + 1)){
//            // 当月当前天数
//            daySize = calendar.get(Calendar.DAY_OF_MONTH);
//        }else {
//            // 当月最大天数
//            daySize = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        }
        // 设置月份
        calendar.set(Calendar.MONTH, month - 1);
        // 设置为当月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        for (int i = 0; i < daySize; i++) {
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            //周末休息
            // 判断这是一个星期的第几天从而判断是否是周末
            if ((week == Calendar.SATURDAY || week == Calendar.SUNDAY)) {

            }else{
                // 得到当天是一个月的第几天
                list.add(year+"-"+month+"-"+calendar.get(Calendar.DAY_OF_MONTH));
            }
            //在第一天的基础上加1
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }


    @Override
    public List<String> attendanceCompany() {
        // 获取公司列表
        QueryWrapper<AttendancePersonnelBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("cname");
        List<AttendancePersonnelBase> personnelBases = personnelBaseMapper.selectList(queryWrapper);
        List<String> companyList = personnelBases.stream().map(AttendancePersonnelBase::getCname).collect(Collectors.toList());
        return companyList;
    }

    @Override
    public List<JSONObject> companyAnalysis(AttendanceReqVo reqVo) {
        if (StringUtils.isBlank(reqVo.getYearMonth())){
            Calendar calendar = Calendar.getInstance();
            reqVo.setYear(calendar.get(Calendar.YEAR) + "");
            reqVo.setMonth((calendar.get(Calendar.MONTH) + 1) + "");
            if (reqVo.getMonth().length() < 2){
                reqVo.setMonth("0" + reqVo.getMonth());
            }
            reqVo.setYearMonth(reqVo.getYear() + "-" + reqVo.getMonth());
        }
        //获取考勤公司数据
        QueryWrapper<AttendancePersonnelBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getCorporationName()),"cname",reqVo.getCorporationName());
        queryWrapper.isNotNull("cname");
        List<AttendancePersonnelBase> personnelBases = personnelBaseMapper.selectList(queryWrapper);
        Map<String, List<AttendancePersonnelBase>> cnameMap = personnelBases.stream().collect(Collectors.groupingBy(AttendancePersonnelBase::getCname));
        Set<String> keySet = cnameMap.keySet();

        List<JSONObject> result = new ArrayList<>();
        //根据考勤公司获取对应 参与考勤人数 以及 公司下有的考勤人员数量
        for (String cname : keySet) {
            JSONObject json = new JSONObject();
            json.put("cname",cname);
            List<AttendancePersonnelBase> bases = cnameMap.get(cname);
            List<String> jobNumbers = bases.stream().map(AttendancePersonnelBase::getJobNumber).collect(Collectors.toList());
            json.put("attendanceNum",bases.size());
            List<AttendanceDataBase> dataBases = attendanceMapper.selectList(Wrappers.lambdaQuery(AttendanceDataBase.class)
                    .like(AttendanceDataBase::getAttendanceTime,reqVo.getYearMonth())
                    .eq(AttendanceDataBase::getCorporationName, cname)
                    .in(AttendanceDataBase::getJobNumber,jobNumbers)
                    .groupBy(AttendanceDataBase::getJobNumber));
            json.put("joinAttendanceNum",dataBases.size());
            result.add(json);
        }
        return result;
    }


    @Override
    public List<AttendanceDataResVo> companySummary(AttendanceReqVo reqVo) {
        Calendar calendar = Calendar.getInstance();
        reqVo.setMonth( (calendar.get(Calendar.MONTH) + 1) +"" );
        reqVo.setYear(calendar.get(Calendar.YEAR) + "");
        PageVO<AttendanceDataResVo> pageVo = attendanceAnalysis(reqVo);
        List<AttendanceDataResVo> list = pageVo.getList();
        for (AttendanceDataResVo resVo : list) {
            //实际打卡日 与 应打卡日 不为空 且被除数不为 0
            if (ObjectUtils.isNotEmpty(resVo.getAttendanceNum()) && ObjectUtils.isNotEmpty(resVo.getNeedNum()) && resVo.getNeedNum() != 0){
                BigDecimal rate = BigDecimal.valueOf(resVo.getAttendanceNum()).divide(BigDecimal.valueOf(resVo.getNeedNum()), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
                //大于一百也设置为 100
                resVo.setAttendanceRate(rate.compareTo(BigDecimal.valueOf(100)) > 0 ? BigDecimal.valueOf(100): rate);
            }
        }
        return list;
    }
}
