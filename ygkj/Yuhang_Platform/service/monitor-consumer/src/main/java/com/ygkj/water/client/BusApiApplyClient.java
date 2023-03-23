package com.ygkj.water.client;


import com.ygkj.soft.database.api.VO.*;
import com.ygkj.soft.database.api.model.ApiApplyStatusEnum;
import com.ygkj.soft.database.api.model.BusApiApply;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import java.util.List;

@FeignClient(contextId = "BusApiApply",name = "soft-database")
public interface BusApiApplyClient {

    @PostMapping("/busApiApply/queryMyAuditTodo")
    public CommonResult queryMyAuditTodo(ApiApplyQueryVo queryVO) throws Exception;

    @PostMapping("/busApiApply/queryAuditRecordsFromMe")
    public CommonResult queryAuditRecordsFromMe(ApiApplyQueryVo queryVO) throws Exception;

    @GetMapping("/busApiApply/loadApply/{applyId}")
    public CommonResult loadApply(@PathVariable("applyId") String applyId) throws Exception;

    @PostMapping("/busApiApply/startApply")
    public CommonResult startApply(ApiApplyUpdateVo updateVo) throws Exception;

    @PostMapping("/busApiApply/reject")
    public CommonResult reject(@RequestParam("applyId") String applyId,
                               @RequestParam("remark") String remark) throws Exception;

    @PostMapping("/busApiApply/agree")
    public CommonResult agree(@RequestParam("applyId") String applyId) throws Exception;

    @DeleteMapping("/busApiApply/delApply/{applyId}")
    public CommonResult delApply(@PathVariable("applyId") String applyId) throws Exception ;

}
