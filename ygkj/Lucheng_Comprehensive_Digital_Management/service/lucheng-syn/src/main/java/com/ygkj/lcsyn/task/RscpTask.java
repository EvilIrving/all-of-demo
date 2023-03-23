package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.mapper.AttSixchargeChangeRecordMapper;
import com.ygkj.lcsyn.mapper.BasicMapper;
import com.ygkj.lcsyn.model.*;
import com.ygkj.lcsyn.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = LcSynApplication.class)
public class RscpTask {

    @Value("${access.key}")
    private String key;

    @Value("${access.secret}")
    private String secret;

    @Value("${access.url}")
    private String url;

    @Resource
    private RestTemplate restTemplate;

    Map<String,String> wasRiverMap = new HashMap<String,String>(){{
        put("3350ee98-1c7b-11ea-8760-6c92bf66b51e","戍浦江");
        put("SZ330302000122","仰义直河");
        put("SZ330302000121","丰门河");
        put("SZ330301000007","卧旗大河");
        put("SZ330302000102","丰门河泰力路箱涵");
        put("SZ330301000001","广化河");
        put("SZ330301000008","勤奋河");
        put("SZ330301000012","上陡门浦");
        put("SZ330302000126","三十六村河");
        put("SZ330301000011","灰桥浦河");
        put("SZ330302000127","环山直河");
        put("SZ330301000058","环山河");
        put("SZ330302000099","上江沥河");
        put("SZ330302000100","秧子湫河");
        put("SZ330302000101","东江河");
    }};

    @Resource
    private BasicMapper basicMapper;

    @Autowired
    private AttSixchargeChangeRecordMapper recordMapper;

    @Scheduled(cron = "0 0 9 * * ?")
    @Async("asyncTaskExecutor")
    public void waga() {
        log.info("----------------------开始同步水闸信息----------------------");
        List<AttWagaBase> list = getListFromRscp(AttWagaBase.class, "/mgt/bm/project/synchronization");
        list = list.stream().distinct().collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(list)){
            list.forEach(attWagaBase -> {
                attWagaBase.setStartLat(attWagaBase.getEndLat());
                attWagaBase.setRiverName(wasRiverMap.get(attWagaBase.getWagaCode()));
            });
            String delSql = SqlUtil.delSql(AttWagaBase.class);
            List<String> insertSqlList = SqlUtil.batchInsertSql(list);
            basicMapper.del(delSql);
            for (String insertSql:insertSqlList){
                basicMapper.batchInsert(insertSql);
            }
        }
        log.info("----------------------结束同步水闸信息----------------------");
    }

    @Scheduled(cron = "0 20 17 * * ?")
    @Async("asyncTaskExecutor")
//    @Test
    public void sixCharge() {
        JSONArray dataMaintainPeopleSyns = new JSONArray();
        List<String> proCodes = new ArrayList<>();
        String token = getToken();
        String skUrl = "https://sk.zjwater.com/mgt/ml/waterdirectory/listJson?adcd=330302000000&type=水库&fetchAll=true&outsideToken=" + token;
        String get1 = HttpClientUtil.httpGet(skUrl);
        JSONObject object1 = JSONObject.parseObject(get1);
        JSONArray array1 = object1.getJSONArray("rows");
        if (array1 != null && array1.size() > 0){
            array1.forEach(o->proCodes.add(((JSONObject)o).getString("prcd")));
            dataMaintainPeopleSyns.addAll(array1);
        }
        String htUrl = "https://sk.zjwater.com/mgt/ml/seawalldirectory/listJson?adcd=330302000000&type=海塘&fetchAll=true&outsideToken=" + token;
        String get2 = HttpClientUtil.httpGet(htUrl);
        JSONObject object2 = JSONObject.parseObject(get2);
        JSONArray array2 = object2.getJSONArray("rows");
        if (array2 != null && array2.size() > 0){
            array2.forEach(o->proCodes.add(((JSONObject)o).getString("prcd")));
            dataMaintainPeopleSyns.addAll(array2);
        }
        String szUrl = "https://sk.zjwater.com/mgt/ml/sluicedirectory/listJson?adcd=330302000000&type=水闸&fetchAll=true&outsideToken=" + token;
        String get3 = HttpClientUtil.httpGet(szUrl);
        JSONObject object3 = JSONObject.parseObject(get3);
        JSONArray array3 = object3.getJSONArray("rows");
        if (array3 != null && array3.size() > 0){
            array3.forEach(o->proCodes.add(((JSONObject)o).getString("prcd")));
            array3.forEach(o -> {
                Integer projectScale = ((JSONObject) o).getInteger("projectScale");
                if (projectScale < 6){
                    dataMaintainPeopleSyns.add((JSONObject)o);
                }
            });
        }
        String dfUrl = "https://sk.zjwater.com/mgt/ml/dikedirectory/listJson?adcd=330302000000&type=堤防&fetchAll=true&outsideToken=" + token;
        String get4 = HttpClientUtil.httpGet(dfUrl);
        JSONObject object4 = JSONObject.parseObject(get4);
        JSONArray array4 = object4.getJSONArray("rows");
        if (array4 != null && array4.size() > 0){
            array4.forEach(o->proCodes.add(((JSONObject)o).getString("prcd")));
            array4.forEach(o -> {
                Integer projectScale = ((JSONObject) o).getInteger("projectScale");
                if (projectScale < 7){
                    dataMaintainPeopleSyns.add((JSONObject)o);
                }
            });
        }
        List<DataMaintainPeopleSyn> dataMaintainPeopleSyns1 = dataMaintainPeopleSyns.toJavaList(DataMaintainPeopleSyn.class);
        if (dataMaintainPeopleSyns1.size() > 0){
            basicMapper.delDataMaintainPeopleSyn();
            basicMapper.batchInsertDataMaintainPeopleSyn(dataMaintainPeopleSyns1);
        }
        log.info("----------------------开始同步六个责任人----------------------");
        List<AttSixchargeBase> list = getListFromRscp(AttSixchargeBase.class, "/mgt/temp/threepeople/synchronization");
        if(!CollectionUtils.isEmpty(list)){
            list = list.stream().filter(o -> proCodes.contains(o.getProjectCode())).collect(Collectors.toList());
            list.forEach(attSixchargeBase -> attSixchargeBase.setId(String.valueOf(SnowFlake.snowFlake.nextId())));
            //要把责任人有更新的记录下来
            //查询库中所有的记录
            List<AttSixchargeBase> attSixchargeBases = basicMapper.listAllSixCharge();
            Map<String, AttSixchargeBase> sixchargeBaseMap = attSixchargeBases.stream().collect(Collectors.toMap(AttSixchargeBase::getProjectCode, Function.identity()));
            for (AttSixchargeBase newData : list) {
                if (sixchargeBaseMap.containsKey(newData.getProjectCode())){
                    Date updateTime = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    if (newData.getUpdateTime() != null){
                        try {
                            updateTime = sdf.parse(newData.getUpdateTime());
                        }catch (Exception e){

                        }
                    }else {
                        updateTime = new Date();
                    }
                    AttSixchargeBase oldData = sixchargeBaseMap.get(newData.getProjectCode());
                    //比较发生变化的数据
                    if ((oldData.getCompetentDepartmentChargeName() == null && newData.getCompetentDepartmentChargeName() != null) ||
                            (oldData.getCompetentDepartmentChargeName() != null && newData.getCompetentDepartmentChargeName() == null) ||
                            (oldData.getCompetentDepartmentChargeName() != null && newData.getCompetentDepartmentChargeName() != null &&
                            (!oldData.getCompetentDepartmentChargeName().equals(newData.getCompetentDepartmentChargeName()) ||
                            !oldData.getCompetentDepartmentChargePhone().equals(newData.getCompetentDepartmentChargePhone()) ||
                            !oldData.getCompetentDepartmentChargeJob().equals(newData.getCompetentDepartmentChargeJob()) ||
                            !oldData.getCompetentDepartmentChargeDept().equals(newData.getCompetentDepartmentChargeDept())))){
                        AttSixchargeChangeRecord record = new AttSixchargeChangeRecord();
                        record.setType("主管部门负责人");
                        record.setProjectCode(newData.getProjectCode());

                        record.setChargeNameBefore(oldData.getCompetentDepartmentChargeName());
                        record.setChargePhoneBefore(oldData.getCompetentDepartmentChargePhone());
                        record.setChargeJobBefore(oldData.getCompetentDepartmentChargeJob());
                        record.setChargeDeptBefore(oldData.getCompetentDepartmentChargeDept());

                        record.setChargeNameAfter(newData.getCompetentDepartmentChargeName());
                        record.setChargePhoneAfter(newData.getCompetentDepartmentChargePhone());
                        record.setChargeJobAfter(newData.getCompetentDepartmentChargeJob());
                        record.setChargeDeptAfter(newData.getCompetentDepartmentChargeDept());

                        record.setChangeTime(updateTime);
                        recordMapper.insert(record);
                    }
                    if ((oldData.getManageDepartmentChargeName() == null && newData.getManageDepartmentChargeName() != null) ||
                            (oldData.getManageDepartmentChargeName() != null && newData.getManageDepartmentChargeName() == null) ||
                            (oldData.getManageDepartmentChargeName() != null && newData.getManageDepartmentChargeName() != null &&
                            (!oldData.getManageDepartmentChargeName().equals(newData.getManageDepartmentChargeName()) ||
                            !oldData.getManageDepartmentChargePhone().equals(newData.getManageDepartmentChargePhone()) ||
                            !oldData.getManageDepartmentChargeJob().equals(newData.getManageDepartmentChargeJob()) ||
                            !oldData.getManageDepartmentChargeDept().equals(newData.getManageDepartmentChargeDept())))){
                        AttSixchargeChangeRecord record = new AttSixchargeChangeRecord();
                        if ("有".equals(oldData.getIfDepartment())){
                            record.setType("管理单位责任人");
                        }else {
                            record.setType("物业单位管理责任人");
                        }
                        record.setProjectCode(newData.getProjectCode());

                        record.setChargeNameBefore(oldData.getManageDepartmentChargeName());
                        record.setChargePhoneBefore(oldData.getManageDepartmentChargePhone());
                        record.setChargeJobBefore(oldData.getManageDepartmentChargeJob());
                        record.setChargeDeptBefore(oldData.getManageDepartmentChargeDept());

                        record.setChargeNameAfter(newData.getManageDepartmentChargeName());
                        record.setChargePhoneAfter(newData.getManageDepartmentChargePhone());
                        record.setChargeJobAfter(newData.getManageDepartmentChargeJob());
                        record.setChargeDeptAfter(newData.getManageDepartmentChargeDept());

                        record.setChangeTime(updateTime);
                        recordMapper.insert(record);
                    }
                    if ((oldData.getPatrolChargeName() == null && newData.getPatrolChargeName() != null) ||
                            (oldData.getPatrolChargeName() != null && newData.getPatrolChargeName() == null) ||
                            (oldData.getPatrolChargeName() != null && newData.getPatrolChargeName() != null &&
                                    (!oldData.getPatrolChargeName().equals(newData.getPatrolChargeName()) ||
                                            !oldData.getPatrolChargeName().equals(newData.getPatrolChargeName()) ||
                                            !oldData.getPatrolChargeName().equals(newData.getPatrolChargeName()) ||
                                            !oldData.getPatrolChargeName().equals(newData.getPatrolChargeName())))){
                        AttSixchargeChangeRecord record = new AttSixchargeChangeRecord();
                        record.setType("巡查责任人");
                        record.setProjectCode(newData.getProjectCode());

                        record.setChargeNameBefore(oldData.getPatrolChargeName());
                        record.setChargePhoneBefore(oldData.getPatrolChargePhone());
                        record.setChargeJobBefore(oldData.getPatrolChargeJob());
                        record.setChargeDeptBefore(oldData.getPatrolChargeDept());

                        record.setChargeNameAfter(newData.getPatrolChargeName());
                        record.setChargePhoneAfter(newData.getPatrolChargePhone());
                        record.setChargeJobAfter(newData.getPatrolChargeJob());
                        record.setChargeDeptAfter(newData.getPatrolChargeDept());

                        record.setChangeTime(updateTime);
                        recordMapper.insert(record);
                    }
                    if ((oldData.getTechnologyChargeName() == null && newData.getTechnologyChargeName() != null) ||
                            (oldData.getTechnologyChargeName() != null && newData.getTechnologyChargeName() == null) ||
                            (oldData.getTechnologyChargeName() != null && newData.getTechnologyChargeName() != null) &&
                            (!oldData.getTechnologyChargeName().equals(newData.getTechnologyChargeName()) ||
                            !oldData.getTechnologyChargePhone().equals(newData.getTechnologyChargePhone()) ||
                            !oldData.getTechnologyChargeJob().equals(newData.getTechnologyChargeJob()) ||
                            !oldData.getTechnologyChargeDept().equals(newData.getTechnologyChargeDept()))){
                        AttSixchargeChangeRecord record = new AttSixchargeChangeRecord();
                        record.setType("技术责任人");
                        record.setProjectCode(newData.getProjectCode());

                        record.setChargeNameBefore(oldData.getTechnologyChargeName());
                        record.setChargePhoneBefore(oldData.getTechnologyChargePhone());
                        record.setChargeJobBefore(oldData.getTechnologyChargeJob());
                        record.setChargeDeptBefore(oldData.getTechnologyChargeDept());

                        record.setChargeNameAfter(newData.getTechnologyChargeName());
                        record.setChargePhoneAfter(newData.getTechnologyChargePhone());
                        record.setChargeJobAfter(newData.getTechnologyChargeJob());
                        record.setChargeDeptAfter(newData.getTechnologyChargeDept());

                        record.setChangeTime(updateTime);
                        recordMapper.insert(record);
                    }
                }
            }
            String delSql = SqlUtil.delSql(AttSixchargeBase.class);
            List<String> insertSqlList = SqlUtil.batchInsertSql(list);
            basicMapper.del(delSql);
            for (String insertSql:insertSqlList){
                basicMapper.batchInsert(insertSql);
            }
        }
        log.info("----------------------结束同步六个责任人----------------------");
    }

    private <T> List<T> getListFromRscp(Class<T> clazz, String path) {
        JSONObject jsonObject = restTemplate.getForObject(url + path + "?outsideToken=" + getToken(null), JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        if (!CollectionUtils.isEmpty(jsonArray)) {
            return JSONArray.parseArray(jsonArray.toString(), clazz);
        }
        return null;
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async("asyncTaskExecutor")
    public void facilities() {
        log.info("----------------------开始同步附属建筑物----------------------");
        List<BmPrcdAccprcd> list = facilitiesFromRscp();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("[附属建筑物数量]{}", list.size());
            basicMapper.delBmPrcdAccprcd();
            basicMapper.batchInsertBmPrcdAccprcd(list);
        }
        log.info("----------------------结束同步附属建筑物----------------------");
    }

    /**
     * 从工程运管查询附属建筑物
     *
     * @return
     */
    private List<BmPrcdAccprcd> facilitiesFromRscp() {
        JSONObject jsonObject = restTemplate.getForObject(url + "/mgt/bm/prcdaccprcd/synchronization?outsideToken=" + getToken(null), JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        if (!CollectionUtils.isEmpty(jsonArray)) {
            return JSONArray.parseArray(jsonArray.toString(), BmPrcdAccprcd.class);
        }
        return null;
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async("asyncTaskExecutor")
    public void cross() {
        log.info("----------------------开始同步交叉建筑物----------------------");
        List<BmAccBuild> list = crossFromRscp();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("[交叉建筑物数量]{}", list.size());
            basicMapper.delBmAccBuild();
            basicMapper.batchInsertBmAccBuild(list);
        }
        log.info("----------------------结束同步交叉建筑物----------------------");
    }

    /**
     * 从工程运管查询交叉建筑物
     *
     * @return
     */
    private List<BmAccBuild> crossFromRscp() {
        JSONObject jsonObject = restTemplate.getForObject(url + "/mgt/bm/accbuild/synchronization?outsideToken=" + getToken(null), JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        if (!CollectionUtils.isEmpty(jsonArray)) {
            return JSONArray.parseArray(jsonArray.toString(), BmAccBuild.class);
        }
        return null;
    }


    /**
     * 从工程运管同步海塘主体
     *
     * @return
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async("asyncTaskExecutor")
    public void syncSeawallStructinfo(){
        List<String> stringList = basicMapper.selectCode();
        for (String prcd : stringList) {
            JSONObject jsonObject = restTemplate.getForObject(url + "/mgt/lucheng/seawallstructinfo/list?outsideToken=" + getToken(prcd)+"&fetchAll=true", JSONObject.class);
            JSONArray jsonArray = jsonObject.getJSONArray("rows");
            if (!CollectionUtils.isEmpty(jsonArray)) {
                log.info("----------------------开始同步海塘主体----------------------");
                List<BmSeawallStructinfo> list = JSONArray.parseArray(jsonArray.toString(), BmSeawallStructinfo.class);
                if (!CollectionUtils.isEmpty(list)) {
                    log.info("[开始同步海塘主体]{}", list.size());
                    basicMapper.delBmSeawallStructinfo(prcd);
                    basicMapper.batchInsertBmSeawallStructinfo(list);
                }
                log.info("----------------------结束同步海塘主体----------------------");
            }
        }
    }

    /**
     * 从工程运管同步堤防
     *
     * @return
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    @Async("asyncTaskExecutor")
    public void syncDikeInfo(){

        JSONObject jsonObject = restTemplate.getForObject(url + "/mgt/ml/dikedirectory/listJson?outsideToken=" + getToken()+"&fetchAll=true&adcd=330302000000&type=堤防", JSONObject.class);
        JSONArray jsonArray = jsonObject.getJSONArray("rows");
        if (!CollectionUtils.isEmpty(jsonArray)) {
            log.info("----------------------开始同步堤防----------------------");
            List<AttDikeBase> list = JSONArray.parseArray(jsonArray.toString(), AttDikeBase.class);
            if (!CollectionUtils.isEmpty(list)) {
                log.info("[开始同步堤防]{}", list.size());
                for (AttDikeBase  base: list) {

                    basicMapper.batchUpdateDikeInfo(base);
                }
            }
            log.info("----------------------结束同步堤防----------------------");
        }

    }

    /**
     * 从工程运管同步物业合同
     *
     * @return
     */
    @Scheduled(cron = "0 0 0/2 * * ?")
    @Async("asyncTaskExecutor")
    public void syncContract(){
        String s = HttpClientUtil.httpGet("https://sk.zjwater.com/out/service/getDynamicToken");
        JSONObject tokenJson = JSONObject.parseObject(s);
        String token = tokenJson.getString("token");
        Map<String, String> urlMap = new HashMap<>();
        urlMap.put("水库","/mgt/ml/waterdirectory/listJson?adcd=330302000000");
        urlMap.put("海塘","/mgt/ml/seawalldirectory/listJson?adcd=330302000000");
        urlMap.put("水闸","/mgt/ml/sluicedirectory/listJson?adcd=330302000000");
        urlMap.put("堤防","/mgt/ml/dikedirectory/listJson?adcd=330302000000");
        Set<Map.Entry<String, String>> entries = urlMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            JSONObject projectJson = restTemplate.getForObject(url + entry.getValue() + "&type=" + entry.getKey() + "&token=" + token +"&fetchAll=true", JSONObject.class);
            JSONArray projectArray = projectJson.getJSONArray("rows");
            List<JSONObject> list1 = projectArray.toJavaList(JSONObject.class);
            for (JSONObject object : list1) {
                String prcd = object.getString("prcd");
                JSONObject jsonObject = restTemplate.getForObject(url + "/mgt/bm/propertycontract/listJson?token=" + token + "&fetchAll=true" + "&prcd=" + prcd, JSONObject.class);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");
                if (!CollectionUtils.isEmpty(jsonArray)) {
                    log.info("----------------------开始同步物业合同----------------------");
                    List<BmPropertyContract> list = JSONArray.parseArray(jsonArray.toString(), BmPropertyContract.class);
                    if (!CollectionUtils.isEmpty(list)) {
                        log.info("[开始同步物业合同]{}", list.size());
                        for (BmPropertyContract  base: list) {
                            JSONObject fileJson = restTemplate.getForObject(url + "/mgt/lookup/fileListJson?token=" + token+"&dataId=" + base.getContractFile(), JSONObject.class);
                            JSONArray fileArray = fileJson.getJSONArray("list");
                            if (fileArray.size() > 0){
                                List<JSONObject> fileList = fileArray.toJavaList(JSONObject.class);
                                JSONObject file  = fileList.get(0);
                                base.setContractFile(file.getString("filurl"));
                            }
                            base.setPrcd(prcd);
                            base.setSourceType("1");
                            basicMapper.insertContract(base);
                        }
                    }
                    log.info("----------------------结束同步物业合同----------------------");
                }
            }

        }
    }

//
//    /**
//     * 从工程运管同步海塘主体的断面图到附件表
//     *
//     * @return
//     */
//    @Scheduled(cron = "0 1 0/1 * * ?")
//    @Async("asyncTaskExecutor")
//    public void syncSeawallStructinfoSection(){
//        List<String> stringList = basicMapper.selectCode();
//        for (String prcd : stringList) {
//            JSONObject jsonObject = restTemplate.getForObject(url + "/mgt/lucheng/seawallstructinfo/list?outsideToken=" + getToken(prcd)+"&fetchAll=true", JSONObject.class);
//            JSONArray jsonArray = jsonObject.getJSONArray("rows");
//            if (!CollectionUtils.isEmpty(jsonArray)) {
//                log.info("----------------------开始同步海塘主体断面图----------------------");
//                List<BmSeawallStructinfo> list = JSONArray.parseArray(jsonArray.toString(), BmSeawallStructinfo.class);
//                if (!CollectionUtils.isEmpty(list)) {
//                    log.info("[开始同步海塘主体]{}", list.size());
//                    basicMapper.delBmSeawallStructinfo(prcd);
//                    basicMapper.batchInsertBmSeawallStructinfo(list);
//                }
//                log.info("----------------------结束同步海塘主体断面图----------------------");
//            }
//        }
//    }
    private String getToken(String prcd) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", "13858860396");
        jsonObject.put("prcd", "HT330301000001");
        if(StringUtils.isNotBlank(prcd)){
            jsonObject.put("prcd", prcd);
        }
        jsonObject.put("accesskey", key);
        jsonObject.put("accesssecret", secret);
        String data = JSON.toJSONString(jsonObject);
        return DESUtil.encrypt(DESUtil.KEY, data);
    }

    private String getToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", "13858860396");
        jsonObject.put("prcd", "");
        jsonObject.put("accesskey", key);
        jsonObject.put("accesssecret", secret);
        String data = JSON.toJSONString(jsonObject);
        return DESUtil.encrypt(DESUtil.KEY, data);
    }
}
