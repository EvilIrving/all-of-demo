package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.enums.ProvinceDataEnum;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.model.ProvinceDataRecord;
import com.ygkj.lcsyn.service.DingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ProvinceDataTask {

    @Resource
    private DingService dingService;

    @Resource
    private BusinessMapper businessMapper;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {
        log.info("请求省数据仓开始");
        ProvinceDataEnum[] values = ProvinceDataEnum.values();
        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        map.put("startTime", now.minusDays(1).format(dtf));
        map.put("endTime", now.format(dtf));

//        JSONObject res = dingService.post(ProvinceDataEnum.TYPHOON_HISTORY, map);
//        log.info("[调用成功][接口]{}[结果]{}", ProvinceDataEnum.TYPHOON_HISTORY.getDescribe(), res);
        List<ProvinceDataRecord> list = new ArrayList<>();
        for (ProvinceDataEnum provinceDataEnum : values) {
            if (provinceDataEnum == ProvinceDataEnum.TOKEN) {
                continue;
            }
            ProvinceDataRecord provinceDataRecord = new ProvinceDataRecord(provinceDataEnum);
            provinceDataRecord.setParam(map.toString());
            try {
                JSONObject res = dingService.post(provinceDataEnum, map);
                provinceDataRecord.setResult(0);
                provinceDataRecord.setRes(res.toJSONString());
            } catch (Exception e) {
                provinceDataRecord.setResult(1);
                provinceDataRecord.setRes(e.getMessage());
            } finally {
                list.add(provinceDataRecord);
            }
        }
        if(!CollectionUtils.isEmpty(list)){
            businessMapper.batchInsertProvinceData(list);
        }
        log.info("请求省数据仓结束");
    }
}
