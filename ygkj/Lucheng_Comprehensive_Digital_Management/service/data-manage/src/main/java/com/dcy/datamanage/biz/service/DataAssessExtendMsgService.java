package com.dcy.datamanage.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.DataAssessExtendMsg;
import com.dcy.datamanage.biz.dsm.DataSourceRegistry;
import com.dcy.datamanage.biz.dsm.DefaultDataSource;
import com.dcy.datamanage.biz.mapper.DataAssessExtendMsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataAssessExtendMsgService extends ServiceImpl<DataAssessExtendMsgMapper, DataAssessExtendMsg> {

    @Autowired
    private DataSourceRegistry dataSourceRegistry;

    public void replace(DataAssessExtendMsg dataAssessExtendMsg) {
        baseMapper.replace(dataAssessExtendMsg);
    }

    public Map<String, DataAssessExtendMsg> listLastUpdateTime(String dataSourceId, List<String> tableNameList) {
        Map<String, DataAssessExtendMsg> result = new HashMap<>();
        //最近一次更新时间
        //通过数据源自己的方式
        DefaultDataSource dataSource = dataSourceRegistry.getDataSource(dataSourceId);
        List<DataAssessExtendMsg> sourceTimeList = dataSource.listLastUpdateTime(tableNameList);
        if (!CollectionUtils.isEmpty(sourceTimeList)) {
            sourceTimeList.forEach(dataAssessExtendMsg -> result.put(dataAssessExtendMsg.getTableName(), dataAssessExtendMsg));
        }

        //通过本地方式
        LambdaQueryWrapper<DataAssessExtendMsg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataAssessExtendMsg::getDataSourceId, dataSourceId);
        queryWrapper.in(DataAssessExtendMsg::getTableName, tableNameList);
        List<DataAssessExtendMsg> extendTimeList = list(queryWrapper);
        if (!CollectionUtils.isEmpty(extendTimeList)) {
            extendTimeList.forEach(dataAssessExtendMsg -> {
                String tableName = dataAssessExtendMsg.getTableName();
                //比较哪个时间更迟
                if (result.containsKey(tableName)) {
                    DataAssessExtendMsg source = result.get(tableName);
                    if (source.getUpdateTime() != null && dataAssessExtendMsg.getUpdateTime() != null &&
                            dataAssessExtendMsg.getUpdateTime().after(source.getUpdateTime())) {
                        result.put(tableName, dataAssessExtendMsg);
                    }
                } else {
                    //直接放这个
                    result.put(tableName, dataAssessExtendMsg);
                }
            });
        }
        return result;
    }
}