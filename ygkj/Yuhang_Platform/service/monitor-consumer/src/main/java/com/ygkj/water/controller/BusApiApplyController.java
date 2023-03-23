package com.ygkj.water.controller;

import com.ygkj.soft.database.api.ApiApplyApi;
import com.ygkj.soft.database.api.VO.ApiApplyQueryVo;
import com.ygkj.soft.database.api.VO.ApiApplyUpdateVo;
import com.ygkj.water.client.BusApiApplyClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/busApiApply")
public class BusApiApplyController implements ApiApplyApi {

    @Autowired
    private BusApiApplyClient busApiApplyClient;

    @Override
    @PostMapping("/queryMyAuditTodo")
    public CommonResult queryMyAuditTodo(ApiApplyQueryVo queryVO) throws Exception {
        return busApiApplyClient.queryMyAuditTodo(queryVO);
    }

    @Override
    @PostMapping("/queryAuditRecordsFromMe")
    public CommonResult queryAuditRecordsFromMe(ApiApplyQueryVo queryVO) throws Exception {
        return busApiApplyClient.queryAuditRecordsFromMe(queryVO);
    }

    @Override
    @GetMapping("/loadApply/{applyId}")
    public CommonResult loadApply(@PathVariable("applyId") String applyId) throws Exception {
        return busApiApplyClient.loadApply(applyId);
    }

    @Override
    @PostMapping("/startApply")
    public CommonResult startApply(ApiApplyUpdateVo updateVo) throws Exception {
        return busApiApplyClient.startApply(updateVo);
    }

    @Override
    @PostMapping("/reject")
    public CommonResult reject(@RequestParam String applyId, @RequestParam String remark) throws Exception {
        return busApiApplyClient.reject(applyId,remark);
    }

    @Override
    @PostMapping("/agree")
    public CommonResult agree(@RequestParam String applyId) throws Exception {
        return busApiApplyClient.agree(applyId);
    }

    @Override
    @DeleteMapping("/delApply/{applyId}")
    public CommonResult delApply(@PathVariable("applyId") String applyId) throws Exception {
        return busApiApplyClient.delApply(applyId);
    }
}
