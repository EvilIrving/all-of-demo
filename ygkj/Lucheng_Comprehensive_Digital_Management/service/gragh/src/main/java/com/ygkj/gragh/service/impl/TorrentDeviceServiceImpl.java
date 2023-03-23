package com.ygkj.gragh.service.impl;

import com.alibaba.fastjson.JSON;
import com.ygkj.gragh.mapper.TorrentDeviceDataMapper;
import com.ygkj.gragh.model.TorrentDeviceData;
import com.ygkj.gragh.service.TorrentDeviceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author lxl
 * @create 2022-04-08 17:45
 * @description
 */
@Service
@Log4j2
public class TorrentDeviceServiceImpl implements TorrentDeviceService {

    @Resource
    private TorrentDeviceDataMapper torrentDeviceDataMapper;


    @Override
    public Object pushData(String timingData) {
        try {
            TorrentDeviceData torrentDeviceData = JSON.parseObject(timingData, TorrentDeviceData.class);
            torrentDeviceDataMapper.insert(torrentDeviceData);
        }catch (Exception e){
            log.info("数据推送异常,{}",e.getMessage());
        }
        return null;
    }
}
