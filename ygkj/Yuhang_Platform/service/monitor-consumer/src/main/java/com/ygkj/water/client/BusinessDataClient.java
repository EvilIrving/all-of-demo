package com.ygkj.water.client;

import com.ygkj.soft.database.api.VO.BusinessDataQueryVO;
import com.ygkj.soft.database.api.VO.SoftDataAddVO;
import com.ygkj.soft.database.api.VO.SoftDataQueryVO;
import com.ygkj.soft.database.api.VO.SoftDataUpdateVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import javax.validation.Valid;

/**
 * @author Huang.zh
 * @date 2020/9/9 16:10
 * @Description: feign调用业务数据相关服务封装
 */
@FeignClient(contextId = "businessData",name = "authentication-system")
public interface BusinessDataClient {

    @PostMapping("/softData/queryBusinessDataByPage")
    CommonResult queryBusinessDataByPage(BusinessDataQueryVO queryVO);

    @PostMapping("/softData/insertSoftData")
    CommonResult insertSoftData(SoftDataAddVO softDataAddVO);

    @PostMapping("/softData/querySoftData")
    CommonResult querySoftData(SoftDataQueryVO softDataQueryVO);

    @PostMapping("/softData/updateSoftDataStatus")
    CommonResult updateSoftDataStatus(SoftDataUpdateVO softDataUpdateVO);

    @PostMapping("/businessData/generateApi")
    CommonResult generateApi(@RequestParam("dataSourceName") String dataSourceName,@RequestParam("tableName") String tableName);
}
