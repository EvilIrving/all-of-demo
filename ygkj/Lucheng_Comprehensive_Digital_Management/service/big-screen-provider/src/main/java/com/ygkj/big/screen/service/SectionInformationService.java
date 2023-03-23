package com.ygkj.big.screen.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.big.screen.model.SectionInformation;

import java.util.List;

/**
 * @author wgf
 * @date 2022/5/19 16:42
 * @Description:
 */
public interface SectionInformationService extends IService<SectionInformation> {
    /**
     * 戌浦江断面水位图数据
     * @param time
     * @return
     */
    List<JSONObject> hydrograph(String time,String startTime,String endTime);

    List<SectionInformation> fillData(List<SectionInformation> list);
}
