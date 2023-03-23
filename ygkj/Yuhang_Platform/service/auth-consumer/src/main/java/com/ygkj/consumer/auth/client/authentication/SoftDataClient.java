package com.ygkj.consumer.auth.client.authentication;

import com.ygkj.soft.database.api.model.BusDataSource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/9 10:08
 * @Description:
 */
@FeignClient(contextId = "softData",name = "authentication-system")
public interface SoftDataClient {
    @GetMapping("/busDataSource")
    List<BusDataSource> getBusDataSource(BusDataSource busDataSource);
}
