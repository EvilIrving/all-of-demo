package com.ygkj.big.screen.unit;

import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.vo.request.TideListQueryReqVo;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * @author xq
 * @Description 水源地保障组件
 * @Date 2021/8/11
 */
@Component
@Slf4j
public class WatSrcGuaranteeManager extends MemStackCachedUnitManager {

    @Autowired
    ProjectManageClient projectManageClient;

    public WatSrcGuaranteeManager(@Autowired ApplicationContext context) {
        super(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void doSubscribe() {

    }

    @Override
    public void doPublish() {

    }

    @Override
    public Object getData(UnitDataReqVo reqVo) {
        switch (reqVo.getStrategy()) {
            case "rsvrWatSrcTable": {
                String watSrcName = reqVo.getParam("watSrcName", String.class);
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rsvrWatSrcTable", watSrcName))) == null) {
                    result = rsvrWatSrcTable(watSrcName);
                }
                return result;
            }
            case "rsvrWatSrc4CWindow": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("rsvrWatSrc4CWindow"))) == null) {
                    result = rsvrWatSrc4CWindow();
                }
                return result;
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项水源保障缓存任务");
        String watSrcName = null;
        cache.put(buildDefaultCacheKey("rsvrWatSrcTable", watSrcName), rsvrWatSrcTable(watSrcName));
        cache.put(buildDefaultCacheKey("rsvrWatSrc4CWindow"), rsvrWatSrc4CWindow());
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每5分钟提交一次任务
     */
    @Scheduled(cron = "0 1/5 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }


    /**
     * 水源保证列表和柱状图
     *
     * @param watSrcName 水源名称
     * @return
     */
    public CommonResult rsvrWatSrcTable(String watSrcName) {
        return projectManageClient.rsvrWatSrcTable(watSrcName);
    }

    /**
     * 水源保障c位
     *
     * @return
     */
    public CommonResult rsvrWatSrc4CWindow() {
        return projectManageClient.rsvrWatSrc4CWindow();
    }
}
