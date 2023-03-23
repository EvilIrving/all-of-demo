package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.vo.request.*;
import com.ygkj.big.screen.vo.response.ChartResVo;
import com.ygkj.big.screen.vo.response.DilapProjResVo;
import com.ygkj.enums.StationType;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonCode;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/7/15
 */
@Component
@Slf4j
public class DilapidatedProjectManager extends AbstractUnitManager {

    @Autowired
    ProjectManageClient projectManageClient;

    private ConcurrentHashMap<String, Object> cache;

    public DilapidatedProjectManager(@Autowired ApplicationContext context) {
        super(context);
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        cache.clear();
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
            case "dilapProjList": {
                DilapProjReqVo reqVoParam = reqVo.getParam("reqVo", DilapProjReqVo.class);
                return dilapProjList(reqVoParam);
            }
            case "dilapProjStats": {
                Object result = null;
                if ((result = cache.get(buildDefaultCacheKey("dilapProjStats"))) == null) {
                    result = dilapProjStats();
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
        log.info("提交了一项病险工程缓存任务");
        cache.put(buildDefaultCacheKey("dilapProjStats"), dilapProjStats());
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每15分钟提交一次任务
     */
    @Scheduled(cron = "0 1/15 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }

    public List<ChartResVo<String, Long>> dilapProjStats() {
        List<ChartResVo<String, Long>> result = new ArrayList<>(4);
        CommonResult<PageVO<LinkedHashMap>> resp = projectManageClient.dilapProjList(Builder.of(DilapProjReqVo::new)
                .build());
        // 这个顺序不能乱
        String[] projTypes = {"水库", "海塘", "水闸", "山塘"};
        Map<String, Long> map = Collections.emptyMap();
        if (resp.getCode() == CommonCode.SUCCESS.code() && CollectionUtils.isNotBlank(resp.getData().getList())) {
            List<DilapProjResVo> data = JSONArray.parseArray(JSONArray.toJSONString(resp.getData().getList(), SerializerFeature.WriteMapNullValue), DilapProjResVo.class);
            map = data.stream().collect(Collectors.groupingBy(DilapProjResVo::getProjectType, Collectors.counting()));
        }
        for (String projType : projTypes) {
            result.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, "病险" + projType)
                    .with(ChartResVo::setValue, map.getOrDefault(projType, 0L))
                    .build());
        }
        return result;
    }

    /**
     * 病险工程列表
     *
     * @return
     */
    public CommonResult dilapProjList(DilapProjReqVo reqVo) {
        if (reqVo == null) {
            reqVo = new DilapProjReqVo();
        }
        reqVo.setResWaterLevel(true);
        return projectManageClient.dilapProjList(reqVo);
    }
}
