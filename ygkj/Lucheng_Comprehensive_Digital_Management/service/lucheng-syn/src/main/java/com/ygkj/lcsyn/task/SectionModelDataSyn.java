package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.mapper.SectionModelDataMapper;
import com.ygkj.lcsyn.model.SectionModelData;
import com.ygkj.lcsyn.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
//@RunWith(SpringRunner.class)
//@ActiveProfiles("dev")
//@SpringBootTest(classes = LcSynApplication.class)
public class SectionModelDataSyn {
    private static final String token = "eyJlbmMiOiJBMjU2R0NNIiwiYWxnIjoiUlNBLU9BRVAifQ.DHF-MO7BlALzsMs8NH0KMBNg1_xPACfDl0fZWBu1fL6difgGAq7Lk1EPT9fS0tHbmL72bHsYNxd2YI7yvm7ibDeJUUC3wi57k_93CwI-yNOCN2FxNzJKEjU3WFSsYexB7fopkzRTWnW3DrIUR4_Y9bweOtM4AP2Xg32DPIDb7R8.xg_Y5QSoNeTMHVrO.2vW-SAfGImqZC9WCIgnT6igPaOtMh-Ct1uCKYPxqLHyzfu4aGKA1KBh4TfeZ.CzaLmzLWqzD31GeAWs6qVg";

    @Autowired
    private SectionModelDataMapper sectionModelDataMapper;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void sectionModelDataSyn(){
        String url = "http://112.17.127.75:18099/infoWork/queryResult";
        String[] ids = {"333.a.27"};
        Map<String,String> params = new HashMap<>();
        params.put("sectionList",String.join(",",ids));
        params.put("attributeList","rr_depth,rr_flow");

        JSONObject jsonObject = HttpClientUtil.doPost(url, params, "Authorization", token);
        if (jsonObject != null){
            for (String id : ids) {
                if (jsonObject.containsKey(id)){
                    List<SectionModelData> modelData = new ArrayList<>();
                    List<JSONObject> objectList = new ArrayList<>();
                    String sectionId = id.substring(6);

                    JSONObject object = jsonObject.getJSONObject(id);
                    JSONArray rrDepth = object.getJSONArray("rr_depth");
                    List<JSONObject> rrDepthList = rrDepth.toJavaList(JSONObject.class);
                    objectList.addAll(rrDepthList);

                    rrDepthList.forEach(o->o.put("type","rrDepth"));
                    JSONArray rrFlow = object.getJSONArray("rr_flow");
                    List<JSONObject> rrFlowList = rrFlow.toJavaList(JSONObject.class);
                    rrFlowList.forEach(o->o.put("type","rrFlow"));
                    objectList.addAll(rrFlowList);

                    Map<String, List<JSONObject>> dataTime = objectList.stream().collect(Collectors.groupingBy(o -> o.getString("dataTime")));
                    Set<Map.Entry<String, List<JSONObject>>> entries = dataTime.entrySet();
                    for (Map.Entry<String, List<JSONObject>> entry : entries) {
                        String key = entry.getKey();
                        List<JSONObject> value = entry.getValue();
                        SectionModelData sectionModelData = new SectionModelData();
                        sectionModelData.setSectionid(sectionId);
                        sectionModelData.setHour(key);
                        for (JSONObject obj : value) {
                            if ("rrDepth".equals(obj.getString("type"))){
                                sectionModelData.setDepth(obj.getDouble("dataValue"));
                            }
                            if ("rrFlow".equals(obj.getString("type"))){
                                sectionModelData.setFlow(obj.getDouble("dataValue"));
                            }
                        }
                        modelData.add(sectionModelData);
                    }
                    sectionModelDataMapper.replaceAll(modelData);
                }
            }
        }
    }
}
