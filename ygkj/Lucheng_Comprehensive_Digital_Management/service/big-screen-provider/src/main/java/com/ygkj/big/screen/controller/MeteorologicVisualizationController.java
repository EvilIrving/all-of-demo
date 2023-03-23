package com.ygkj.big.screen.controller;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.big.screen.MeteorologicVisualizationControllerApi;
import com.ygkj.big.screen.service.MeteorologicVisualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/20
 */
@RestController
@RequestMapping("meteorologic")
public class MeteorologicVisualizationController implements MeteorologicVisualizationControllerApi {

    @Autowired
    MeteorologicVisualizationService meteorologicVisualizationService;

    @GetMapping("forecast")
    @Override
    public List<JSONObject> forecast() {
        return meteorologicVisualizationService.forecast();
    }

    @GetMapping("meteorologicStatisticTemp")
    @Override
    public List<JSONObject> meteorologicStatisticTemp() {
        return meteorologicVisualizationService.meteorologicStatistic("温度");
    }

    @GetMapping("meteorologicStatisticRain")
    @Override
    public List<JSONObject> meteorologicStatisticRain() {
        return meteorologicVisualizationService.meteorologicStatistic("降雨量");
    }

    @GetMapping("meteorologicStatisticWind")
    @Override
    public List<JSONObject> meteorologicStatisticWind() {
        return meteorologicVisualizationService.meteorologicStatistic("风速");
    }

    @GetMapping("typhoonStatistic")
    @Override
    public List<JSONObject> typhoonStatistic() {
        return meteorologicVisualizationService.typhoonStatistic();
    }

    @GetMapping("typhoonRecently")
    @Override
    public List<JSONObject> typhoonRecently() {
        return meteorologicVisualizationService.typhoonRecent();
    }

    @GetMapping("similarTyphoon")
    @Override
    public List<JSONObject> similarTyphoon(@RequestParam("type") Integer type) {
        String myType = null;
        switch (type) {
            case 1:
                myType = "path";
                break;
            case 2:
                myType = "season";
                break;
            case 3:
                myType = "born_land";
                break;
            default:
                break;
        }
        if (myType == null) {
            return Collections.emptyList();
        } else {
            return meteorologicVisualizationService.similarTyphoon(myType);
        }
    }

    @GetMapping("realtimeRainfall")
    @Override
    public List<JSONObject> realtimeRainfall() {
        return meteorologicVisualizationService.realtimeRainfall();
    }

    @GetMapping("forecastRainfall")
    @Override
    public List<JSONObject> forecastRainfall() {
        return meteorologicVisualizationService.forecastRainfall();
    }

    @GetMapping("rainfallStatistic")
    @Override
    public List<JSONObject> rainfallStatistic(@RequestParam("type") Integer type) {
        String myType = null;
        switch (type) {
            case 1:
                myType = "1h";
                break;
            case 2:
                myType = "3h";
                break;
            case 3:
                myType = "24h";
                break;
            default:
                break;
        }
        if (myType == null) {
            return Collections.emptyList();
        } else {
            return meteorologicVisualizationService.rainfallStatistic(myType);
        }
    }

    @GetMapping("warnStatsNum")
    @Override
    public List<JSONObject> warnStatsNum(@RequestParam("type") Integer type) {
        String myType = null;
        switch (type) {
            case 1:
                myType = "1h";
                break;
            case 2:
                myType = "3h";
                break;
            case 3:
                myType = "24h";
                break;
            case 4:
                myType = "水位";
                break;
            default:
                break;
        }
        if (myType == null) {
            return Collections.emptyList();
        } else {
            return meteorologicVisualizationService.warnStatsNum(myType);
        }
    }

    @GetMapping("rsvrStorageTotal")
    @Override
    public List<JSONObject> rsvrStorageTotal(@RequestParam("type") Integer type) {
        return meteorologicVisualizationService.rsvrStorageTotal(type);
    }

    @GetMapping("rsvrStorageList")
    @Override
    public List<JSONObject> rsvrStorageList(@RequestParam("type") Integer type) {
        return meteorologicVisualizationService.rsvrStorageList(type);
    }

    @GetMapping("realtimeRainSt")
    @Override
    public List<JSONObject> realtimeRainSt() {
        return meteorologicVisualizationService.realtimeRainSt();
    }

    @GetMapping("realtimeWaterSt")
    @Override
    public List<JSONObject> realtimeWaterSt() {
        return meteorologicVisualizationService.realtimeWaterSt();
    }

    @GetMapping("rsvrPoint")
    @Override
    public List<JSONObject> rsvrPoint() {
        return meteorologicVisualizationService.rsvrPoint();
    }

    @GetMapping("typhoonPath")
    @Override
    public List<JSONObject> typhoonPath() {
        return meteorologicVisualizationService.typhoonPath();
    }

    @GetMapping("wagaPoint")
    @Override
    public List<JSONObject> wagaPoint() {
        return meteorologicVisualizationService.wagaPoint();
    }

    @GetMapping("floodPoint")
    @Override
    public List<JSONObject> floodPoint() {
        return meteorologicVisualizationService.floodPoint();
    }

    @GetMapping("innerRiverLevel")
    @Override
    public List<JSONObject> innerRiverLevel() {
        return meteorologicVisualizationService.innerRiverLevel();
    }

    @GetMapping("rsvrRisk")
    @Override
    public List<JSONObject> rsvrRisk(Integer type) {
        String myType = null;
        switch (type) {
            case 1:
                myType = "超防洪高";
                break;
            case 2:
                myType = "超汛限";
                break;
            default:
                break;
        }
        if (myType == null) {
            return Collections.emptyList();
        } else {
            return meteorologicVisualizationService.rsvrRisk(myType);
        }
    }

    @GetMapping("wagaRisk")
    @Override
    public List<JSONObject> wagaRisk(Integer type) {
        String myType = null;
        switch (type) {
            case 1:
                myType = "超警戒";
                break;
            case 2:
                myType = "超保证";
                break;
            default:
                break;
        }
        if (myType == null) {
            return Collections.emptyList();
        } else {
            return meteorologicVisualizationService.wagaRisk(myType);
        }
    }

    @GetMapping("drainageAbility")
    @Override
    public List<JSONObject> drainageAbility() {
        return meteorologicVisualizationService.drainageAbility();
    }

    @GetMapping("drainageAbilityTotal")
    @Override
    public List<JSONObject> drainageAbilityTotal() {
        return meteorologicVisualizationService.drainageAbilityTotal();
    }

    @GetMapping("riverWarnStats")
    @Override
    public List<JSONObject> riverWarnStats() {
        return meteorologicVisualizationService.riverWarnStats();
    }

    @GetMapping("floodPointWarnStats")
    @Override
    public List<JSONObject> floodPointWarnStats() {
        return meteorologicVisualizationService.floodPointWarnStats();
    }

    @GetMapping("antiDroughtStats")
    @Override
    public List<JSONObject> antiDroughtStats() {
        return meteorologicVisualizationService.antiDroughtStats();
    }
}
