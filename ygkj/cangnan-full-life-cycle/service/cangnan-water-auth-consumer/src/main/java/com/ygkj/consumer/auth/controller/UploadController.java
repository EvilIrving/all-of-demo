package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.api.upload.UploadApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Huang.zh
 * @date 2020/9/2 13:54
 * @Description: 文件上传服务消费方
 */
@RestController
@RequestMapping("/upload")
public class UploadController implements UploadApi {
    @Autowired
    private AuthenticationClient authenticationClient;

    @Override
    @PostMapping(value = "",headers = "content-type=multipart/form-data")
    public Object upload(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile){
        return authenticationClient.upload(uploadFile);
    }
}
