package com.ygkj.soft.database.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.ygkj.soft.database.constant.Constants;
import com.ygkj.soft.database.api.model.BusDataSource;
import io.seata.spring.annotation.datasource.SeataDataSourceBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Huang.zh
 * @date 2020/9/7 18:26
 * @Description: 数据源对应JDBCTemplate动态注入工具
 */
@Component
@Slf4j
public class DynamicDataSourceJDBCTemplateRegister implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private DefaultListableBeanFactory beanFactory;
    //用于创建绑定了不同数据源的jdbcTemplate
    private BeanDefinitionBuilder jdbcTemplateBeanDefinitionBuilder;
    List<String> connectionInitSqlList;
    //一把可重入锁，控制数据源写入时的并发环节
    private ReentrantLock lock;

    /**
     * @Author Huang.zh
     * @Description 根据beanID从容器中获取指定的JDBCTemplate
     * @Date 2020/9/8 9:02
     */
    public JdbcTemplate getCustomJDBCTemplate(String dataBaseName){
        String beanID = Constants.MULTIPLE_JDBC_TEMPLATE_PREFIX.concat(dataBaseName);
        return (JdbcTemplate) beanFactory.getBean(beanID);
    }

    /**
     * @Author Huang.zh
     * @Description 根据一批beanID从容器中获取指定的一批JDBCTemplate
     * @Date 2020/9/21 10:40
     */
    public Map<String,JdbcTemplate> getCustomJDBCTemplates(List<String> dataBaseNameList){
        Map<String,JdbcTemplate> map = new HashMap<>();
        for (String dataBaseName : dataBaseNameList) {
            map.put(dataBaseName,getCustomJDBCTemplate(dataBaseName));
        }
        return map;
    }

    /**
     * @Author Huang.zh
     * @Description 测试数据库连接是否成功
     * @Date 2020/9/8 9:10
     */
    public boolean testDataSourceConnection(BusDataSource dataSource) throws SQLException {
        DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
        druidDataSource.setUrl(dataSource.getDataBaseAddress());
        druidDataSource.setUsername(dataSource.getAccountNumber());
        druidDataSource.setPassword(dataSource.getAccountPwd());
        druidDataSource.setConnectionErrorRetryAttempts(1);
        druidDataSource.init();
        //五秒钟拿不到连接就当做失败
        try {
            DruidPooledConnection connection = druidDataSource.getConnection(5000);
            if (connection == null){
                return false;
            }
            PreparedStatement preparedStatement = connection.prepareStatement(Constants.DEFAULT_CONNECTION_INIT_SQL);
            return preparedStatement.execute();
        }catch (Exception e){
            throw e;
        }finally {
            druidDataSource.close();
        }
    }

    public JdbcTemplate JDBCTemplateRegistry(BusDataSource source){
        lock.lock();
        try {
            //先添加数据源，返回一个数据源代理对象，用于支持分布式事务场景
            DataSource targetDataSource = dataSourceRegistry(source);
            //动态注入jdbcTemplate
            AbstractBeanDefinition beanDefinition = jdbcTemplateBeanDefinitionBuilder.getBeanDefinition();
            String jdbcTemplateName = Constants.MULTIPLE_JDBC_TEMPLATE_PREFIX.concat(source.getDataBaseName());
            //注册beanDefinition，此时只对容器声明添加了jdbcTemplateName对应的bean元数据，还未开始创建
            if (!beanFactory.containsBeanDefinition(jdbcTemplateName)){
                beanFactory.registerBeanDefinition(jdbcTemplateName,beanDefinition);
            }
            //此步是关键，getBean内部最终会通过doCreateBean方法创建当前beanID对应的实例
            //开发小伙伴需要确保beanID唯一，这里影射为数据源的名称要唯一
            JdbcTemplate targetTemplate = (JdbcTemplate) beanFactory.getBean(jdbcTemplateName,targetDataSource);
            //此时的targetTemplate是Spring通过反射调用无参构造为我们创建的，只有拿到该实例才可以手动指定数据源
            targetTemplate.setDataSource(targetDataSource);
            return targetTemplate;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("添加数据源发生异常！请联系管理员！");
        }finally {
            lock.unlock();
        }
    }

    /**
     * @Author Huang.zh
     * @Description 向上下文容器中动态的添加数据源
     * @Date 2020/9/7 20:48
     */
    private DataSource dataSourceRegistry(BusDataSource dataSource) throws Exception {
        DruidDataSource targetDataSource = DruidDataSourceBuilder.create().build();
        targetDataSource.setUrl(dataSource.getDataBaseAddress());
        targetDataSource.setUsername(dataSource.getAccountNumber());
        targetDataSource.setPassword(dataSource.getAccountPwd());
        //由druid完成心跳包发送，监测数据源连接的存活状态
        targetDataSource.setKeepAlive(true);
        //连接初始化后的测试语句
        targetDataSource.setConnectionInitSqls(connectionInitSqlList);
        targetDataSource.init();
        //直接用seata提供的后置处理器对数据源进行代理，最后暴露给外部可供调用的数据源是代理对象。
        SeataDataSourceBeanPostProcessor processor = applicationContext.getBean(SeataDataSourceBeanPostProcessor.class);
        DruidDataSource druidDataSourceProxy = (DruidDataSource)processor.postProcessAfterInitialization(targetDataSource, dataSource.getDataBaseName());
        //注入容器的实际上也是经过代理包装后的数据源
        beanFactory.autowireBean(druidDataSourceProxy);
        return druidDataSourceProxy;
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(applicationContext,"暂未获取到应用程序上下文，请检查代码注入流程！");
        lock = new ReentrantLock();
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        beanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        //初始化两个bean元数据的建造器
        jdbcTemplateBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(JdbcTemplate.class);
        //初始化测试数据源连接的默认sql语句集合
        List<String> connectionInitSqlList = new ArrayList<>();
        connectionInitSqlList.add(Constants.DEFAULT_CONNECTION_INIT_SQL);
        log.info("-----数据源动态注册工具完成初始化！-----");
    }
}
