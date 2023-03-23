package com.ygkj.lcsyn.task;


import com.ygkj.lcsyn.mapper.BhMapper;
import com.ygkj.lcsyn.mapper.ExchangeMapper;
import com.ygkj.lcsyn.model.BhLgPrjHideDanger;
import com.ygkj.lcsyn.model.BhSlPatrolGis;
import com.ygkj.lcsyn.model.BhSlPatrolGisPosition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
public class PatrolTask {

    @Resource
    private BhMapper bhMapper;

    @Resource
    private ExchangeMapper exchangeMapper;

    @Scheduled(cron = "0 0 0/6 * * ?")
    @Async("asyncTaskExecutor")
    public void run() {
        Date date = new Date();
        date.setTime(date.getTime() - 30l * 24 * 60 * 60 * 1000);
        //工程巡查总记录
        synPatrol(date);

        //巡查点巡查记录
        synPatrolPosition(date);

        //工程隐患
        synDanger();
    }

    //    @Scheduled(cron = "0 0 0/6 * * ?")
//    @Async("asyncTaskExecutor")
    private void synDanger() {
        log.info("-------------开始同步工程隐患-------------");
        List<BhLgPrjHideDanger> list = bhMapper.listDanger();
        if (!CollectionUtils.isEmpty(list)) {
            exchangeMapper.del("bh_lg_prj_hide_danger");
            //替换这部分数据
            exchangeMapper.batchInsertDanger(list);
        }
        log.info("-------------结束同步工程隐患-------------");
    }

    private void synPatrolPosition(Date date) {
        log.info("-------------开始同步工程巡查点-------------");
        List<BhSlPatrolGisPosition> list = bhMapper.listPatrolPosition(date);
        if (!CollectionUtils.isEmpty(list)) {
            exchangeMapper.delPatrolPosition(date);
            //替换这部分数据
            exchangeMapper.batchInsertPatrolPosition(list);
        }
        log.info("-------------结束同步工程巡查点-------------");
    }

    private void synPatrolPosition2(Date date) {
        log.info("-------------开始同步工程巡查点-------------");
        //查出标化数据
        List<BhSlPatrolGisPosition> list = bhMapper.listPatrolPosition(date);
        Set<Integer> set = list.stream().map(BhSlPatrolGisPosition::getId).collect(Collectors.toSet());

        //查询本地数据
        List<BhSlPatrolGisPosition> localList = exchangeMapper.listPatrolPosition(date);
        Map<Integer,BhSlPatrolGisPosition> localMap = localList.stream().collect(Collectors.toMap(BhSlPatrolGisPosition::getId,Function.identity()));

        //和本地数据进行比对

        //删除列表 del
        List<Integer> delList = new ArrayList<>();
        for (BhSlPatrolGisPosition bhSlPatrolGisPosition : localList) {
            Integer id = bhSlPatrolGisPosition.getId();
            //标化已经没有该记录就删除
            if (!set.contains(id)) {
                delList.add(id);
            }
        }
        if(!CollectionUtils.isEmpty(delList)){
            exchangeMapper.delPatrolPosition2(delList);
        }

        //更新列表 replace into
        List<BhSlPatrolGisPosition> updateList = new ArrayList<>();

        //新增列表 insert into
        List<BhSlPatrolGisPosition> insertList = new ArrayList<>();

        for (BhSlPatrolGisPosition bhSlPatrolGisPosition : list) {
            if(localMap.containsKey(bhSlPatrolGisPosition.getId())){
                //本地有该条数据且数据不相等则更新
                if(!bhSlPatrolGisPosition.equals(localMap.get(bhSlPatrolGisPosition.getId()))){
                    updateList.add(bhSlPatrolGisPosition);
                }
            }else {
                //本地没有该条数据就新增
                insertList.add(bhSlPatrolGisPosition);
            }
        }

       if(!CollectionUtils.isEmpty(updateList)){
           exchangeMapper.batchReplacePatrolPosition(updateList);
       }

       if(!CollectionUtils.isEmpty(insertList)){
           exchangeMapper.batchInsertPatrolPosition(insertList);
       }

        log.info("-------------结束同步工程巡查点-------------");
    }

    private void synPatrol(Date date) {
        log.info("-------------开始同步巡查-------------");
        List<BhSlPatrolGis> list = bhMapper.listPatrol(date);
        if (!CollectionUtils.isEmpty(list)) {
            exchangeMapper.delPatrol(date);
            //替换这部分数据
            exchangeMapper.batchInsertPatrol(list);
        }
        log.info("-------------结束同步巡查-------------");
    }

    public static void main(String[] args) {
        Date date = new Date();
        date.setTime(date.getTime() - 30l * 24 * 60 * 60 * 1000);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
    }


}
