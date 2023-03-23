package com.ygkj.controller;

import com.ygkj.service.ProcessCustomizeService;
import com.ygkj.vo.req.ChooseOperatorReqVo;
import com.ygkj.vo.req.ProcessCustomizePageReqVo;
import com.ygkj.vo.resp.OperatorRespVo;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: fml
 * @Date: 2020/10/9 16:52
 * @Description: 流程定制
 */
@Api(tags = "流程定制")
@RestController
@RequestMapping("/processCustomize")
public class ProcessCustomizeController {

    @Autowired
    private ProcessCustomizeService processCustomizeService;

    @ApiOperation(value = "根据步骤查看操作人员")
    @PostMapping("/queryOperator")
    public CommonResult<PageVO<OperatorRespVo>> queryOperator(@RequestBody @Valid ProcessCustomizePageReqVo vo){

        return processCustomizeService.queryOperator(vo);
    }

    @ApiOperation(value = "选择操作人员接口")
    @PostMapping("/chooseOperator")
    public CommonResult chooseOperator(@RequestBody @Valid ChooseOperatorReqVo vo){

        return processCustomizeService.chooseOperator(vo);
    }

    @ApiOperation(value = "获取已经选择的操作人员")
    @ApiImplicitParam(name = "step", value = "步骤(1企业申请，2经办人受理，3审批，4领导签批，5校对)", paramType = "query")
    @GetMapping("/getOperators")
    public CommonResult<List<OperatorRespVo>> getOperators(@RequestParam("step") Integer step){

        return processCustomizeService.getOperators(step);
    }
}
