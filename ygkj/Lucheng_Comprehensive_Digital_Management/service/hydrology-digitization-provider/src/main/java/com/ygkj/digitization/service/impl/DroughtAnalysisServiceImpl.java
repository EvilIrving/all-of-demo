package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.feign.ProjectManageClient;
import com.ygkj.digitization.mapper.DroughtAnalysisMapper;
import com.ygkj.digitization.service.DroughtAnalysisService;
import com.ygkj.digitization.service.WaterReportService;
import com.ygkj.digitization.service.WaterStatusReportService;
import com.ygkj.digitization.vo.request.DroughtReqVo;
import com.ygkj.digitization.vo.request.RainfallReqVo;
import com.ygkj.digitization.vo.request.StationRequestVo;
import com.ygkj.digitization.vo.response.DayRainResVo;
import com.ygkj.digitization.vo.response.DroughtResVo;
import com.ygkj.digitization.vo.response.RainfallResVo;
import com.ygkj.digitization.vo.response.StResVo;
import com.ygkj.project.vo.request.RsvrWatSrcQueryVo;
import com.ygkj.project.vo.response.RsvrStationWaterResVo;
import com.ygkj.project.vo.response.WatSrcResVo;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("droughtAnalysisService")
public class DroughtAnalysisServiceImpl implements DroughtAnalysisService {

    private final String normalDate = "2020-12-01";
    private static final String PP_NAME = "st_pptn_r_";
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

    @Resource
    private DroughtAnalysisMapper droughtAnalysisMapper;

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,-29);
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date date = calendar.getTime();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
    }

    /**
     * 时间段降雨量
     *
     * @param reqVo
     * @return
     */
    @Override
    public Map<String, List<RainfallResVo>> listRainfall(RainfallReqVo reqVo) {
        //获取所有雨量测站
        List<StResVo> stResVoList = droughtAnalysisMapper.listRainSt();
        Map<String, StResVo> map = new HashMap<>();
        List<String> stcdList = new ArrayList<>();
        stResVoList.forEach(stResVo -> {
            String stcd = stResVo.getStCode();
            map.put(stcd, stResVo);
            stcdList.add(stcd);
        });
        reqVo.setStcdList(stcdList);
        List<RainfallResVo> rainfallResVos = droughtAnalysisMapper.listRainfallNew(reqVo);
        //加上经纬度
        Map<String, List<RainfallResVo>> result = addLl(rainfallResVos, map);

        LocalDate maxDate = LocalDate.parse(reqVo.getStartDate(), dtf).minusDays(1);
        //拿到最大的日期
        for (String dateStr : result.keySet()) {
            LocalDate date = LocalDate.parse(dateStr, dtf);
            if (maxDate.isBefore(date)) {
                maxDate = date;
            }
            LocalDate.parse(dateStr, dtf);
        }

        //还有日期的数据没有的话就去实时表查
        if (!dtf.format(maxDate).equals(reqVo.getEndDate())) {
            reqVo.setStartDate(dtf.format(maxDate.plusDays(1)));
            result.putAll(listRainfallMissing(reqVo, map));
        }

        return result;
    }

    /**
     * 时间段降雨量
     *
     * @param reqVo
     * @return
     */
    private Map<String, List<RainfallResVo>> listRainfallMissing(RainfallReqVo reqVo, Map<String, StResVo> map) {
        String startDate = reqVo.getStartDate();
        String endDate = reqVo.getEndDate();
        //计算需要哪几个月份的数据
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate end = LocalDate.parse(endDate, dtf).plusDays(1);
        endDate = dtf.format(end);
        List<String> month = getMonth(startDate, endDate);
//        //获取所有雨量测站
//        List<StResVo> stResVoList = droughtAnalysisMapper.listRainSt();
//        Map<String, StResVo> map = new HashMap<>();
//        List<String> stcdList = new ArrayList<>();
//        stResVoList.forEach(stResVo -> {
//            String stcd = stResVo.getStCode();
//            map.put(stcd, stResVo);
//            stcdList.add(stcd);
//        });
        Map<String, Object> param = new HashMap<>();
        param.put("stcdList", reqVo.getStcdList());
        param.put("tableList", month);
        param.put("startTime", startDate + " 08:00:00");
        param.put("endTime", endDate + " 07:59:59");
        //获取这段时间内的降雨量
        List<RainfallResVo> list = droughtAnalysisMapper.listRainfall(param);
        Map<String, List<RainfallResVo>> result = new HashMap<>();

        return addLl(list, map);
    }


    private Map<String, List<RainfallResVo>> addLl(List<RainfallResVo> list, Map<String, StResVo> map) {
        Map<String, List<RainfallResVo>> result = new HashMap<>();
        list.forEach(rainfallResVo -> {
            String stcd = rainfallResVo.getStcd();
            if (map.containsKey(stcd)) {
                List<RainfallResVo> rainfallResVos = result.computeIfAbsent(rainfallResVo.getDate(), k -> new ArrayList<>());
                StResVo stResVo = map.get(stcd);
                rainfallResVo.setStLong(stResVo.getStLong());
                rainfallResVo.setStLat(stResVo.getStLat());
                rainfallResVos.add(rainfallResVo);
            }
        });

        return result;
    }


    private List<String> getMonth(String startDateStr, String endDateStr) {
        try {
            Date startDate = new SimpleDateFormat("yyyy-MM").parse(startDateStr);
            Date endDate = new SimpleDateFormat("yyyy-MM").parse(endDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            // 获取开始年份和开始月份
            int startYear = calendar.get(Calendar.YEAR);
            int startMonth = calendar.get(Calendar.MONTH);
            // 获取结束年份和结束月份
            calendar.setTime(endDate);
            int endYear = calendar.get(Calendar.YEAR);
            int endMonth = calendar.get(Calendar.MONTH);
            //
            List<String> list = new ArrayList<>();
            for (int i = startYear; i <= endYear; i++) {
                String date = "";
                if (startYear == endYear) {
                    for (int j = startMonth; j <= endMonth; j++) {
                        if (j < 9) {
                            date = i + "0" + (j + 1);
                        } else {
                            date = i + "" + (j + 1);
                        }
                        list.add(PP_NAME + date);
                    }
                } else {
                    if (i == startYear) {
                        for (int j = startMonth; j < 12; j++) {
                            if (j < 9) {
                                date = i + "0" + (j + 1);
                            } else {
                                date = i + "" + (j + 1);
                            }
                            list.add(PP_NAME + date);
                        }
                    } else if (i == endYear) {
                        for (int j = 0; j <= endMonth; j++) {
                            if (j < 9) {
                                date = i + "0" + (j + 1);
                            } else {
                                date = i + "" + (j + 1);
                            }
                            list.add(PP_NAME + date);
                        }
                    } else {
                        for (int j = 0; j < 12; j++) {
                            if (j < 9) {
                                date = i + "0" + (j + 1);
                            } else {
                                date = i + "" + (j + 1);
                            }
                            list.add(PP_NAME + date);
                        }
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Scheduled(cron = "0 0 10 * * ?")
    public void countDayRainfall() {
        //查询表中当前时间,没有的话用默认起始时间
        String startDateStr = droughtAnalysisMapper.getLastDate();
        if (StringUtils.isEmpty(startDateStr)) {
            startDateStr = normalDate;
        }
        LocalDate startDate = LocalDate.parse(startDateStr, dtf);
        LocalDate now = LocalDate.now();

        //同步的日期如果小于当前日期就进行同步
        while (now.isAfter(startDate)) {
            LocalDate endDate = startDate.plusDays(1);
            String endDateStr = dtf.format(endDate);

            List<String> month = getMonth(startDateStr, endDateStr);
            Map<String, Object> param = new HashMap<>();
            param.put("tableList", month);
            param.put("startTime", startDate + " 08:00:00");
            param.put("endTime", endDate + " 07:59:59");

            //获取这段时间内的降雨量
            List<RainfallResVo> list = droughtAnalysisMapper.listRainfall(param);

            //数据入库
            if (!CollectionUtils.isEmpty(list)) {
                droughtAnalysisMapper.batchInsert(list);
            }

            startDate = endDate;
            startDateStr = endDateStr;
        }
    }


    @Autowired
    private WaterReportService waterReportService;

    @Autowired
    WaterStatusReportService waterStatusReportService;

    @Override
    public Object droughtWarning(DroughtReqVo reqVo) {

        if (StringUtils.isEmpty(reqVo.getQueryDate())){
            Calendar instance = Calendar.getInstance();
            reqVo.setQueryDate(instance.getTime());
        }
        List<DroughtResVo> resultArr = new ArrayList<>();

        //TODO 这个要改成鹿城自己的 可供水天数
//        resultArr.add(supplyWaterAbilite());
//        //可供水天数（乡村）
//        DroughtResVo resVo = supplyWaterDays(reqVo.getRuralSupplyDays());
//        resultArr.add(resVo);

        //近30日降水量
        DroughtResVo resVo1 = thirtyDaysPrecipitation();
        resultArr.add(resVo1);

        //连续无雨日数
        DroughtResVo noRainRes = getNoRainRes(reqVo.getAreaCode(), 0);
        resultArr.add(noRainRes);
//
//        //珊溪水库水位
//        DroughtResVo shanxi = shanxi();
//        resultArr.add(shanxi);
//
//        //中型供水水库蓄水率
//        DroughtResVo rsStorage = rsvrStorage(reqVo.getQueryDate());
//        resultArr.add(rsStorage);

        //降雨预报   TODO ： 原本这里是根据前端传值的，现在需要获取近七日降雨预报。暂无对应可使用接口
//        DroughtResVo rainReport = ranReport(reqVo.getWeatherForecast());
//        resultArr.add(rainReport);

//        //饮水困难人数
//        DroughtResVo drinkRes = drink(reqVo.getDrinkNum());
//        resultArr.add(drinkRes);
//
//        //供水紧张人数
//        resultArr.add(waterSupply(reqVo.getWaterSupplyNum()));




        return resultArr;
    }


    /**
     * 2）近30日降水量：指各县（市、区）近30日累计降水量距平百分率；- 目前即使温州市距平百分率（当前日往前推30天，并按月统计降雨量）降雨量
     * 当天不算进去，雨量都是一天一天计算(8到8)。
     * 距平百分率=降雨量-多年平均降雨量/多年平均降雨量*100%
     */
    private DroughtResVo thirtyDaysPrecipitation(){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("近30日降水量");

        StationRequestVo queryVo = new StationRequestVo();
        Calendar endInstance = Calendar.getInstance();
        Date endTime = endInstance.getTime();
        endInstance.add(Calendar.DATE,-30);
        Date beginTime = endInstance.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        queryVo.setStartTime(sdf.format(beginTime));
        queryVo.setEndTime(sdf.format(endTime));
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        Double rainAvarge;
        if (endTime.getMonth() < beginTime.getMonth()){
            rainAvarge = droughtAnalysisMapper.findRainAvarge(format.format(beginTime),format.format(endTime),false);
        }else {
            rainAvarge = droughtAnalysisMapper.findRainAvarge(format.format(beginTime), format.format(endTime),true);
        }
        //获取对应对年降雨平均值


        try {
            LinkedList<Map<String, Object>> maps = waterReportService.partitionRainfall(queryVo);
            if (!CollectionUtils.isEmpty(maps)){
                Double sum = 0.00;
                for (Map<String,Object> map: maps){
                    Double averageRain = (Double) map.get("averageRain");
                    sum += averageRain;
                }
                //计算平均值
                Double average = sum/maps.size();
                //多年降雨平均 从表中获取对应时间降雨值累加数得到 偏少40%～60%	偏少60%～80%	偏少80%～95% 	偏少超过95%
                Double flag = (average - rainAvarge)/rainAvarge;
                resVo.setAnalyzeResult(BigDecimal.valueOf(flag).multiply(BigDecimal.valueOf(100)).setScale(2,BigDecimal.ROUND_HALF_UP).toString());

                if (-flag > 0.4 && -flag <=0.6){
                    resVo.setWrState("blue");
                    resVo.setScope("偏少40%～60%");

                }else if (-flag >0.6 && -flag<=0.8){
                    resVo.setWrState("yellow");
                    resVo.setScope("偏少60%～80%");
                }else if (-flag >0.8 && -flag <= 0.95){
                    resVo.setWrState("orange");
                    resVo.setScope("偏少80%～95%");
                }else if (-flag > 0.95){
                    resVo.setWrState("red");
                    resVo.setScope("偏少超过95%");
                }else {
                    resVo.setWrState("normal");
                    resVo.setScope("正常");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resVo;
    }



    /**
     * 3）连续无雨日数：查询各县（市、区）面平均降水量<3mm的连续天数，根据查询数值判断色值。
     */
    private Integer noRainDayNum(String areaCode,Integer index){

        StationRequestVo queryVo = new StationRequestVo();
        Calendar endInstance = Calendar.getInstance();
        //从当前天开始往前统计
        endInstance.add(Calendar.DATE,-index);
        Date endTime = endInstance.getTime();
        //间隔一天计算一次
        endInstance.add(Calendar.DATE,-1);
        Date beginTime = endInstance.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        queryVo.setStartTime(sdf.format(beginTime));
        queryVo.setEndTime(sdf.format(endTime));
        queryVo.setAreaCode(areaCode);
        try {
            LinkedList<Map<String, Object>> maps = waterReportService.partitionRainfall(queryVo);
            if (!CollectionUtils.isEmpty(maps)){
                Double sum = 0.00;
                for (Map<String,Object> map: maps){
                    Double averageRain = (Double) map.get("averageRain");
                    sum += averageRain;
                }
                //计算平均值
                Double average = sum/maps.size();
                //降雨量大于3mm 或者无雨天数累计到了70天，退出返回天数
                if (average>=3 || index>=70){
                    return index;
                }else {
                    return noRainDayNum(areaCode,index+1);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public DroughtResVo getNoRainRes(String areaCode,Integer index){
        DroughtResVo resVo = new DroughtResVo();
        if (StringUtils.isEmpty(areaCode)){
            return resVo;
        }
        resVo.setIndex("连续无雨日数");
        resVo.setAreaCode(areaCode);
        Integer days = noRainDayNum(areaCode, index);
        if (!ObjectUtils.isEmpty(days)){
            resVo.setAnalyzeResult(days + "");
            if (days>=70){
                resVo.setScope("超过70天");
                resVo.setWrState("red");

            }else if (days >= 50){
                resVo.setScope("超过50天");
                resVo.setWrState("orange");

            }else if (days >= 40){
                resVo.setScope("超过40天");
                resVo.setWrState("yellow");

            }else if (days >= 30){
                resVo.setScope("超过30天");
                resVo.setWrState("blue");
            }else {
                resVo.setWrState("normal");
                resVo.setScope("正常");
            }
        }
        return resVo;
    }



    /**
     * 4）珊溪水库水位：查询当天珊溪水库坝上水位，根据查询数值判断色值。
     */
    public DroughtResVo shanxi(){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("珊溪水库水位");

        Calendar endInstance = Calendar.getInstance();
        Date endTime = endInstance.getTime();
        endInstance.add(Calendar.DATE,-1);
        Date beginTime = endInstance.getTime();
        List<LinkedHashMap> hashMaps = waterStatusReportService.rsvrWaterTimeInterval(beginTime, endTime, "珊溪水库");
        if (!CollectionUtils.isEmpty(hashMaps)){
            Set<String> set = hashMaps.get(0).keySet();
            for (String key: set){
                try {
                    if (!key.equals("resName")&& !key.equals("resCode") && !key.equals("areaName")){
                        LinkedHashMap linkedHashMap = hashMaps.get(0);
                        Double ral = (Double) linkedHashMap.get(key);

                        resVo.setAnalyzeResult(ral + "米");
                        if (ral<128.64){
                            //进行色值判断并且返回
                            //126.42～128.64米 蓝	124.07～126.42米 黄 	121.54～124.07米 橙	 低于121.54米 红
                            if (ral>=126.42){
                                resVo.setWrState("blue");
                                resVo.setScope("126.42~128.64米");

                            }else if (ral>=124.07){
                                resVo.setWrState("yellow");
                                resVo.setScope("124.07～126.42米");

                            }else if (ral>=121.54){
                                resVo.setWrState("orange");
                                resVo.setScope("121.54～124.07米");

                            }else {
                                resVo.setWrState("red");
                                resVo.setScope("低于121.54米");
                            }
                        }
                        return resVo;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return resVo;
    }


    private DroughtResVo drink(Integer drinkNum){
        //1～5万人	5～20万人	20～30万人	超过30万人
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("饮水困难人数");
        if (!ObjectUtils.isEmpty(drinkNum)){
            Integer num = drinkNum;
            resVo.setAnalyzeResult(num + "万人");
            if (num>1&& num<=5){
                resVo.setWrState("blue");
                resVo.setScope("1～5万人");

            }else if (num >5 && num <=20){
                resVo.setWrState("yellow");
                resVo.setScope("5～20万人");

            }else if (num >20 && num <=30){
                resVo.setWrState("orange");
                resVo.setScope("20～30万人");

            }else if (num > 30){
                resVo.setWrState("red");
                resVo.setScope("超过30万人");

            }
        }
        return resVo;
    }


    private DroughtResVo waterSupply(Integer supplyNum){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("供水紧张人数");
        if (!ObjectUtils.isEmpty(supplyNum)){
            Integer num = supplyNum;
            resVo.setAnalyzeResult(num+"万人");
            //20万人	20～50万人	50～100万人	超过100万人
            if (num<=20){
                resVo.setWrState("blue");
                resVo.setScope("20万人");

            }else if (num >20 && num <=50){
                resVo.setWrState("yellow");
                resVo.setScope("20～50万人");

            }else if (num >50 && num <=100){
                resVo.setWrState("orange");
                resVo.setScope("50～100万人");

            }else if (num > 100){
                resVo.setWrState("red");
                resVo.setScope("超过100万人");

            }
        }
        return resVo;
    }

    private DroughtResVo supplyWaterDays(Integer days){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("可供水天数（乡村）");
        if (!ObjectUtils.isEmpty(days)){
            Integer num = days;
            resVo.setAnalyzeResult(num+"天");
            if (num<=60 && num>45){
                resVo.setWrState("blue");
                resVo.setScope("60天");
            }else if (num >30 && num <=45){
                resVo.setWrState("yellow");
                resVo.setScope("45天");
            }else if (num >20 && num <=30){
                resVo.setWrState("orange");
                resVo.setScope("30天");
            }else if (num <= 20){
                resVo.setWrState("red");
                resVo.setScope("20天");
            }
        }
        return resVo;
    }



    /**
     *  50%～55%	45%～50%	35%～45%	小于35%
     * @param queryDate
     * @return
     */
    private DroughtResVo rsvrStorage(Date queryDate){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("中型供水水库蓄水率");
        Double flag = 0.00;
        List<RsvrStationWaterResVo> resVos = waterStatusReportService.rsvrStorageStats(queryDate);
        if (!CollectionUtils.isEmpty(resVos)){
            if (resVos.get(resVos.size()-1).getAreaName().equals("合计")){
                flag = resVos.get(resVos.size()-1).getStorageRatio();
                resVo.setAnalyzeResult(flag+"%");
                if (flag > 50 && flag <=55){
                    resVo.setWrState("blue");
                    resVo.setScope("50%～55%");

                }else if (flag >45 && flag <= 50){
                    resVo.setWrState("yellow");
                    resVo.setScope("45%～50%");

                }else if (flag >35 && flag <= 45){
                    resVo.setWrState("orange");
                    resVo.setScope("35%～45%");

                }else if (flag <=35){
                    resVo.setWrState("red");
                    resVo.setScope("小于35%");
                }
            }
        }
        return resVo;
    }


    private DroughtResVo ranReport(String ranReport){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("降雨预报");
        resVo.setAnalyzeResult(ranReport);

        return resVo;
    }

    @Autowired
    private ProjectManageClient projectManageClient;

    private static final String res = "珊溪水库,吴家园水库,桥墩水库,钟前水库,淡溪水库,顺溪水库,泽雅水库";

    private DroughtResVo supplyWaterAbilite(){
        DroughtResVo resVo = new DroughtResVo();
        resVo.setIndex("可供水天数（城镇）");
        Integer days = 0;
        List<String> split = Arrays.asList(res.split(","));
        for (String resName:split){
            RsvrWatSrcQueryVo queryVo = new RsvrWatSrcQueryVo();
            queryVo.setWatSrcName(resName);
            CommonResult<List<WatSrcResVo>> commonResult = projectManageClient.rsvrWatSrcTable(queryVo);
            if (commonResult.getCode() == CommonCode.SUCCESS.code()){
                List<WatSrcResVo> data = JSONArray.parseArray(JSONArray.toJSONString(commonResult.getData()), WatSrcResVo.class);
//                List<WatSrcResVo> data = commonResult.getData();
                if (!CollectionUtils.isEmpty(data)){
                    WatSrcResVo watSrcResVo = data.get(0);
                    Integer supplyAbility = watSrcResVo.getSupplyAbility();
                    days += (ObjectUtils.isEmpty(supplyAbility)?0:supplyAbility);
                }
            }
        }
        days = days / 7 ;
        resVo.setAnalyzeResult(days+"天");
        //90天（城镇，下同）	60天	45天	30天
        if (days <= 30){
            resVo.setWrState("red");
            resVo.setScope("30天");
        }else if (days > 30 && days <= 45){
            resVo.setWrState("orange");
            resVo.setScope("45天");
        }else if (days >45 && days <= 60){
            resVo.setWrState("yellow");
            resVo.setScope("60天");
        }else if (days > 60 && days <= 90){
            resVo.setWrState("blue");
            resVo.setScope("90天");
        }
        return resVo;
    }


    @Override
    public Object getDroughtStandard() {

        List<JSONObject> jsonObjects = droughtAnalysisMapper.selectDroughtStandard();
        return jsonObjects;
    }

    /**
     * 近三十日降雨量
     *
     * @return
     */
    @Override
    public List<DayRainResVo> listDayRainfall() {
        List<String> tables = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String endTable = PP_NAME + sdf.format(date);
        calendar.add(Calendar.DAY_OF_YEAR,-29);
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        date = calendar.getTime();
        String startTable = PP_NAME + sdf.format(date);
        if(!endTable.equals(startTable)){
            tables.add(startTable);
        }
        tables.add(endTable);
        //获取参与面雨量计算的测站
        List<String> stList = droughtAnalysisMapper.listSurfaceSt();
        BigDecimal size = new BigDecimal(stList.size());

        //获取每日的总降雨量
        List<DayRainResVo> rainList = droughtAnalysisMapper.list30DayRain(tables,date,stList);
        if(!CollectionUtils.isEmpty(rainList)){
            for (DayRainResVo dayRainResVo:rainList){
                //计算面雨量
                if(dayRainResVo.getDrp() > 0){
                    dayRainResVo.setDrp(new BigDecimal(dayRainResVo.getDrp()).divide(size,2,BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        }

        return rainList;
    }

}
