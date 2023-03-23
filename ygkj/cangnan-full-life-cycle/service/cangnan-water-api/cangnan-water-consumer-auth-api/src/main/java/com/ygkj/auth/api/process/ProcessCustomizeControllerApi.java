package com.ygkj.auth.api.process;

import com.ygkj.auth.VO.request.ChooseOperatorReqVo;
import com.ygkj.auth.VO.request.ProcessCustomizePageReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * @author: huangzh
 * @Date: 2020/10/10 15:45
 * @Description:
 */
@Api(value = "ProcessCustomizeControllerApi", tags = "流程定制")
public interface ProcessCustomizeControllerApi {

    @ApiOperation(value = "根据步骤查看操作人员")
    Object queryOperator(ProcessCustomizePageReqVo vo);

    @ApiOperation(value = "选择操作人员接口")
    Object chooseOperator(ChooseOperatorReqVo vo);

    @ApiOperation(value = "获取已经选择的操作人员")
    @ApiImplicitParam(name = "step", value = "步骤(1企业申请，2经办人受理，3审批，4领导签批，5校对)", paramType = "query")
    Object getOperators(Integer step);
}
