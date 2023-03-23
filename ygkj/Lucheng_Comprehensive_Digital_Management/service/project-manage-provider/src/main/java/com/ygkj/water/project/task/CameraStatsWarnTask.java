package com.ygkj.water.project.task;

import com.ygkj.project.model.SysWarnLog;
import com.ygkj.project.vo.response.ChartResVo;
import com.ygkj.project.vo.response.HikCameraResVo;
import com.ygkj.project.vo.response.HikCameraStatsResVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.project.ProjectManageApplication;
import com.ygkj.water.project.feign.SmsFeignClient;
import com.ygkj.water.project.mapper.HikCameraMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
@Component
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = ProjectManageApplication.class)
public class CameraStatsWarnTask {


    private static final String SMS_SEND_URL = "http://172.20.25.165:8085/message/sendMessage";

    private static final String DEFAULT_ENCODING = "UTF-8";

    @Resource
    HikCameraMapper cameraMapper;

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH：mm：ss");

    @Resource
    SmsFeignClient smsClient;

    @Test
    @Scheduled(cron = "0 0 9 * * ?")
    public void doStatsAndWarn() {
        long total = 0;
        AtomicLong offline = new AtomicLong(0);
        AtomicLong online = new AtomicLong(0);
        HikCameraStatsResVo result = new HikCameraStatsResVo();
        List<HikCameraResVo> cameraList = this.cameraMapper.selectAllStatusAdcdBasin();
        cameraList = cameraList == null ? Collections.emptyList() : cameraList;
        List<ChartResVo<String, Long>> onlineNum = new ArrayList<>();
        List<ChartResVo<String, Long>> totalNum = new ArrayList<>();
        List<ChartResVo<String, Long>> offlineNum = new ArrayList<>();
        total = cameraList.size();
        cameraList.removeAll(Collections.singleton(null));
        Map<Integer, List<HikCameraResVo>> tempMap = cameraList.stream().filter(o -> o.getStatus() != null).collect(Collectors.groupingBy(o -> o.getStatus()));
        tempMap.forEach((key, tempStatus) -> {
            if (key.equals(1)) {
                online.set(tempStatus.size());
            } else {
                offline.set(tempStatus.size());
            }
        });
        result.setOfflineNum(offlineNum);
        result.setTotalNum(totalNum);
        result.setOnlineNum(onlineNum);
        String content = "摄像头简报：" + LocalDateTime.now().format(dtf) + "共有摄像头" + total
                + "个，当前在线" + online.get() + "个，离线" + offline.get() + "个";
        String phones = "15968720803";
////        String phones = "15958129170";
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("content", content);
//        params.put("phone", phones);
//
//        String response = HttpClientUtil.sendGet(SMS_SEND_URL, null, params, DEFAULT_ENCODING);
//        System.out.println(response);
        /**
         * 4月26日，更改短信发送方式，当obj_id 为空的时候，只会发送短信，不会插入数据库日志中
         */
        smsClient.addOrUpdate(Builder.of(SysWarnLog::new)
                .with(SysWarnLog::setReceivePhone, phones)
                .with(SysWarnLog::setMsg, content)
                .with(SysWarnLog::setMsgType, "100")
                .build());
    }
}
