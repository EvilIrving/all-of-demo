package com.ygkj.soft.database.controller;

import com.ygkj.soft.database.service.BusApiApplyService;
import com.ygkj.soft.database.api.ApiApplyApi;
import com.ygkj.soft.database.api.VO.ApiApplyQueryVo;
import com.ygkj.soft.database.api.VO.ApiApplyUpdateVo;
import com.ygkj.soft.database.api.model.ApiApplyStatusEnum;
import com.ygkj.soft.database.api.model.BusApiApply;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;


/**
 *
 */
@RestController
@RequestMapping("/busApiApply")
public class ApiApplyController implements ApiApplyApi {

    @Autowired
    private BusApiApplyService busApiApplyService;

    @Override
    @PostMapping("/queryMyAuditTodo")
    public CommonResult queryMyAuditTodo(@RequestBody ApiApplyQueryVo queryVO) throws Exception {
        EasyuiPager easyuiPager = new EasyuiPager();
        BeanUtils.copyProperties(queryVO,easyuiPager);
        Pager pager = busApiApplyService.queryMyAuditTodo(queryVO,easyuiPager);
        return CommonResult.success(pager);
    }

    @Override
    @PostMapping("/queryAuditRecordsFromMe")
    public CommonResult queryAuditRecordsFromMe(@RequestBody ApiApplyQueryVo queryVO) throws Exception {
        EasyuiPager easyuiPager = new EasyuiPager();
        BeanUtils.copyProperties(queryVO,easyuiPager);
        Pager pager = busApiApplyService.queryAuditRecordsFromMe(queryVO,easyuiPager);
        return CommonResult.success(pager);
    }

    @Override
    @GetMapping("/loadApply/{applyId}")
    public CommonResult loadApply(@PathVariable("applyId") String applyId) throws Exception {
        BusApiApply load = busApiApplyService.load(applyId);
        return CommonResult.success(load);
    }

    @Override
    @PostMapping("/startApply")
    public CommonResult startApply(@RequestBody ApiApplyUpdateVo updateVo) throws Exception {
        boolean rs = busApiApplyService.insert(updateVo);
        return rs?CommonResult.success("申请成功"):CommonResult.failed("申请失败");
    }

    @Override
    @PostMapping("/reject")
    public CommonResult reject(@RequestParam String applyId, @RequestParam String remark) throws Exception {
        boolean rs = busApiApplyService.updateState(applyId, remark, ApiApplyStatusEnum.REJECTED);
        return rs?CommonResult.success("操作成功"):CommonResult.failed("操作失败");
    }

    @Override
    @PostMapping("/agree")
    public CommonResult agree(@RequestParam String applyId) throws Exception {
        boolean rs = busApiApplyService.updateState(applyId, null, ApiApplyStatusEnum.AUDIT_PASS);
        return rs?CommonResult.success("操作成功"):CommonResult.failed("操作失败");
    }

    @Override
    @DeleteMapping("/delApply/{applyId}")
    public CommonResult delApply(@PathVariable("applyId") String applyId) throws Exception {
        boolean rs = busApiApplyService.delete(applyId);
        return rs?CommonResult.success("操作成功"):CommonResult.failed("操作失败");
    }
}
