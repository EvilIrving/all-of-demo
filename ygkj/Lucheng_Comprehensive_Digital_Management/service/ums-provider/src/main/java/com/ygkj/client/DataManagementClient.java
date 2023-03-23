package com.ygkj.client;

import com.ygkj.data.management.model.BusDataTableCategory;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "data-management")
public interface DataManagementClient {

    @GetMapping("/busDataTableCategory/myCategory/{name}")
    CommonResult<List<BusDataTableCategory>> getMyCategory(@PathVariable String name);
}
