package com.ygkj.consumer.auth.client.authentication;

import com.ygkj.auth.api.monitor.model.BusProject;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/24 9:06
 * @Description:
 */
//@FeignClient(name = "sca-water-wms")
public interface WmsClient {
    @GetMapping("/BusProject/queryClosestProject")
    CommonResult queryClosestProject();

    @GetMapping("/BusProject/getAllProjects")
    CommonResult<List<BusProject>> selectAll();
}
