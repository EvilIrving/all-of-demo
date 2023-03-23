package com.ygkj.controller;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.HomeService;
import com.ygkj.utils.DataResult;
import com.ygkj.vo.resp.HomeRespVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: HomeController
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/10/25 21:20
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/10/25 21:20
 * @Version: 0.0.1
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "首页数据")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    @ApiOperation(value = "获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request) {
        String accessToken = request.getHeader("authorization");
        String system = request.getParameter("system");
        /**
         * 通过access_token拿userId
         */
        String userId = JwtTokenUtil.getUserId(accessToken);
        DataResult<HomeRespVO> result = DataResult.success();
        result.setData(homeService.getHomeInfo(userId, system));
        return result;
    }

    @GetMapping("/myTrajectory")
    @ApiOperation(value = "我的轨迹")
    public CommonResult myTrajectory() {
        return CommonResult.success(homeService.myTrajectory());
    }


}
