package com.ygkj.big.screen.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.enums.MessageTypes;
import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MountainFloodWarningStrategy implements WarningStrategy {

    private ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2, 20, 60, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(10));

    public MountainFloodWarningStrategy() {

    }

    @Override
    public String type() {
        return MessageTypes.MOUNTAIN_FLOOD.type();
    }

    @Override
    public List<MessageRecord> handle(Collection<?> collection) {
        JSONArray array = (JSONArray) collection;
        List<MessageRecord> result = new ArrayList<>(collection.size() % 2 == 0 ? collection.size() : collection.size() << 1);
        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;
            String county = jsonObject.getString("adnm");
            JSONArray villages = jsonObject.getJSONArray("villages");
            for (Object o : villages) {
                String batchId = UUID.randomUUID().toString();
                JSONObject village = (JSONObject) o;
                String villageName = village.getString("adnm");
                String warnGrade = village.getString("warnGradeID").equals("4") ? "准备转移" : "立即转移";
                String content = "山洪预警（".concat(warnGrade).concat("）：".concat(village.getString("thresholdTime")).concat(county).concat(villageName))
                        .concat("累计降雨量超过").concat(village.getString("thresholdData")).concat("mm,")
                        .concat(
                                warnGrade.equals("准备转移") ? "，及时通知转移责任人安排准备转移相关事宜" : "，及时通知转移责任人安排村民立即转移"
                        );
                MessageRecord record = new MessageRecord(content, batchId, DEFAULT_RECEIVE_PHONE, DEFAULT_RECEIVE_PERSON, new Date(), type(), "已发送");
                executor.submit(new SendTask(content, DEFAULT_RECEIVE_PHONE));
                result.add(record);
            }
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
