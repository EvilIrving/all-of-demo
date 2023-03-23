package com.ygkj.visualization.service.impl;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.visualization.mapper.CollectPointMapper;
import com.ygkj.visualization.model.CollectPoint;
import com.ygkj.visualization.service.CollectPointService;
import com.ygkj.visualization.service.WeatherService;
import com.ygkj.visualization.vo.request.CollectPointAddReqVo;
import com.ygkj.visualization.vo.response.CollectPointResVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author xq
 * 
 * @Date 2021/2/26
 */
@Service
public class CollectPointServiceImpl implements CollectPointService {
    Logger logger = LoggerFactory.getLogger(CollectPointServiceImpl.class);
    @Resource
    CollectPointMapper collectPointMapper;
    @Resource
    GenerateDistributionIDService idService;
    @Autowired
    private WeatherService weatherService;

    ExecutorService exec = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    final static long timeOut = 2;

    @PreDestroy
    public void destroy(){
        exec.shutdownNow();
    }

    @Override
    public List<CollectPointResVo> getAllPoint(String name) {

        DecimalFormat decimalFormat=new DecimalFormat("0.##");
        List<CollectPoint> list = collectPointMapper.selectAll(JwtTokenUtil.getCurrentUserId().orElse(getCurrentUserId())
                , name);
        List<CollectPointResVo> result = new ArrayList<>();
        //六小时未来降雨量
        for (CollectPoint point : list) {
            CollectPointResVo collectPointResVo = new CollectPointResVo();
            BeanUtils.copyProperties(point, collectPointResVo);
            Callable<Double> call = () -> weatherService.rainfallWaterFromSiChuang(String.valueOf(point.getLongitude())
                    , String.valueOf(point.getLatitude())).getDouble("sixHourTotalRainfall");
            try {
                Future<Double> future = exec.submit(call);
                collectPointResVo.setSixHourRainfall(Double.valueOf(decimalFormat.format(future.get(timeOut, TimeUnit.SECONDS))));
            } catch (TimeoutException ex) {
                logger.debug("四创六小时未来降雨量请求超时", ex.getStackTrace());
                collectPointResVo.setSixHourRainfall(0D);
            } catch (Exception e) {
                logger.debug("四创六小时未来降雨量请求错误", e.getStackTrace());
                collectPointResVo.setSixHourRainfall(0D);
            }
            result.add(collectPointResVo);
        }
        return result;
    }

    @Override
    public Object addPoint(CollectPointAddReqVo collectPoint) {
        CollectPoint val = new CollectPoint();
        BeanUtils.copyProperties(collectPoint, val);
        String id = idService.nextID();
        val.setId(id);
        val.setUserId(getCurrentUserId());
        collectPointMapper.insert(val);
        return id;
    }

    @Override
    public Object deletePoint(String id) {
        return this.collectPointMapper.deleteByIdList(Arrays.asList(id), getCurrentUserId());
    }

    private String getCurrentUserId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("authorization");
        String userId = null;
        if (StringUtils.isNotBlank(token)) {
            userId = JwtTokenUtil.getUserId(token);
        }
        return userId;
    }
}
