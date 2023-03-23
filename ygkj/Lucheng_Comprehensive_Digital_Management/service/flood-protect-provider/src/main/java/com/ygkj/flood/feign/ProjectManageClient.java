package com.ygkj.flood.feign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(contextId = "project-manage", name = "project-manage")
public interface ProjectManageClient {

    @GetMapping("/warningUser/addressBookList")
    List<JSONObject> addressBookList(@RequestParam(required = false) String key);


    @GetMapping("/threepeople/threeChargeTree")
    List<JSONObject> threeChargeTree(@RequestParam(required = false) String projectName);

}
