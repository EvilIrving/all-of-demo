package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.SectionConfiguration;
import com.ygkj.project.model.SectionStorm;

import java.util.List;


/**
 * @author wgf
 * @date 2022/5/10 17:39
 * @Description:
 */
public interface SectionConfigurationService extends IService<SectionConfiguration> {

    JSONObject automaticAcquisition();

    JSONObject windows(String id);

    List<SectionConfiguration> forecastSectionPage(List<SectionConfiguration> list);

    List<SectionStorm> forecastSectionInfo(String sectionId);

    /**
     * 获取温州平台接口的token
     * @return
     */
    String getToken();

    List<JSONObject> forecastMaxHour(SectionConfiguration sectionConfiguration);

    JSONObject forecastSectionHour(String sectionId);
}
