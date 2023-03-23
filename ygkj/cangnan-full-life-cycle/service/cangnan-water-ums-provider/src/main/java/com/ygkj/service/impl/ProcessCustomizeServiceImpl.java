package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.entity.ProcessCustomize;
import com.ygkj.mapper.ProcessCustomizeMapper;
import com.ygkj.service.ProcessCustomizeService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.ChooseOperatorReqVo;
import com.ygkj.vo.req.ProcessCustomizePageReqVo;
import com.ygkj.vo.resp.OperatorRespVo;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author: huangzh
 * @Date: 2020/10/10 09:40
 * @Description:
 */
@Service
public class ProcessCustomizeServiceImpl implements ProcessCustomizeService {

    @Resource
    private ProcessCustomizeMapper processCustomizeMapper;

    @Override
    public CommonResult<PageVO<OperatorRespVo>> queryOperator(ProcessCustomizePageReqVo vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        // 获取所有水利用户
        List<OperatorRespVo> operatorRespVos = processCustomizeMapper.queryAllWaterUsers();

        String operator = null;
        Integer step = vo.getStep();

        // 操作步骤为null或者不在步骤范围内，返回所有水利用户
        if (step == null || step > 5 || step < 1) {
            return CommonResult.success(PageUtils.getPageVO(operatorRespVos));
        }
        // step = 2 或 5 获取经办人
        if (step == 2 || step == 5) {
            operator = processCustomizeMapper.getAssignees(2);
        }

        // step = 3 获取审核人员
        if (step == 3) {
            operator = processCustomizeMapper.getAssignees(3);
        }

        // step = 4 获取签批领导
        if (step == 4) {
            operator = processCustomizeMapper.getAssignees(4);
        }

        // 获取操作人员为空
        if (StringUtils.isBlank(operator)) {
            return CommonResult.success(PageUtils.getPageVO(operatorRespVos));
        }

        // 操作人员不为空
        List<String> operators = Arrays.asList(operator.split(","));
        for (OperatorRespVo operatorRespVo : operatorRespVos) {
            operatorRespVo.setChoose(false);
            if (operators.contains(operatorRespVo.getId())) {
                operatorRespVo.setChoose(true);
            }
        }


        return CommonResult.success(PageUtils.getPageVO(operatorRespVos));
    }

    @Override
    public CommonResult chooseOperator(ChooseOperatorReqVo vo) {

        Integer step = vo.getStep();

        // 操作步骤为null或者不在步骤范围内
        if (step == null || step > 5 || step < 1) {
            return CommonResult.failed("操作失败，请选择具体操作步骤");
        }
        if (StringUtils.isBlank(vo.getOperator())) {
            return CommonResult.failed("操作人员为空，请选择及具体操作人员");
        }

        ProcessCustomize processCustomize = new ProcessCustomize();
        ProcessCustomize pc = new ProcessCustomize();

        // step = 2 或 5，查询是否存在该步骤经办人信息
        if (step == 2 || step == 5) {
            pc = processCustomizeMapper.getProcessCustomize(2);
            processCustomize.setStep(2);
        }

        // step = 3 查询是否存在该步骤审核人员
        if (step == 3) {
            pc = processCustomizeMapper.getProcessCustomize(3);
            processCustomize.setStep(3);

        }

        // step = 4 查询是否存在该步骤签批领导
        if (step == 4) {
            pc = processCustomizeMapper.getProcessCustomize(4);
            processCustomize.setStep(4);
        }

        processCustomize.setId(UUID.randomUUID().toString());
        processCustomize.setOperator(vo.getOperator());
        if (pc == null || pc.getId() == null) {
            // 插入经办人员
            processCustomizeMapper.addOperator(processCustomize);
        } else {
            // 更新经办人员
            processCustomizeMapper.updateOperator(processCustomize);
        }

        return CommonResult.success("");
    }

    @Override
    public CommonResult<List<OperatorRespVo>> getOperators(Integer step) {
        if (step == null || step > 5 || step < 1) {
            return CommonResult.failed("操作失败，请选择具体操作步骤");
        }

        if (step == 1) {
            // 返回所有企业用户
            return CommonResult.success(processCustomizeMapper.queryAllWaterCompanyUsers());
        }

        List<OperatorRespVo> operatorRespVos = new ArrayList<>();
        String operator = null;

        if (step == 2 || step == 5) {
            // 获取已选择的经办人
            operator = processCustomizeMapper.getAssignees(2);
        }

        if (step == 3) {
            // 获取已选择的审核人
            operator = processCustomizeMapper.getAssignees(3);
        }

        if (step == 4) {
            // 获取已选择的签批领导
            operator = processCustomizeMapper.getAssignees(4);
        }

        if (StringUtils.isBlank(operator)) {
            // 操作人员为空
            return CommonResult.success(operatorRespVos);
        }

        // 获取操作人员id列表
        List<String> list = Arrays.asList(operator.split(","));

        // 根据操作人员id列表获取对应人员信息
        operatorRespVos = processCustomizeMapper.getUsers(list);
        return CommonResult.success(operatorRespVos);
    }
}
