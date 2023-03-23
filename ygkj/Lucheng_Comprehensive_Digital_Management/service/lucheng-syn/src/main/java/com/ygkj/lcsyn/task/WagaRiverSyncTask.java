package com.ygkj.lcsyn.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ygkj.lcsyn.config.DefaultThreadFactory;
import com.ygkj.lcsyn.mapper.BusinessMapper;
import com.ygkj.lcsyn.model.StWasR;
import com.ygkj.lcsyn.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 温瑞平水闸 工程、测站、实时监测值 同步任务
 * @author xq
 * @Date 2021/4/15
 */
@Component
@Slf4j
public class WagaRiverSyncTask implements InitializingBean, DisposableBean {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd%HH:mm:ss");

    /**
     * 水闸要用的自己的线程池，和restclient，以免因第三方接口影响公共线程池
     */
    RestTemplate restTemplate;

    ExecutorService commonExecutor;

    @Resource
    private BusinessMapper businessMapper;


    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyyMM");

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void scheduleTask(){
        commonExecutor.execute(()->{
            try {
                log.info("开始温瑞平水闸水闸测站数据同步任务");
                syncWagaStationValue(LocalDateTime.now());
            }catch (Exception e){
                log.info("温瑞平水闸水闸测站数据同步任务报错",e);
            }
        });
    }


    public void syncWagaStationValue(LocalDateTime now) {
        try {
            log.info("开始同步温瑞平水闸水闸测站数据");
            int sum = 0;
            String response = restTemplate.getForObject("https://60.190.114.117:80/wrpbz/base/Queryzmgq?tm=" + now.format(dtf), String.class);
            JSONArray jsonArray = JSONArray.parseArray(response);
            for (Object o : jsonArray) {
                JSONObject jo = (JSONObject) o;
                StWasR gate = new StWasR();
                /**
                 * 合并测站编码
                 */
                gate.setMgstcd(jo.getString("ZMID"));
                /**
                 * 测站编码
                 */
                gate.setStcd(jo.getString("ZMID"));
                /**
                 * 检测时间
                 */
                gate.setTm(jo.getDate("time"));
                /**
                 * 闸上水位
                 */
                gate.setUpz(jo.getDouble("ZQSW"));
                /**
                 * 闸下水位
                 */
                gate.setDwz(jo.getDouble("ZHSW"));
                /**
                 * 最大过闸流量
                 */
                gate.setMxgtq(jo.getString("MaxLL"));
                /**
                 * 开度
                 */
                gate.setOvs(jo.getString("KDs"));
                /**
                 * 开度
                 */
                gate.setLl(jo.getString("LL"));
                if (StringUtils.isBlank(gate.getStcd()) || gate.getTm() == null) {
                    // tmd ,这数据质量太差了
                    continue;
                }
                gate.setTableName("st_was_r_" + DateUtils.date2LocalDateTime(gate.getTm()).format(dtf1));
                try {
                    sum += businessMapper.replaceWas(gate.getTableName(), Arrays.asList(gate));
//                    sum += synchronizedMapper.replaceWeirGate(gate);
                } catch (Exception e) {
                    log.error("温瑞平水闸水闸数据插入异常", e);
                }
            }
            log.info("同步温瑞平水闸水闸测站数据结束：{}", sum);
        } catch (Exception e) {
            log.error("同步温瑞平水闸水闸测站数据异常", e);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        commonExecutor= new ThreadPoolExecutor(1, 32, 30, TimeUnit.MINUTES
                , new ArrayBlockingQueue<>(128),new DefaultThreadFactory("waga_sync_pool_"),new ThreadPoolExecutor.DiscardOldestPolicy());
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                return true;
            }
        }).build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext,
                new String[]{"TLSv1","TLSv1.2"},
                null,
                NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(30000);//30s 超时
        requestFactory.setReadTimeout(5*60*1000);//5m 超时
        this.restTemplate= new RestTemplate(requestFactory);
    }


    @Override
    public void destroy() throws Exception {
        commonExecutor.shutdown();
    }
}
