package com.ygkj.client;

import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author xq
 * @Description 用于请求行政区划相关数据
 * @Date 2021/1/13
 */
@FeignClient(name = "graph")
public interface AdcdClient {

    @PostMapping("/adcd/selectAdnmByAdcdList")
    CommonResult selectAdnmByAdcdList(@RequestBody List<String> adcdList);
}
