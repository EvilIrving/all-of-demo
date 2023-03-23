package com.ygkj.soft.database.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.service.DataReceiveService;
import com.ygkj.soft.database.dao.DataDao;
import com.ygkj.soft.database.service.DataQueryService;
import com.ygkj.soft.database.util.DataReceiveThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Huang.zh
 * @date 2020/9/1 15:18
 * @Description: 数据接收服务具体实现
 */
//@Service("dataReceiveService")
@Slf4j
public class DataReceiveServiceImpl implements DataReceiveService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private DataReceiveThreadFactory threadFactory;

    @Autowired
    private DataQueryService dataQueryService;

    @Autowired
    private DataDao dataDao;

    @Override
    public void DataReceive(Object data) throws Exception {
        DataReceiveTask task = new DataReceiveTask(data);
        Future<Integer> submit = threadFactory.submit(task);
        //这里get方法阻塞一下，是为了确保数据尽可能的被持久化，否则容易发生数据不同步的情况
        int value = submit.get().intValue();
        if (value <= 0){
            throw new RuntimeException("数据接收异常");
        }
    }


    /**
     * @Author Huang.zh
     * @Description 核心任务，将开放的资源数据持久化至中间库，有返回值，返回DB操作影响的记录行数
     * @Date 2020/9/2 9:20
     */
    private class DataReceiveTask implements Callable {

        private Object data;

        public DataReceiveTask(Object data) {
            this.data = data;
        }

        @Override
        public Object call() throws Exception {
            JSONObject object = (JSONObject) JSON.toJSON(data);
            object = object.getJSONObject("data");
            String tableName = object.getString("tableName");
            if (tableName == null) {
                throw new RuntimeException("暂未指定表名！请添加tableName字段");
            }
            /*****判定数据版本*****/
            String id = object.getString("id");
            int version;
            if (StringUtils.isEmpty(id)){
                //插入的数据版本号为1
                version = 1;
            }else{
                //更新的数据在前面的版本基础上+1
                JSONObject newestVersion = dataQueryService.queryNewestVersionById(id, tableName);
                if (newestVersion == null){
                    version = 1;
                }else{
                    version = newestVersion.getIntValue("version")+1;
                }
            }
            /*****组装基础字段的数据，基础字段可以反映数据的变化*****/
            Map<String,Object> params = new HashMap<>();
            params.put("version",String.valueOf(version));
            params.put("data",object.toJSONString());
            params.put("id",object.getString("id"));
            params.put("global_id", UUID.randomUUID().toString());
            int records = dataDao.saveData(tableName, params);
            return records;
        }
    }
    @Override
    public void changeStatus(@NotNull(message = "tableName不允许为空！") String tableName,
                             @NotNull(message = "globalId不允许为空！") String globalId,
                             @NotNull(message = "status不允许为空！") int status) {
        int rows = dataDao.changeStatus(tableName, globalId, status);
        if (rows < 1){
            throw new RuntimeException("操作失败");
        }
        // TODO: 2020/9/2 集成消息中间件 ，将globalId推送至消息中间件
        if (status == Constants.PASS_STATUS){
            //通过审核，将globalId推送至RocketMQ，采用异步推送模式
            rocketMQTemplate.asyncSend("soft-data", globalId, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("已成功推送至broker："+sendResult.toString());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.info(throwable.getMessage());
                }
            });
        }
    }
}
