package com.ygkj.lcsyn.task;

import ch.qos.logback.classic.db.names.TableName;
import com.ygkj.lcsyn.LcSynApplication;
import com.ygkj.lcsyn.mapper.BhMapper;
import com.ygkj.lcsyn.mapper.ExchangeMapper;
import com.ygkj.lcsyn.model.*;
import com.ygkj.lcsyn.utils.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = LcSynApplication.class)
public class BhDutyTask {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private BhMapper bhMapper;

    @Resource(name = "exchangeDataSourceTransactionManager")
    private DataSourceTransactionManager exchangeDataSourceTransactionManager;

    @Resource
    private ExchangeMapper exchangeMapper;

    @Resource(name = "bhJdbc")
    private JdbcTemplate jdbcTemplate;

    @Resource(name = "exchangeJdbc")
    private JdbcTemplate exchangeJdbc;

    @Scheduled(cron = "0 0 2 * * ?")
    @Async("asyncTaskExecutor")
    public void run() {
        log.info("----------------------开始同步标化值班人员信息----------------------");
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        log.info("[同步值班人员时间]{}",sdf.format(date));
        //只查询本月的信息
        List<BhDutyRecord> list = bhMapper.listDuty(date);
        if (!CollectionUtils.isEmpty(list)) {
            //删除本月旧的信息
            exchangeMapper.delDuty(date);

            //保存本月新的信息
            exchangeMapper.batchInsertDuty(list);
        }
        log.info("----------------------结束同步标化值班人员信息----------------------");
    }

    @Scheduled(cron = "0 0 3 * * ?")
    @Async
    @Test
    public void allSyn() {
        log.info("----------------------标化表同步开始----------------------");
        Class[] classArr = new Class[]{
                BhDepJobsettinginfo.class,
                BhDepManagefund.class,
                BhDepManageinstitution.class,
                BhDepPersontrain.class,
                BhDepProsoninfo.class,
                BhLgMfd.class,
                BhLgMfpm.class,
                BhLgMfu.class,
                BhLgTermiteCheck.class,
                BhLgLmrDisDom.class,
                BhLgLmrDisDomGs.class,
                BhLgLmrDisDomSs.class,
                BhSlPatrolGis.class,
                BhSlPatrolGisAnnex.class,
                BhSysAttachment.class,
                BhSlProjPatrolRate.class
        };

        for (Class clazz : classArr) {
            try {
                //从标化查询数据
                String tableName = SqlUtil.tableName(clazz);
                log.info("----------------------[同步标化表]{}----------------------", tableName);
                String bhTableName = getBhTableName(tableName);
                RowMapper rowMapper = new BeanPropertyRowMapper(clazz);
                List list = jdbcTemplate.query("select * from " + bhTableName, rowMapper);
                log.info("list长度为：{}",list.size());
                if (!CollectionUtils.isEmpty(list)) {
                    List<String> insertSqlList = SqlUtil.batchInsertSql(list);

                    allReplace(tableName, insertSqlList);
                }
            } catch (Exception e) {
                log.info("同步标化表异常，异常为：{}", e.getMessage());
//                e.printStackTrace();
            }
        }
        log.info("----------------------标化表同步结束----------------------");
    }

    public void allReplace(String tableName, List<String> insertSqlList) {
        //手动开启事务和回滚
        // 1.获取事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 2.设置事务隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        //3.获得事务状态
        TransactionStatus status = exchangeDataSourceTransactionManager.getTransaction(def);

        try {
            //删除旧数据
            exchangeJdbc.execute("DELETE FROM " + tableName + " WHERE SOURCE_TYPE = 1");
            //存入新数据
            for (String insertSql : insertSqlList) {
                exchangeJdbc.execute(insertSql);
            }
            exchangeDataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            exchangeDataSourceTransactionManager.rollback(status);
            e.printStackTrace();
        }

    }

    private String getBhTableName(String tableName) {
        return tableName.replace("bh_", "");
    }


    /**
     * 同步标化大数据表，无法一次性更新删除的，且满足id自增
     */
    @Test
    @Scheduled(cron = "0 0 3 * * ?")
    @Async
    public void allSynBigData() {
        log.info("----------------------标化大数据表同步开始----------------------");
        Class[] classArr = new Class[]{
            BhSlPatrolGisInfo.class
        };

        for (Class clazz : classArr) {
            try {
                //从标化查询数据
                String tableName = SqlUtil.tableName(clazz);
                log.info("----------------------[同步标化表]{}----------------------", tableName);
                String bhTableName = getBhTableName(tableName);
                RowMapper rowMapper = new BeanPropertyRowMapper(clazz);

                String maxId = bhMapper.findMaxId(tableName);
                List list = jdbcTemplate.query("select * from " + bhTableName + " where id > " + maxId , rowMapper);
                log.info("list长度为：{}",list.size());
                if (!CollectionUtils.isEmpty(list)) {
                    List<String> insertSqlList = SqlUtil.batchInsertSql(list);
                    //插入数据
                    insertData(tableName, insertSqlList);
                }
            } catch (Exception e) {
                log.info("同步标化大数据表异常，异常为：{}", e.getMessage());
//                e.printStackTrace();
            }
        }
        log.info("----------------------标化大数据表同步结束----------------------");
    }

    public void insertData(String tableName, List<String> insertSqlList) {
        //手动开启事务和回滚
        // 1.获取事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        // 2.设置事务隔离级别，开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        //3.获得事务状态
        TransactionStatus status = exchangeDataSourceTransactionManager.getTransaction(def);

        try {
            //存入新数据
            for (String insertSql : insertSqlList) {
                exchangeJdbc.execute(insertSql);
            }
            exchangeDataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            exchangeDataSourceTransactionManager.rollback(status);
            e.printStackTrace();
        }
    }


}
