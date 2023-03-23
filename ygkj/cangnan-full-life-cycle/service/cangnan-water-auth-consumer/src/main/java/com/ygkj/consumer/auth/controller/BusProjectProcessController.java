package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.ProjectAssigneeOpinionReqVo;
import com.ygkj.auth.VO.request.ProjectCheckInfoReqVo;
import com.ygkj.auth.api.monitor.BusProjectProcessControllerApi;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: huangzh
 * @Date: 2020/9/21 10:13
 * @Description:
 */
@RestController
@RequestMapping("/projectProcess")
public class BusProjectProcessController implements BusProjectProcessControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @Override
    @GetMapping("/getProcessRecords/{id}")
    public Object getProcessRecords(@PathVariable("id") String id) {
        return authenticationClient.getProcessRecords(id);
    }

    @Override
    @PostMapping("/notAccept")
    public Object notAccept(ProjectAssigneeOpinionReqVo vo) {
        return authenticationClient.notAccept(vo);
    }

    @Override
    @PostMapping("/correction")
    public Object correction(ProjectAssigneeOpinionReqVo vo) {
        return authenticationClient.correction(vo);
    }

    @Override
    @PostMapping("/accept")
    public Object accept(ProjectCheckInfoReqVo vo) {
        return authenticationClient.accept(vo);
    }

    @Override
    @PostMapping("/finish")
    public Object finish(ProjectAssigneeOpinionReqVo vo) {
        return authenticationClient.finish(vo);
    }

    @Override
    @PostMapping("/callback")
    public Object callback(ProjectAssigneeOpinionReqVo vo) {
        return authenticationClient.callback(vo);
    }

    @Override
    @PostMapping("/check")
    public Object check(ProjectCheckInfoReqVo vo) {
        return authenticationClient.check(vo);
    }

    @Override
    @PostMapping("/approval")
    public Object approval(ProjectCheckInfoReqVo vo) {
        return authenticationClient.approval(vo);
    }

    @Override
    @PostMapping("/saveCheckInfo")
    public Object saveCheckInfo(ProjectCheckInfoReqVo vo) {
        return authenticationClient.saveCheckInfo(vo);
    }

    @Override
    @PostMapping("/proofreader")
    public Object proofreader(ProjectAssigneeOpinionReqVo vo) {
        return authenticationClient.proofreader(vo);
    }

    @Override
    @GetMapping("/getProjectLicense/{id}")
    public Object getProjectLicense(@PathVariable("id") String id) {
        return authenticationClient.getProjectLicense(id);
    }

    @Override
    @GetMapping("/getProjectCheckInfo/{id}")
    public Object getProjectCheckInfo(@PathVariable("id") String id) {
        return authenticationClient.getProjectCheckInfo(id);
    }
}
