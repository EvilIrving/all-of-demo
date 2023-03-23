package com.dcy.datamanage.biz.dsm;

import com.dcy.datamanage.api.model.BusDataSource;
import com.dcy.datamanage.biz.dsm.datasource.*;
import com.dcy.datamanage.biz.service.DataSourceService;
import org.springframework.aop.interceptor.AsyncExecutionAspectSupport;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataSourceRegistry implements DisposableBean {

    @Autowired
    private DataSourceService dataSourceService;

    private static Map<String, DefaultDataSource> map = new ConcurrentHashMap<>();

    public void register(String dataSourceId, DefaultDataSource dataSource) {
        map.put(dataSourceId, dataSource);
    }

    public DefaultDataSource getDataSource(String dataSourceId) {
        DefaultDataSource dataSource;
        if (map.containsKey(dataSourceId)) {
            dataSource = map.get(dataSourceId);
        } else {
            //没有注册过的初始化
            BusDataSource busDataSource = dataSourceService.queryById(dataSourceId);
            switch (busDataSource.getDataBaseType()) {
                case 1:
                    dataSource = new MySqlDataSource(busDataSource);
                    break;
                case 2:
                    dataSource = new OracleDataSource(busDataSource);
                    break;
                case 3:
                    dataSource = new PostgreSqlDataSource(busDataSource);
                    break;
                case 4:
                    dataSource = new SqlServerDataSource(busDataSource);
                    break;
                case 5:
                    dataSource = new DmDataSource(busDataSource);
                    break;
                default:
                    dataSource = new DefaultDataSource(busDataSource);
            }
            register(dataSourceId, dataSource);
        }
        return dataSource;
    }

    @Override
    public void destroy() throws Exception {
        cleanAll();
    }

    @Scheduled(cron = "0 0 5 * * ?")
    @Async(AsyncExecutionAspectSupport.DEFAULT_TASK_EXECUTOR_BEAN_NAME)
    public void cleanAll() {
        map.forEach((k, v) -> v.close());
        map.clear();
    }

}
