package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.big.screen.model.HeavyRainsArchive;
import com.ygkj.vo.PageVO;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/6 13:51
 * @Description:
 */
public interface HeavyRainsArchiveService extends IService<HeavyRainsArchive> {

    String precipitation(HeavyRainsArchive heavyRainsArchive);

    List<JSONObject> hourlyRainfallvalue(HeavyRainsArchive heavyRainsArchive);
}
