package com.ygkj.big.screen.feign;


import com.ygkj.gragh.model.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;

/**
 * @author xq
 * @Description
 */
@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface AuthenticationClient {

    /**
     * 文件上传
     *
     * @param uploadFile
     * @return
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    LinkedHashMap upload(@RequestPart(value = "uploadFile") MultipartFile uploadFile);


    @GetMapping("sys/user/{id}")
    SysUser detailInfo(@PathVariable("id") String id);

}
