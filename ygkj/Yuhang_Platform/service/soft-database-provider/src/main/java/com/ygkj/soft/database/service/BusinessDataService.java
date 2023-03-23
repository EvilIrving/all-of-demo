package com.ygkj.soft.database.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.BusDataResource;
import com.ygkj.soft.database.api.model.SoftData;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/8 17:13
 * @Description: 业务数据服务层抽象
 */
public interface BusinessDataService {

    Pager queryBusinessDataByPage(BusinessDataQueryVO queryVO, EasyuiPager easyuiPager);

    void insertSoftData(SoftDataAddVO softDataAddVO) throws Exception;

    JSONObject querySoftData(SoftDataQueryVO softDataQueryVO, EasyuiPager easyuiPager) throws Exception;

    void updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO) throws Exception;

    void addResource(BusDataResource busDataResource) throws Exception;

    List<BusDataResource> findDataResource(BusDataResource busDataResource);

    void divideTableResource(TableDivideVO tableDivideVO) throws Exception;

    void deleteBusDataResource(String resourceId) throws Exception;

    void updateBusDataResource(BusDataResource busDataResource) throws Exception;

    void divideBusinessData(DataSourceDivideVO divideVO) throws Exception;

}