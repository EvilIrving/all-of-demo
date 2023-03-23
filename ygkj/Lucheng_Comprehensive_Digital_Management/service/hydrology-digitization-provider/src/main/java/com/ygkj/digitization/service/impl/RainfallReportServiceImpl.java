package com.ygkj.digitization.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.digitization.service.RainfallReportService;
import com.ygkj.digitization.mapper.AttStBaseMapper;
import com.ygkj.digitization.vo.request.StationQueryVo;
import com.ygkj.digitization.vo.response.WaterLevelStationResVo;
import com.ygkj.enums.StationType;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("rainfallReportService")
public class RainfallReportServiceImpl implements RainfallReportService {

    @Autowired
    private AttStBaseMapper stBaseMapper;

    /**
     * 河网水位
     * @param queryVo
     * @return
     */
    @Override
    public JSONObject riverwaterLevel(StationQueryVo queryVo) {
        if (StringUtils.isEmpty(queryVo.getStationType())) {
            queryVo.setStationType("RR");
        }
        List<WaterLevelStationResVo> list = stBaseMapper.selectWaterLevelStationByCondition(queryVo);//测站集合
        String[] tableNames = DateUtils.getTableNamesByTimeInterval(StationType.RAINFALL.table(),
                queryVo.getStartTime(), queryVo.getEndTime());//表名称list



        return null;
    }
}
