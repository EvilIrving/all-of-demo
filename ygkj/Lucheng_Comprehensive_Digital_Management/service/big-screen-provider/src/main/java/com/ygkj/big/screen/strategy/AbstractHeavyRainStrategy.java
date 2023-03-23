package com.ygkj.big.screen.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.enums.MessageTypes;
import com.ygkj.big.screen.model.MessageRecord;
import com.ygkj.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class AbstractHeavyRainStrategy implements WarningStrategy {

    @Override
    public String type() {
        return MessageTypes.HEAVY_RAIN.type();
    }

    @Override
    public List<MessageRecord> handle(Collection<?> collection) {
        JSONArray array = (JSONArray) collection;
        List<MessageRecord> result = new ArrayList<>();
        String content = "暴雨快讯：";
        Date date = array.getJSONObject(0).getDate("tm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        int hour1 = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.add(Calendar.HOUR, 1);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);
        int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
        List<Object> oneHourBeyond30 = array.stream().filter(o -> ((JSONObject) o).getString("type").equals("1") && ((JSONObject) o).getDoubleValue("drp") > 30d).collect(Collectors.toList());
        String part1 = "" + day1 + "日" + hour1 + "时-" + day2 + "日" + hour2 + "时，" + "1小时超过30mm的站点共" +
                oneHourBeyond30.size() + "个：";
        StringBuffer buffer1 = new StringBuffer();
        String delimiter = "";
        for (Object o : oneHourBeyond30) {
            JSONObject jsonObject = (JSONObject) o;
            buffer1.append(delimiter.concat(jsonObject.getString("stationName").concat(jsonObject.getString("drp")).concat("mm")));
            delimiter = "，";
        }
        delimiter = "";
        String concat1 = content.concat(part1).concat(buffer1.toString()).concat("。");
        calendar.add(Calendar.HOUR, -3);
        day1 = calendar.get(Calendar.DAY_OF_MONTH);
        hour1 = calendar.get(Calendar.HOUR_OF_DAY);
        List<Object> threeHourBeyond60 = array.stream().filter(o -> ((JSONObject) o).getString("type").equals("3") && ((JSONObject) o).getDoubleValue("drp") > 60d).collect(Collectors.toList());
        String part2 = "" + day1 + "日" + hour1 + "时-" + day2 + "日" + hour2 + "时，" + "3小时超过60mm的站点共" +
                threeHourBeyond60.size() + "个：";
        StringBuffer buffer2 = new StringBuffer();
        for (Object o : threeHourBeyond60) {
            JSONObject jsonObject = (JSONObject) o;
            buffer2.append(delimiter.concat(jsonObject.getString("stationName").concat(jsonObject.getString("drp")).concat("mm")));
            delimiter = "，";
        }
        String candidateContent = concat1.concat(part2).concat(buffer2.toString()).concat("。");
        JSONObject params = new JSONObject();
        params.put("phone", DEFAULT_RECEIVE_PHONE);
        params.put("content", candidateContent);
        try {
            log.info(HttpClientUtil.httpPost(POST_SEND_URL_PREFIX, params.toJSONString(), new HashMap<>()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        MessageRecord record = new MessageRecord(candidateContent, UUID.randomUUID().toString(), DEFAULT_RECEIVE_PHONE, DEFAULT_RECEIVE_PERSON, new Date(), type(), "已发送");
        result.add(record);
        return result;
    }

}
