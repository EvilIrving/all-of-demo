package com.ygkj.auth.api.upload;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Huang.zh
 * @date 2020/9/2 13:56
 * @Description: 文件上传服务调用方抽象
 */
@Api(tags = "文件上传服务")
public interface UploadApi {

    @ApiOperation(value = "上传文件")
    Object upload(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile);
}
