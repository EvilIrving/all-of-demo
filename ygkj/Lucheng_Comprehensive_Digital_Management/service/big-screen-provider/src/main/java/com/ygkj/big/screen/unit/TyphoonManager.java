package com.ygkj.big.screen.unit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.feign.MeteorologicVisualizationClient;
import com.ygkj.big.screen.feign.ProjectManageClient;
import com.ygkj.big.screen.vo.request.TyphoonProjectReqVo;
import com.ygkj.big.screen.vo.request.UnitDataBuilderReqVo;
import com.ygkj.big.screen.vo.request.UnitDataReqVo;
import com.ygkj.big.screen.vo.response.CurTyphoonReqVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.DistanceUtil;
import com.ygkj.water.model.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Component
@Slf4j
public class TyphoonManager extends AbstractUnitManager {

    @Autowired
    ProjectManageClient projectManageClient;

    @Autowired
    MeteorologicVisualizationClient meteorologicVisualizationClient;

    private ConcurrentHashMap<String, Object> cache;

    public TyphoonManager(@Autowired ApplicationContext context) {
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
            case "typhoonStats4C": {
                CurTyphoonReqVo param = reqVo.getParam("CurTyphoonReqVo", CurTyphoonReqVo.class);
                if (param != null && !param.isAllNull()) {
                } else {
                    Object result = cache.get("typhoonStats4C");
                    if (result != null) {
                        return result;
                    }
                }
                return computeTyphoonStats4C(param);
            }
            default:
                return null;
        }
    }

    @Override
    public void buildData(UnitDataBuilderReqVo reqVo) {
        log.info("提交了一项台风缓存任任务");
        cache.put("typhoonStats4C", computeTyphoonStats4C(null));
    }

    @Override
    public ExecutorService getExecutor() {
        return getDefaultConfigExecutor();
    }

    /**
     * 每十分钟提交一次任务
     */
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void updateCacheTask() {
        doBuild(null);
    }


    /**
     * 台风组件，C位统计
     *
     * @param param
     * @return
     */
    public CommonResult<JSONObject> computeTyphoonStats4C(CurTyphoonReqVo param) {
        Double distance = 0D;
        Integer projectNum = 0;
        String code = null;
        Integer power = null;
        String name = null;
        String englishName = null;
        Date time = null;
        if (param != null && !param.isAllNull()) {
            code = param.getCode();
            power = param.getPower();
            time = param.getTime();
        } else {
            param = param == null ? new CurTyphoonReqVo() : param;
            // 1. 先拿到最新的一条台风
            CommonResult resp = meteorologicVisualizationClient.newestTyphoon();
            if (resp.getData() != null) {
                JSONObject typhoon = (JSONObject) JSONObject.toJSON(resp.getData());
                code = typhoon.getString("code");
                name = typhoon.getString("name");
                englishName = typhoon.getString("english_name");
                String s = typhoon.getString("points");
                if (StringUtils.isNotBlank(s)) {
                    Optional<JSONObject> lastPointOpt = JSONArray.parseArray(s).toJavaList(JSONObject.class).stream().filter(o -> o.getDate("time") != null).max(Comparator.comparing(o -> o.getDate("time")));
                    if (lastPointOpt.isPresent()) {
                        JSONObject lastPoint = lastPointOpt.get();
                        JSONObject radius10_quad = lastPoint.getJSONObject("radius10_quad");
                        if (radius10_quad != null) {
                            param.setNe(radius10_quad.getDouble("ne"));
                            param.setNw(radius10_quad.getDouble("nw"));
                            param.setSe(radius10_quad.getDouble("se"));
                            param.setSw(radius10_quad.getDouble("sw"));
                        }
                        param.setLat(lastPoint.getDouble("lat"));
                        param.setLng(lastPoint.getDouble("lng"));
                        power = lastPoint.getInteger("power");
                        time = lastPoint.getDate("time");
                    }
                }
            }
        }
        if (!param.isAllNull()) {
            TyphoonProjectReqVo finalParam = Builder.of(TyphoonProjectReqVo::new)
                    .with(TyphoonProjectReqVo::setType, 2)
                    .with(TyphoonProjectReqVo::setLng, param.getLng())
                    .with(TyphoonProjectReqVo::setLat, param.getLat())
                    .with(TyphoonProjectReqVo::setSe, param.getSe())
                    .with(TyphoonProjectReqVo::setSw, param.getSw())
                    .with(TyphoonProjectReqVo::setNe, param.getNe())
                    .with(TyphoonProjectReqVo::setNw, param.getNw())
                    .build();
            CommonResult<Integer> resp = projectManageClient.countTyphoonAllRealProject(finalParam);
            if (resp != null && resp.getData() != null) {
                projectNum = resp.getData();
            }
            // 计算距离温州的距离
            double lng = 120.707312d, lat = 28.001819d;
            distance = DistanceUtil.getDistance(lat, lng, finalParam.getLat(), finalParam.getLng()).doubleValue();
        }

        return CommonResult.success(Builder.of(JSONObject::new)
                .with(JSONObject::put, "distance", distance)
                .with(JSONObject::put, "projectNum", projectNum)
                .with(JSONObject::put, "code", code)
                .with(JSONObject::put, "power", power)
                .with(JSONObject::put, "name", name)
                .with(JSONObject::put, "englishName", englishName)
                .with(JSONObject::put, "time", time)
                .build());
    }

}
