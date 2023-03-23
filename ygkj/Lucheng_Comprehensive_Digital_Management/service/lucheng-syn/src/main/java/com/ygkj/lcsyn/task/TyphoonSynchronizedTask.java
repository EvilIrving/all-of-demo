package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.model.RealTyphoonData;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;
/*@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)*/
@Component
public class TyphoonSynchronizedTask implements InitializingBean {

    @Resource
    private BusinessMapper businessMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private SqlSessionFactory sqlSessionFactory;


    private CountDownLatch latch;

    private Map<String, List<RealTyphoonData>> typhoonMap;

    private List<String> yearList;

    private ThreadPoolExecutor executor;

    private static final String YEAR_JSON_URL_PREFIX =
            "http://data.istrongcloud.com/v2/data/complex/years.json?v=";

    private static final String TYPHOON_BASE_JSON_PREFIX = "http://data.istrongcloud.com/v2/data/complex/{}.json?v=";

    private static final String TYPHOON_DETAIL_JSON_PREFIX = "http://data.istrongcloud.com/v2/data/complex/{}.json?v=";

    private static final String TYPHOON_FEATURE_JSON_PREFIX = "https://datacenter.istrongcloud.com/data/contour/v2/";

    private CopyOnWriteArrayList<RealTyphoonData> updateList;

    private CopyOnWriteArrayList<RealTyphoonData> insertList;

    @Scheduled(cron = "0 0/15 * * * ?")
    public void execute(){
        if (!yearList.isEmpty()){
            for (String year : yearList) {
                executor.execute(new PersistenceTyphoonTask(year));
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
            BusinessMapper mapper = session.getMapper(BusinessMapper.class);
            if (!updateList.isEmpty()){
                for (RealTyphoonData realTyphoonData : updateList) {
                    mapper.updateTyphoon(realTyphoonData);
                }
                updateList.clear();
            }
            if (!insertList.isEmpty()){
                List<RealTyphoonData> collect = insertList.stream().filter(data -> data.getCode().startsWith(LocalDate.now().getYear()+"")).collect(Collectors.toList());
                if (!collect.isEmpty()){
//                    insertList.clear();
//                    return;
                    if (collect.size() > 300){
                        int index = 0;
                        while (index < collect.size()){
                            mapper.insertTyphoon(collect.subList(index,index+300>insertList.size()?insertList.size():index+300));
                            index += 300;
                        }
                    } else {
                        mapper.insertTyphoon(collect);
                    }
                    insertList.clear();
                }
                insertList.clear();
            }
            try {
                session.commit();
            }catch (Exception e){
                e.printStackTrace();
                session.rollback();
            }finally {
                session.clearCache();
                session.close();
                //更新持久化的记录
                typhoonMap = businessMapper.selectTyphoons().stream().collect(Collectors.groupingBy(data -> data.getYear()));
            }
        }
    }


    private class PersistenceTyphoonTask implements Runnable{

        private String year;

        private Map<String,List<RealTyphoonData>> map;

        public PersistenceTyphoonTask(String year) {
            this.year = year;
            List<RealTyphoonData> realTyphoonData = typhoonMap.get(year);
            if (null != realTyphoonData){
                map = realTyphoonData.stream().collect(Collectors.groupingBy(RealTyphoonData::getCode));
            }
        }

        @Override
        public void run() {
            //台风名录
            String url = parse("{", "}", TYPHOON_BASE_JSON_PREFIX, year);
            String basicJson = HttpClientUtil.httpGet(url);
            JSONArray array = JSONArray.parseArray(basicJson);
            for (Object o : array) {
                JSONObject jsonObject = (JSONObject) o;
                String code = jsonObject.getString("tfbh");
                if (null != map && map.containsKey(code)){
                    RealTyphoonData realTyphoonData = map.get(code).get(0);
                    if (jsonObject.getInteger("is_current") != 0 || StringUtils.isNotBlank(realTyphoonData.getAffectedArea())){
                        //台风还没结束，更新结束时间和路径
                        String endTime = jsonObject.getString("end_time");
                        realTyphoonData.setEndTime(endTime);
                        realTyphoonData.setLand(jsonObject.getString("land"));
                        //获取路径详情
                        url = parse("{", "}", TYPHOON_DETAIL_JSON_PREFIX, code);
                        String detailJson = HttpClientUtil.httpGet(url);
                        String points = JSONArray.parseArray(detailJson).getJSONObject(0).getString("points");
                        realTyphoonData.setPoints(points);
                        String feature = getCurrrentTyphoonFeature(code, endTime);
                        if (StringUtils.isNotBlank(feature)){
                            realTyphoonData.setFeature(feature);
                        }
                        updateList.add(realTyphoonData);
                    }
                } else {
                    url = parse("{", "}", TYPHOON_DETAIL_JSON_PREFIX, code);
                    String detailJson = HttpClientUtil.httpGet(url);
                    JSONArray detailArray = JSONArray.parseArray(detailJson);
                    if (!detailArray.isEmpty()){
                        JSONObject detailJsonObject = detailArray.getJSONObject(0);
                        String beginTime = detailJsonObject.getString("begin_time");
                        String endTime = detailJsonObject.getString("end_time");
                        RealTyphoonData realTyphoonData = new RealTyphoonData(
                                StringUtils.isEmpty(detailJsonObject.getString("name"))?detailJsonObject.getString("ename"):detailJsonObject.getString("name"),
                                beginTime, endTime,detailJsonObject.getString("land"),
                                code,detailJsonObject.getString("points"),beginTime == null?code.substring(0,4):beginTime.substring(0,4));
                        String feature = getCurrrentTyphoonFeature(code, endTime);
                        if (StringUtils.isNotBlank(feature)){
                            realTyphoonData.setFeature(feature);
                        }
                        insertList.add(realTyphoonData);
                    }
                }
            }
            latch.countDown();
        }
    }

    private String getCurrrentTyphoonFeature(String code,String endTime){
        if (StringUtils.isBlank(endTime) || StringUtils.isBlank(code)){
            return null;
        }
        String index = endTime.substring(5, 7).concat(endTime.substring(8, 10)).concat(endTime.substring(11, 13));
        String url = TYPHOON_FEATURE_JSON_PREFIX.concat(code).concat("/").concat(index).concat(".json?v=")
                .concat(String.valueOf(System.currentTimeMillis()));
        try {
            String result = HttpClientUtil.httpGet(url);
            return JSONObject.parseObject(result).getJSONObject("contour").toJSONString();
        } catch (Exception e){
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<RealTyphoonData> realTyphoonData = businessMapper.selectTyphoons();
        typhoonMap = realTyphoonData.stream().collect(Collectors.groupingBy(data -> data.getYear()));
        String yearJson = HttpClientUtil.httpGet(YEAR_JSON_URL_PREFIX + System.currentTimeMillis());
        JSONArray array = JSONArray.parseArray(yearJson);
        //209行空指针报错项目无法启动，原因array为空
        if (array !=null && array.size() > 0){
            yearList = array.stream().map(o -> ((JSONObject)o).getString("year")).collect(Collectors.toList());
            latch = new CountDownLatch(yearList.size());
            int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
            executor = new ThreadPoolExecutor(corePoolSize,100,600, TimeUnit.SECONDS
                    ,new ArrayBlockingQueue<>(array.size()-corePoolSize));
        }
        insertList = new CopyOnWriteArrayList<>();
        updateList = new CopyOnWriteArrayList<>();
        Assert.notNull(sqlSessionTemplate,"启动失败！请检查SqlSessionTemplate是否成功加载！");
        sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
    }

    public static String parse(String openToken, String closeToken, String text, Object... args) {
        if (args == null || args.length <= 0) {
            return text;
        }
        int argsIndex = 0;

        if (text == null || text.isEmpty()) {
            return "";
        }
        char[] src = text.toCharArray();
        int offset = 0;
        // search open token
        int start = text.indexOf(openToken, offset);
        if (start == -1) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    String value = (argsIndex <= args.length - 1) ?
                            (args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
                    builder.append(value);
                    offset = end + closeToken.length();
                    argsIndex++;
                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }
}
