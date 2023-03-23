package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.enums.ProvinceDataEnum;
import com.ygkj.lcsyn.mapper.ProvinceDataMapper;
import com.ygkj.lcsyn.model.SubAdcd;
import com.ygkj.lcsyn.service.DingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)
public class AdcdTask {
    @Resource
    private DingService dingService;
    @Resource
    private ProvinceDataMapper provinceDataMapper;

    @Scheduled(cron = "0 0 0 1/15 * ?")
//    @Test
    public void subAdcdSyn() throws Exception {

        log.info("同步获取行政区划下的镇街道数据");

        Map<String, Object> params = new HashMap<>();
        params.put("adcd","330302");
        JSONObject adcdJson = dingService.post(ProvinceDataEnum.SUB_ADCD, params);
        String status = adcdJson.getString("status");
        if ("0".equals(status)){
            JSONArray message = adcdJson.getJSONArray("message");
            if (message.size() > 0){
                List<SubAdcd> list = message.toJavaList(SubAdcd.class);
                int i = 0;
                i = provinceDataMapper.replaceSubAdcd(list);
                log.info("同步获取行政区划下的镇街道数据结束，更新条数为：{}",i);
            }

        }
    }
}
