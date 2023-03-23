package com.ygkj.water.project.feign;

import com.ygkj.auth.VO.request.LoginReqVO;
import com.ygkj.auth.VO.response.LoginRespVO;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface UserFeignClient {

    @GetMapping("/sys/users")
    CommonResult users(@RequestParam(value = "ids", required = false) String ids);



    @GetMapping("/sys/putUserChooseProject")
    void putUserChooseProject(@RequestParam("key") String key, @RequestParam("jsonString") String jsonString);

    @GetMapping("/sys/getUserChooseProject")
    String getUserChooseProject(@RequestParam("key") String key);


    @GetMapping("/sys/userList")
    CommonResult userList(@RequestParam("key") String key);

    @GetMapping("/sys/user/{id}")
    SysUser detailInfo(@PathVariable("id") String id);

    @PostMapping("/sys/getOutsideToken")
    CommonResult getOutsideToken(HttpServletRequest request, @RequestParam("prcd")String prcd);

    @GetMapping("/sys/delWyhPerson")
    void delWyhPerson(@RequestParam("wyhId")String wyhId);

    @PostMapping(value = "/sys/user/login")
    LoginRespVO login(LoginReqVO vo);

    @PostMapping("/file/load")
    CommonResult load(@RequestParam("ids") String ids);

}
