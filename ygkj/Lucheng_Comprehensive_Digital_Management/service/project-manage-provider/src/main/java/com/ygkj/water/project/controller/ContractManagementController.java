package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ygkj.gragh.model.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.ContractManagementControllerApi;
import com.ygkj.project.vo.request.*;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/contract")
public class ContractManagementController implements ContractManagementControllerApi {
    @Resource
    private ConCovenantService covenantService;
    @Resource
    private BmPropertyContractService contractService;
    @Resource
    private ConAnnualAcceptanceService annualAcceptanceService;
    @Resource
    private AssessmentInspectionService inspectionService;
    @Resource
    private ConAcceptanceCriteriaService criteriaService;
    @Resource
    private ConAcceptanceApplicationService applicationService;
    @Resource
    private ConAcceptanceApplicationNodeService applicationNodeService;
    @Resource
    private UserFeignClient userFeignClient;
    @Override
    @PostMapping("/saveOrUpdateCovenants")
    public CommonResult saveOrUpdateCovenant(BmPropertyContractReqVo vo) {
        try{
            if (StringUtils.isBlank(vo.getId())){
                contractService.insert(vo);
            }else {
                contractService.update(vo);
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadPageCovenant")
    public CommonResult loadPageCovenant(BmPropertyContractReqVo vo) {
        try{
            String userId = JwtTokenUtil.getCurrentUserId().orElse("");
            SysUser user = userFeignClient.detailInfo(userId);
            if (!ObjectUtils.isEmpty(user)){
                if ("3".equals(user.getUserType())){
                    vo.setContractUnit(user.getDeptId());
                }
            }
            return CommonResult.success(contractService.load(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/delCovenant")
    public CommonResult deleteCovenant(String id) {
        try{
            if (StringUtils.isBlank(id)){
                throw new RuntimeException("参数缺失");
            }
            contractService.delete(id);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/saveOrUpdateCriteria")
    public CommonResult saveOrUpdateCriteria(ConAcceptanceCriteriaReqVo vo) {
        try{
            String userId = JwtTokenUtil.getCurrentUserId().orElse("");
            SysUser user = userFeignClient.detailInfo(userId);
            if (!ObjectUtils.isEmpty(user)){
                if ("3".equals(user.getUserType())){
                    vo.setDeptId(user.getDeptId());
                }
            }
            if (StringUtils.isBlank(vo.getId())){
                criteriaService.insert(vo);
            }else {
                criteriaService.update(vo);
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadPageCriteria")
    public CommonResult loadPageCriteria(ConAcceptanceCriteriaReqVo vo) {
        try{
            String userId = JwtTokenUtil.getCurrentUserId().orElse("");
            SysUser user = userFeignClient.detailInfo(userId);
            if (!ObjectUtils.isEmpty(user)){
                if ("3".equals(user.getUserType())){
                    vo.setDeptId(user.getDeptId());
                }
            }
            return CommonResult.success(criteriaService.load(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/delCriteria")
    public CommonResult deleteCriteria(String id) {
        try{
            if (StringUtils.isBlank(id)){
                throw new RuntimeException("参数缺失");
            }
            criteriaService.delete(id);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/saveOrUpdateApplication")
    public CommonResult saveOrUpdateAcceptanceApplication(ConAcceptanceApplicationReqVo vo) {
        try{
            if (StringUtils.isBlank(vo.getId())){
                applicationService.insert(vo);
            }else {
                applicationService.update(vo);
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadPageApplication")
    public CommonResult loadPageAcceptanceApplication(ConAcceptanceApplicationReqVo vo) {
        try{
            return CommonResult.success(applicationService.loadPage(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/countApplication")
    public CommonResult countAcceptanceApplication(ConAcceptanceApplicationReqVo vo) {
        try{
            return CommonResult.success(applicationService.count(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/appApplicationCount")
    public CommonResult appAcceptanceApplicationCount() {
        try{

            return CommonResult.success(applicationService.appCount());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/delApplication")
    public CommonResult deleteAcceptanceApplication(String id) {
        try{
            if (StringUtils.isBlank(id)){
                throw new RuntimeException("参数缺失");
            }
            applicationService.delete(id);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/passApplicationNode")
    public CommonResult passAcceptanceApplicationNode(ConAcceptanceApplicationNodeReqVo vo) {
        try{
            applicationNodeService.pass(vo);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/rejectApplicationNode")
    public CommonResult rejectAcceptanceApplicationNode(ConAcceptanceApplicationNodeReqVo vo) {
        try{
            applicationNodeService.reject(vo);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/reminderApplicationNode")
    public CommonResult reminderAcceptanceApplicationNode(ConAcceptanceApplicationNodeReqVo vo) {
        try{
            applicationNodeService.reminder(vo);
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadApplicationNodes")
    public CommonResult loadAcceptanceApplicationNodes(ConAcceptanceApplicationNodeReqVo vo) {
        try{
            if (StringUtils.isBlank(vo.getApplicationid())){
                throw new RuntimeException("缺少必要参数");
            }
            return CommonResult.success(applicationNodeService.loadNodes(vo));
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
    }

    @Override
    @PostMapping("/saveOrUpdateAnnualAcceptance")
    public CommonResult saveOrUpdateAnnualAcceptance(ConAnnualAcceptanceReqVo vo) {
        try{
            if (StringUtils.isBlank(vo.getId())){
                annualAcceptanceService.insert(vo);
            }else {
                annualAcceptanceService.update(vo);
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadPageAnnualAcceptance")
    public CommonResult loadPageAnnualAcceptance(ConAnnualAcceptanceReqVo vo) {
        try{
            String userId = JwtTokenUtil.getCurrentUserId().orElse("");
            SysUser user = userFeignClient.detailInfo(userId);
            if (!ObjectUtils.isEmpty(user)){
                if ("3".equals(user.getUserType())){
                    vo.setPropertyUnitId(user.getDeptId());
                }
            }
            return CommonResult.success(annualAcceptanceService.load(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/delAnnualAcceptance")
    public CommonResult deleteAnnualAcceptance(String id) {
        try{
            if (StringUtils.isBlank(id)){
                throw new RuntimeException("参数缺失");
            }
            annualAcceptanceService.delete(id);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @GetMapping("/exportAnnualAcceptance")
    public void exportAnnualAcceptance(ConAnnualAcceptanceReqVo vo, HttpServletResponse response) {
        try{
            annualAcceptanceService.exportAnnualAcceptance(vo,response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ;
    }

    @Override
    @PostMapping("/saveOrUpdateInspection")
    public CommonResult saveOrUpdateAssessmentInspection(AssessmentInspectionReqVo vo) {
        try{
            if (StringUtils.isBlank(vo.getId())){
                inspectionService.insert(vo);
            }else {
                inspectionService.update(vo);
            }
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/loadPageInspection")
    public CommonResult loadPageAssessmentInspection(AssessmentInspectionReqVo vo) {
        try{
            return CommonResult.success(inspectionService.load(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/delInspection")
    public CommonResult deleteAssessmentInspection(String id) {
        try{
            if (StringUtils.isBlank(id)){
                throw new RuntimeException("参数缺失");
            }
            inspectionService.delete(id);

        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @GetMapping("/exportInspection")
    public void exportAssessmentInspection(AssessmentInspectionReqVo vo, HttpServletResponse response) {
        try{
            inspectionService.export(vo,response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ;
    }

    @Override
    @PostMapping("/handingInspection")
    public CommonResult handingAssessmentInspection(AssessmentInspectionReqVo vo,String handType) {
        try{
            inspectionService.handing(vo,handType);
        }catch (Exception e){
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.success();
    }

    @Override
    @PostMapping("/appInspectionCount")
    public CommonResult appAssessmentInspectionCount(AssessmentInspectionReqVo vo) {
        try{
            return CommonResult.success(inspectionService.appCount(vo));
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @Override
    @PostMapping("/getResponsibleUnits")
    public CommonResult getResponsibleUnits() {
        try{
            return CommonResult.success(inspectionService.getResponsibleUnits());
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

}
