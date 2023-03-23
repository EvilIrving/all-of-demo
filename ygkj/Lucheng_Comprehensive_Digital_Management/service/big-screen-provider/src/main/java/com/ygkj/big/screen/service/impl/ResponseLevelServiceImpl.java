package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.big.screen.mapper.ResponseLevelMapper;
import com.ygkj.big.screen.model.ResponseLevel;
import com.ygkj.big.screen.service.ResponseLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/9 14:03
 * @Description:
 */
@Service
public class ResponseLevelServiceImpl extends ServiceImpl<ResponseLevelMapper, ResponseLevel> implements ResponseLevelService {

    @Autowired
    private ResponseLevelMapper responseLevelMapper;

    @Override
    public List<JSONObject> getAllHoursByDate(String date) {
        return responseLevelMapper.getAllHoursByDate(date);
    }

    @Override
    public List<JSONObject> getAllMonthByDate(String date) {
        return responseLevelMapper.getAllMonthByDate(date);
    }
}
