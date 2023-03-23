package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.lcsyn.enums.WzApiEnum;
import com.ygkj.lcsyn.mapper.BasicMapper;
import com.ygkj.lcsyn.model.AttTideFcstBase;
import com.ygkj.lcsyn.model.AttTideFcstValBase;
import com.ygkj.lcsyn.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Component
@Slf4j
public class StormTideTask {

    @Resource
    private BasicMapper basicMapper;

    @Resource
    private TokenUtil tokenUtil;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 风暴潮
     */
    @Scheduled(cron = "0 0 0/4 * * ?")
    public void run() throws InterruptedException {
        log.info("------------------开始同步风暴潮数据-----------------");
        //首先查询所有潮位站的潮位
        List<String> stCodeList = selectAllStCode();
        String stCodes = String.join(",", stCodeList);
        Map<String, Object> param = new HashMap<>();
        param.put("stCodes", stCodes);

        //查询温州所有的风暴潮预报
        List<AttTideFcstBase> wzList = selectFcstTideFromWz();

        //查询本地所有的风暴潮预报
        Set<String> localSet = basicMapper.listFcstTide();

        //遍历所有没查询过的
        for (AttTideFcstBase attTideFcstBase : wzList) {
            if(attTideFcstBase.getReleaseType() == 1){
                continue;
            }
            String id = attTideFcstBase.getId();
            if (!localSet.contains(id)) {
                param.put("fcstId", attTideFcstBase.getId());
                log.info("[同步到新的风暴潮]{}", attTideFcstBase);
                basicMapper.insertFcstTide(attTideFcstBase);
                //获取该风暴潮的详细数据
                List<AttTideFcstValBase> fcstValBaseList = selectFcstTideValFromWz(param);
                //入库该风暴潮和详细数据
                if (!CollectionUtils.isEmpty(fcstValBaseList)) {
                    fcstValBaseList.forEach(attTideFcstValBase -> attTideFcstValBase.setStCode(attTideFcstValBase.getStCode().replaceAll("SWZ_", "")));
                    log.info("[获取风暴潮值列表]{}", fcstValBaseList.size());
                    basicMapper.batchInsertFcstTideVal(fcstValBaseList);
                }
            }
            Thread.sleep(30000);
        }
        log.info("------------------结束同步风暴潮数据-----------------");
    }

    private List<AttTideFcstValBase> selectFcstTideValFromWz(Map<String, Object> param) {
        String token = tokenUtil.getToken(WzApiEnum.FSCT_VAL);
        //从温州请求数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("authorization", token);
        HttpEntity request = new HttpEntity(param, headers);
        String rs = restTemplate.postForObject(WzApiEnum.FSCT_VAL.getUrl(), request, String.class);
        JSONObject jsonObject = JSONObject.parseObject(rs);
        if (jsonObject.getInteger("code") == 0) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                JSONArray jsonArray = data.getJSONArray("data");
                if (!CollectionUtils.isEmpty(jsonArray)) {
                    return JSONArray.parseArray(jsonArray.toJSONString(), AttTideFcstValBase.class);
                }
            }
        }
        return null;
    }

    private List<AttTideFcstBase> selectFcstTideFromWz() {
        String token = tokenUtil.getToken(WzApiEnum.FSCT);
        //从温州请求数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("authorization", token);
        HttpEntity request = new HttpEntity(new HashMap<>(), headers);
        ResponseEntity<String> exchange = restTemplate.exchange(WzApiEnum.FSCT.getUrl(), HttpMethod.GET, request, String.class);
        JSONObject jsonObject = JSONObject.parseObject(exchange.getBody());
        if (jsonObject.getInteger("code") == 0) {
            JSONObject data = jsonObject.getJSONObject("data");
            if (data != null) {
                data = data.getJSONObject("data");
                if (data != null) {
                    JSONArray list = data.getJSONArray("list");
                    if (!CollectionUtils.isEmpty(list)) {
                        return JSONArray.parseArray(list.toJSONString(), AttTideFcstBase.class);
                    }
                }
            }
        }
        return null;
    }

    private List<String> selectAllStCode() {
        final String swz = "SWZ_";
        List<String> stCodeList = basicMapper.listSeawallTide();
        List<String> swzList = new ArrayList<>();
        for (String s : stCodeList) {
            swzList.add(swz + s);
        }
        stCodeList.addAll(swzList);
        return stCodeList;
    }
}
