package com.ygkj.service;

import com.ygkj.vo.req.ChooseOperatorReqVo;
import com.ygkj.vo.req.ProcessCustomizePageReqVo;
import com.ygkj.vo.resp.OperatorRespVo;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;

import java.util.List;

/**
 * @author: huangzh
 * @Date: 2020/10/10 09:39
 * @Description: 流程定制-service
 */
public interface ProcessCustomizeService {

    /**
     * 查询所有水利用户，包含已选择的人员
     *
     * @param vo
     * @return CommonResult
     */
    CommonResult<PageVO<OperatorRespVo>> queryOperator(ProcessCustomizePageReqVo vo);

    /**
     * 选择操作人员
     *
     * @param vo
     * @return CommonResult
     */
    CommonResult chooseOperator(ChooseOperatorReqVo vo);

    /**
     * 根据步骤获取已选择的操作人员
     *
     * @param step
     * @return CommonResult
     */
    CommonResult<List<OperatorRespVo>> getOperators(Integer step);
}
