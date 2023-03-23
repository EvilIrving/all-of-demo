package com.ygkj.big.screen.strategy;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.enums.MessageTypes;
import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.big.screen.vo.response.RsvrStationWaterResVo;
import com.ygkj.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RsvrOverLimitWarningStrategy implements WarningStrategy {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 20, 60, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(10));

    public RsvrOverLimitWarningStrategy() {

    }

    @Override
    public String type() {
        return MessageTypes.WATER_LEVEL.type();
    }

    @Override
    public List<MessageRecord> handle(Collection<?> collection) {
        // 6月5日11时15分,永嘉县-金溪水库水位261.01m，超汛限/超警戒1.01m
        List<RsvrStationWaterResVo> array = (List<RsvrStationWaterResVo>) collection;
        List<MessageRecord> result = new ArrayList<>(collection.size() % 2 == 0 ? collection.size() : collection.size() << 1);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分");
        for (RsvrStationWaterResVo rsvr : array) {
            String county = rsvr.getAreaName();
            String resName = rsvr.getResName();
            Double waterLevel = rsvr.getWaterLevel();
            Double overLimitVal = rsvr.getOverLimitVal();
            String batchId = UUID.randomUUID().toString();
            String content = "水情预警：".concat(sdf.format(rsvr.getDataTime())).concat(county)
                    .concat("-")
                    .concat(resName)
                    .concat("水位")
                    .concat(String.format("%.2f", waterLevel))
                    .concat("m，超汛限/超警戒")
                    .concat(String.format("%.2f", overLimitVal));
            MessageRecord record = new MessageRecord(content, batchId, DEFAULT_RECEIVE_PHONE, DEFAULT_RECEIVE_PERSON, new Date(), type(), "已发送");
            executor.submit(new SendTask(content, DEFAULT_RECEIVE_PHONE));
            result.add(record);
        }
        return result;
    }

    private class SendTask implements Runnable {

        String content;

        String phone;

        public SendTask(String content, String phone) {
            this.content = content;
            this.phone = phone;
        }

        @Override
        public void run() {
            JSONObject params = new JSONObject();
            params.put("phone", phone);
            params.put("content", content);
            String result = null;
            try {
                result = HttpClientUtil.httpPost(POST_SEND_URL_PREFIX, params.toJSONString(), new HashMap<>());
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info(result);
        }
    }
}
