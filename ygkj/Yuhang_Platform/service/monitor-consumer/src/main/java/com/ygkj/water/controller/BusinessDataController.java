package com.ygkj.water.controller;

import com.ygkj.soft.database.api.BusinessDataApi;
import com.ygkj.soft.database.api.VO.BusinessDataQueryVO;
import com.ygkj.soft.database.api.VO.SoftDataAddVO;
import com.ygkj.soft.database.api.VO.SoftDataQueryVO;
import com.ygkj.soft.database.api.VO.SoftDataUpdateVO;
import com.ygkj.water.client.BusinessDataClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Huang.zh
 * @date 2020/9/9 16:12
 * @Description: 业务数据服务控制层
 */
@RestController("/businessData")
public class BusinessDataController implements BusinessDataApi {

    @Autowired
    private BusinessDataClient businessDataClient;

    @PostMapping("/queryBusinessDataByPage")
    @Override
    public CommonResult queryBusinessDataByPage(BusinessDataQueryVO queryVO) {
        return businessDataClient.queryBusinessDataByPage(queryVO);
    }

    @PostMapping(value = "/insertSoftData")
    @Override
    public CommonResult insertSoftData(SoftDataAddVO softDataAddVO) {
        return businessDataClient.insertSoftData(softDataAddVO);
    }

    @PostMapping("/querySoftData")
    @Override
    public CommonResult querySoftData(SoftDataQueryVO softDataQueryVO) {
        return businessDataClient.querySoftData(softDataQueryVO);
    }

    @PostMapping("/updateSoftDataStatus")
    @Override
    public CommonResult updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO) {
        return businessDataClient.updateSoftDataStatus(softDataUpdateVO);
    }

    @PostMapping("/generateApi")
    @Override
    public CommonResult generateApi(String dataSourceName, String tableName) {
        return businessDataClient.generateApi(dataSourceName, tableName);
    }
}
