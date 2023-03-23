package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.AssessmentInspection;
import com.ygkj.project.model.OverseeEvaluation;
import com.ygkj.project.vo.request.AssessmentInspectionReqVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.AssessmentInspectionService;
import com.ygkj.water.project.service.OverseeEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wgf
 * @date 2022/6/10 11:22
 * @Description:
 */
@RestController
@RequestMapping("overseeEvaluation")
public class OverseeEvaluationController {

    @Autowired
    private AssessmentInspectionService inspectionService;
    @Autowired
    private OverseeEvaluationService overseeEvaluationService;

    @PostMapping("savePjById")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult savePjById(OverseeEvaluation overseeEvaluation) {

        QueryWrapper<OverseeEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("db_id", overseeEvaluation.getDbId());
        OverseeEvaluation one = overseeEvaluationService.getOne(queryWrapper);
        if (one != null){
            return CommonResult.failed("您的评价数据已存在");
        }
        AssessmentInspectionReqVo vo = new AssessmentInspectionReqVo();
        vo.setId(overseeEvaluation.getDbId());
        PageVO<AssessmentInspection> load = (PageVO<AssessmentInspection>) inspectionService.load(vo);
        List<AssessmentInspection> list = load.getList();
        if (CollectionUtils.isNotBlank(list)){
            String userId = JwtTokenUtil.getCurrentUserId().orElse("");
            overseeEvaluation.setUserId(userId);
            AssessmentInspection assessmentInspection = list.get(0);
            //查看是否是处理人。处理人可以给出评价
            if (!"2".equals(assessmentInspection.getArchivingStatus())){
                return CommonResult.failed("当前任务尚未归档!");
            }
            if (!userId.equals(assessmentInspection.getResponsiblePeople())){
                return CommonResult.failed("当前用户不是该任务责任人,没有权限编辑!");
            }
            //保存数据
            if (overseeEvaluationService.save(overseeEvaluation)){
                AssessmentInspection inspection = new AssessmentInspection();
                inspection.setId(vo.getId());
                inspection.setEvaluationStatus("2");
                inspectionService.updateById(inspection);
                return CommonResult.success("保存成功");
            }
        }
        return CommonResult.failed("保存失败");
    }

    @PostMapping("selectPjById")
    public CommonResult selectPjById(OverseeEvaluation overseeEvaluation) {
        QueryWrapper<OverseeEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("db_id", overseeEvaluation.getDbId());
        queryWrapper.eq(StringUtils.isNotBlank(overseeEvaluation.getId()),"id", overseeEvaluation.getId());
        OverseeEvaluation one = overseeEvaluationService.getOne(queryWrapper);
        if (one != null){
            String userName = overseeEvaluationService.getUserName(one.getUserId());
            one.setName(userName);
            return CommonResult.success(one);
        } else {
            return CommonResult.failed("查询的数据不存在");
        }
    }

    @PostMapping("getWaterDept")
    public CommonResult getWaterDept(String deptId) {
        String id = overseeEvaluationService.getWaterDept(deptId);
        if (StringUtils.isNotBlank(id)){
            return CommonResult.success("true");
        }else {
            return CommonResult.success("false");
        }
    }
}
