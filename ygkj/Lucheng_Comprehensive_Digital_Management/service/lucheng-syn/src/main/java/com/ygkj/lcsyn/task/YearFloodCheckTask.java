package com.ygkj.lcsyn.task;

import com.ygkj.lcsyn.mapper.BhMapper;
import com.ygkj.lcsyn.mapper.ExchangeMapper;
import com.ygkj.lcsyn.model.BhLgMpAc;
import com.ygkj.lcsyn.model.BhLgMpFc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class YearFloodCheckTask {

    @Resource
    private ExchangeMapper exchangeMapper;

    @Resource
    private BhMapper bhMapper;

    /**
     * 年度检查
     */
    @Scheduled(cron = "0 0 3 * * ?")
    @Async("asyncTaskExecutor")
    public void year() {
        log.info("------------------开始同步年度检查------------------");
        //查询全部年度检查
        List<BhLgMpAc> list = bhMapper.listYearCheck();

        //有新数据就存起来
        if(!CollectionUtils.isEmpty(list)){
            exchangeMapper.batchReplaceYearCheck(list);
        }
        log.info("------------------结束同步年度检查------------------");
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Async("asyncTaskExecutor")
    public void flood() {
        log.info("------------------开始同步汛前汛后检查------------------");
        //查询全部数据
        List<BhLgMpFc> list = bhMapper.listFloodCheck();

        //有新数据就存起来
        if(!CollectionUtils.isEmpty(list)){
            exchangeMapper.batchReplaceFloodCheck(list);
        }
        log.info("------------------结束同步汛前汛后检查------------------");
    }

}
