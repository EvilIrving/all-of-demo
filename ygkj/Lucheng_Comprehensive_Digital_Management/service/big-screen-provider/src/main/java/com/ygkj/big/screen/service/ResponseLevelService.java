package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.big.screen.model.ResponseLevel;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/9 14:01
 * @Description:
 */
public interface ResponseLevelService extends IService<ResponseLevel> {

    List<JSONObject> getAllHoursByDate(String date);

    List<JSONObject> getAllMonthByDate(String date);
}
