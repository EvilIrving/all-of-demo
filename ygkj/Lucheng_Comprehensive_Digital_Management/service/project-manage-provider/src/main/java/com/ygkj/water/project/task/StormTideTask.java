package com.ygkj.water.project.task;

import com.ygkj.project.vo.response.StormResVo;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.water.project.enums.StormTideEnum;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class StormTideTask {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 台风风暴潮
     */
    @Scheduled(cron = "0 10 0/1 * * ?")
    @Async
    public void synTyphoon() {
        synStormTide(StormTideEnum.TYPHOON);
    }

    /**
     * 温带风暴潮
     */
    @Scheduled(cron = "0 10 0/1 * * ?")
    @Async
    public void synTemperate() {
        synStormTide(StormTideEnum.TEMPERATE);

    }

    private void synStormTide(StormTideEnum stormTideEnum) {
        log.info("--------------------开始同步{}------------------",stormTideEnum);
        String outStr = HttpClientUtil.httpGet(stormTideEnum.getUrl());
        Document outDocument = Jsoup.parse(outStr);
        Elements bj_03 = outDocument.getElementsByClass("bj_03");
        Element element = bj_03.get(0);
        Node node = element.childNode(0);
        String href = node.attr("href");
        href = href.substring(href.indexOf("=") + 1);
        //请求最新数据
        String str = HttpClientUtil.sendGet(stormTideEnum.getDetailUrl() + href, null, null, "GB2312");
        Document document = Jsoup.parse(str);
        Elements bk_19 = document.getElementsByClass("bk_19");
        Node resultNode = bk_19.get(0).childNode(0);

        //获取当前结果
        String outerHtml = resultNode.outerHtml();
        log.info("[获取{}风暴潮结果]{}",stormTideEnum,outerHtml);
        //首先判断是否和温州有关
        if (str.indexOf("温州") > 0) {
            log.info("[获取{}风暴潮与温州相关]",stormTideEnum);
            if (outerHtml.equals("解除")) {
                //解除预警就删除台风风暴潮的数据
                redisTemplate.delete(stormTideEnum.getRedisKey());
            } else {
                //有数据比较是否和已经保存的数据是同一条数据
                if (redisTemplate.hasKey(stormTideEnum.getRedisKey())) {
                    StormResVo stormResVo = (StormResVo) redisTemplate.opsForValue().get(stormTideEnum.getRedisKey());
                    //相同数据的话就不做任何处理
                    if (stormResVo.getUrlId().equals(outerHtml)) {
                        return;
                    }
                }
                //保存新的数据
                StormResVo stormResVo = new StormResVo();
                stormResVo.setResult(outerHtml);
                stormResVo.setUrlId(href);
                redisTemplate.opsForValue().set(stormTideEnum.getRedisKey(), stormResVo, 12, TimeUnit.HOURS);
            }
        }
        log.info("---------------------结束同步{}------------------",stormTideEnum);
    }

}
