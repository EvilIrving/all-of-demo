package com.ygkj.data.management;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "开放服务调用", value = "开放服务调用")
public interface BusApiInvokeControllerApi {

//    Object test();

    CommonResult invokeApi(HttpServletRequest request, String apiRelease);
}
