package com.ygkj.project;

import com.ygkj.project.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "合同管理")
public interface ContractManagementControllerApi {
    @ApiOperation("合同管理-约定验收标准-约定事项-新增修改")
    CommonResult saveOrUpdateCovenant(BmPropertyContractReqVo vo);

    @ApiOperation("合同管理-约定验收标准-约定事项-分页查询")
    CommonResult loadPageCovenant(BmPropertyContractReqVo vo);

    @ApiOperation("合同管理-约定验收标准-约定事项-删除")
    CommonResult deleteCovenant(String id);

    @ApiOperation("合同管理-约定验收标准-验收标准-新增修改")
    CommonResult saveOrUpdateCriteria(ConAcceptanceCriteriaReqVo vo);

    @ApiOperation("合同管理-约定验收标准-验收标准-分页查询")
    CommonResult loadPageCriteria(ConAcceptanceCriteriaReqVo vo);

    @ApiOperation("合同管理-约定验收标准-验收标准-删除")
    CommonResult deleteCriteria(String id);

    @ApiOperation("合同管理-合同验收-验收申请-新增修改")
    CommonResult saveOrUpdateAcceptanceApplication(ConAcceptanceApplicationReqVo vo);

    @ApiOperation("合同管理-合同验收-验收申请-分页查询")
    CommonResult loadPageAcceptanceApplication(ConAcceptanceApplicationReqVo vo);

    @ApiOperation("合同管理-合同验收-验收申请-验收统计")
    CommonResult countAcceptanceApplication(ConAcceptanceApplicationReqVo vo);

    @ApiOperation("合同管理-合同验收-验收申请-app验收统计")
    CommonResult appAcceptanceApplicationCount();

    @ApiOperation("合同管理-合同验收-验收申请-删除")
    CommonResult deleteAcceptanceApplication(String id);

    @ApiOperation("合同管理-合同验收-验收申请节点-通过")
    CommonResult passAcceptanceApplicationNode(ConAcceptanceApplicationNodeReqVo vo);

    @ApiOperation("合同管理-合同验收-验收申请节点-驳回")
    CommonResult rejectAcceptanceApplicationNode(ConAcceptanceApplicationNodeReqVo vo);

    @ApiOperation("合同管理-合同验收-验收申请节点-提醒")
    CommonResult reminderAcceptanceApplicationNode(ConAcceptanceApplicationNodeReqVo vo);

    @ApiOperation("合同管理-合同验收-验收申请节点-流程图")
    CommonResult loadAcceptanceApplicationNodes(ConAcceptanceApplicationNodeReqVo vo);

    @ApiOperation("合同管理-合同验收-年度验收-新增修改")
    CommonResult saveOrUpdateAnnualAcceptance(ConAnnualAcceptanceReqVo vo);

    @ApiOperation("合同管理-合同验收-年度验收-分页查询")
    CommonResult loadPageAnnualAcceptance(ConAnnualAcceptanceReqVo vo);

    @ApiOperation("合同管理-合同验收-年度验收-删除")
    CommonResult deleteAnnualAcceptance(String id);

    @ApiOperation("合同管理-合同验收-年度验收-")
    void exportAnnualAcceptance(ConAnnualAcceptanceReqVo vo, HttpServletResponse response);

    @ApiOperation("合同管理-考核管理-督察检查-新增修改")
    CommonResult saveOrUpdateAssessmentInspection(AssessmentInspectionReqVo vo);

    @ApiOperation("合同管理-考核管理-督察检查-分页查询")
    CommonResult loadPageAssessmentInspection(AssessmentInspectionReqVo vo);

    @ApiOperation("合同管理-考核管理-督察检查-删除")
    CommonResult deleteAssessmentInspection(String id);

    @ApiOperation("合同管理-考核管理-督察检查-导出")
    void exportAssessmentInspection(AssessmentInspectionReqVo vo, HttpServletResponse response);

    @ApiOperation("合同管理-考核管理-督察检查-处理归档")
    CommonResult handingAssessmentInspection(AssessmentInspectionReqVo vo,String handType);

    @ApiOperation("合同管理-考核管理-督察检查-app统计")
    CommonResult appAssessmentInspectionCount(AssessmentInspectionReqVo vo);

    @ApiOperation("合同管理-考核管理-督察检查-责任单位列表")
    CommonResult getResponsibleUnits();
}
