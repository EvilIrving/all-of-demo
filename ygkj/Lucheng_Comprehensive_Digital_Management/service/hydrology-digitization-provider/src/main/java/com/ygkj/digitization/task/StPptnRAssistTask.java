package com.ygkj.digitization.task;

import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.mapper.StPptnRAssistMapper;
import com.ygkj.digitization.mapper.SynRainAndWaterMapper;
import com.ygkj.digitization.model.AttStBase;
import com.ygkj.digitization.vo.request.StationRequestVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

//@Component
@Slf4j
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class StPptnRAssistTask {

    @Autowired
    private SynRainAndWaterMapper rainAndWaterMapper;
    @Autowired
    private StPptnRAssistMapper stPptnRAssistMapper;
    @Autowired
    private AttStBaseMapper stBaseMapper;

    @Scheduled(cron = "10 0/1 * * * ?")
    @Test
    public void execute() throws Exception {
        log.info("雨量辅助表数据同步开始");
        StationRequestVo requestVo2 = new StationRequestVo();
        requestVo2.setIsRainStation(Boolean.TRUE);
        List<AttStBase> attStBases = stBaseMapper.selectByConditionT(requestVo2);//测站集合

        ForkJoinPool forkjoinPool = new ForkJoinPool();

        //生成一个计算任务
        CountTask task = new CountTask(attStBases,rainAndWaterMapper,stPptnRAssistMapper,stBaseMapper);

        //执行一个任务
        Future<Integer> result = forkjoinPool.submit(task);

        try
        {
            log.info("计算得到的结果:{}",result.get());
        }
        catch(Exception e)
        {
            log.info("错误信息:{}",e.getMessage());
        }

        log.info("雨量辅助表数据同步结束");
    }

}
