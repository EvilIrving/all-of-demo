package com.ygkj.consumer.auth.client.authentication;

import com.ygkj.auth.VO.request.*;
import com.ygkj.auth.VO.response.DeptRespNodeVO;
import com.ygkj.auth.VO.response.LoginRespVO;
import com.ygkj.auth.VO.response.PageVO;
import com.ygkj.auth.api.achievement.model.SysAchievement;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.monitor.model.BusProject;
import com.ygkj.auth.api.monitor.model.BusProjectMatter;
import com.ygkj.auth.api.user.model.SysUser;
import com.ygkj.water.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Huang.zh
 * @date 2020/8/26 11:10
 * @Description: 用户认证服务feign抽象
 */
@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface AuthenticationClient {

    @PostMapping("/sys/user/login")
    LoginRespVO login(@RequestBody LoginReqVO loginReqVO);

    @PostMapping("/sys/user/validateTicket")
    LoginRespVO validateTicket(ValidateTicketVo validateTicketVo);

    @PostMapping("/sys/user/zzdLogin")
    CommonResult zzdLogin(SingleSignOnRequestVo zzdRequestVo);

    @PostMapping("/sys/user/zlbLogin")
    CommonResult zlbLogin(SingleSignOnRequestVo zzdRequestVo);

    @PostMapping("/sys/user/zwwLogin")
    CommonResult zwwLogin(SingleSignOnRequestVo vo);

    @GetMapping("/sys/user/getInfoByTicket")
    CommonResult getInfoByTicket(@RequestParam String ticketId);

    @GetMapping("/sys/user/sendVerificationCode")
    void sendZzdVerification(@RequestParam(value = "phone", required = false) String phone);

    @PostMapping("/sys/user/rdspwd")
    Map<String, Object> rdspwd();

    @GetMapping("/sys/user/{id}")
    SysUser detailInfo(@PathVariable("id") String id);

    @PostMapping("/sys/users")
    PageVO<SysUser> pageInfo(UserPageReqVO userPageReqVO);

    @PostMapping("/sys/user")
    Object addUser(UserAddReqVO userAddReqVO);

    @DeleteMapping("/sys/user")
    Object deleteUser(List<String> userIds);

    @PutMapping("/sys/user/info")
    Object updateUser(UserUpdateReqVO userUpdateReqVO);

    @PutMapping("/sys/user/pwd")
    Object updatePassword(UpdatePasswordReqVO updatePasswordReqVO);

    @GetMapping("sys/dept/tree")
    List<SysDept> deptList(@RequestParam(value = "deptId", required = false) String deptId);

    @GetMapping("sys/dept/getDeptTree")
    List<DeptRespNodeVO> deptTree(@RequestParam(value = "deptId", required = false) String deptId);

    @PostMapping("sys/dept")
    Object addDepartment(DeptAddReqVO deptAddReqVO);

    @PutMapping("sys/dept")
    Object updateDepartment(DeptUpdateReqVO deptUpdateReqVO);

    @DeleteMapping("/sys/dept/{deptId}")
    Object deleteDepartment(@PathVariable("deptId") String deptId);

    @GetMapping("/sys/companys/getCompanyAll")
    Object getCompanyList();

    @PutMapping("/sys/company/changeStatus")
    Object changeStatus(CompanyUpdateReqVO vo);

    @PostMapping("/sys/company/users")
    Object pageCompanyUsers(UserPageUserByDeptReqVO vo);

    @GetMapping("/sys/company/checkComp/{socialCreditCode}")
    Object checkCompanyRegister(@PathVariable("socialCreditCode") String socialCreditCode);

    @PostMapping("/sys/company/add")
    Object addCompany(CompanyAddReqVO vo);

    @GetMapping("/sys/company/getCompany")
    Object getCompanyInfo();

    @PutMapping("/sys/company/update")
    Object updateCompanyInfo(CompanyUpdateReqVO vo);

    @PutMapping("/sys/company/getCompanyUser")
    Object getCompanyUser(@RequestParam("id") String id);

    @PutMapping("/sys/company/updateCompanyUser")
    Object updateCompanyUser(UserUpdateReqVO vo);

    @PutMapping("/sys/company/updateCompanyUserStatus")
    Object updateCompanyUserStatus(@RequestParam("id") String id, @RequestParam("status") Integer status);

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    Object upload(@RequestPart(value = "uploadFile") MultipartFile uploadFile);

    @PostMapping("/sys/user/logout")
    Object logout(@RequestParam("accessToken") String accessToken
            , @RequestParam("refreshToken") String refreshToken);

    // ---------------------------------水域监测-----------------------------------------

    @PostMapping("/waterbase/insert")
    Object insert(WaterBaseAddReqVo vo);

    @DeleteMapping("/waterbase/delete/{id}")
    Object delete(@PathVariable("id") String id);

    @PutMapping("/waterbase/update")
    Object update(WaterBaseUpdateReqVo vo);

    @GetMapping("/waterbase/load/{id}")
    Object load(@PathVariable("id") String id);

    @PostMapping("/waterbase/pageList")
    Object pageList(WaterBasePageReqVo waterBasePageReqVo);

    @PostMapping("/waterbase/pageListByInfo")
    Object pageListByInfo(WaterBaseByInfoPageReqVo vo);

    @PostMapping("/waterbase/pageListByKeyword")
    Object pageListByKeyword(WaterBaseByKeywordPageReqVo vo);

    @GetMapping(value = "/waterbase/queryAreaNames", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    Object queryAreaNames();

    @PostMapping(value = "/waterbase/parseExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Object parseExcel(@RequestPart(value = "file", required = false) MultipartFile file);

    @PostMapping("/waterbase/queryNearbyWaterBaseInfo")
    Object queryNearbyWaterBaseInfo(@RequestParam("lng") Double lng, @RequestParam("lat") Double lat);

    // ---------------------------------------------水域变化---------------------------------------------------

    /**
     * 根据行政区划和水域类型查询水域变化信息
     */
    @PostMapping("/waterbase/queryWaterChangeInfo")
    Object queryWaterChangeInfo(@RequestBody @Valid WaterChangePageReqVo vo);

    /**
     * 根据水域变化id获取水域变化详情记录
     */
    @GetMapping("/waterbase/getChangeDetails/{id}")
    Object getChangeDetails(@PathVariable("id") String id);

    /**
     * 新增水域详情记录
     */
    @PostMapping("/waterbase/addChangeDetail")
    Object addChangeDetail(@RequestBody @Valid WaterChangeDetailAddReqVo vo);

    @GetMapping("/waterbase/queryChangeDetail/{id}")
    Object queryChangeDetail(@PathVariable("id") String id);

    /**
     * 更新水域变化信息(更新后新增历史数据)
     */
    @PutMapping("/waterbase/updateChangeDetail")
    Object updateWaterChangeInfo(@RequestBody @Valid ChangeDetailUpdateReqVo vo);

    /**
     * 删除水域详情记录
     */
    @DeleteMapping("/waterbase/deleteChangeDetail/{id}")
    Object deleteChangeDetail(@PathVariable("id") String id);

    @GetMapping("/waterbase/queryWaterDetail/{id}")
    Object queryWaterDetail(@PathVariable("id") String id);

    @PostMapping("/waterbase/queryYearWaterDetailInfo")
    Object queryYearWaterDetailInfo(@RequestBody @Valid WaterChangeYearReqVo vo);

    // ----------------------------------智慧审批-项目立项-------------------------------------

    @PostMapping("/BusProject/insert")
    Object insertBusProject(BusProject busProject);

    @DeleteMapping("/BusProject/delete/{id}")
    Object deleteBusProject(@PathVariable("id") String id);

    @PutMapping("/BusProject/update")
    Object updateBusProject(BusProject busProject);

    @GetMapping("/BusProject/load/{id}")
    Object loadBusProject(@PathVariable("id") String id);

    @GetMapping("/BusProject/loadProjectFiles/{id}")
    Object loadObjectFiles(@PathVariable("id") String id);

    @PostMapping("/BusProject/pageList")
    Object pageListBusProject(BusProjectPageReqVo vo);

    @PostMapping("/BusProject/queryDoneProject")
    Object queryDoneProject(BusProjectPageReqVo vo);

    @PostMapping("/BusProject/querycCorrectProject")
    Object querycCorrectProject(BusProjectPageReqVo vo);

    @PostMapping("/BusProject/querycBreakProject")
    Object querycBreakProject(BusProjectPageReqVo vo);

    @PostMapping("/BusProject/querycDeclaredProject")
    Object querycDeclaredProject(BusProjectPageReqVo vo);

    @PutMapping("/BusProject/updateReportStatus/{id}")
    Object updateReportStatus(@PathVariable("id") String id);

    @PostMapping("/BusProject/uploadFiles/{id}")
    Object uploadFiles(@PathVariable("id") String id, ProjectFileReqVos vos);

    @PostMapping("/BusProject/updateReport")
    Object updateReport(BusProject busProject);

    @PostMapping("/BusProject/addTags")
    Object addTags(@RequestParam("id") String id, @RequestParam("tagId") String tagId);

    @PutMapping("/BusProject/updateTags")
    Object updateTags(@RequestParam("ids") String ids, @RequestParam("tagId") String tagId);

    @DeleteMapping("/BusProject/deletedTags")
    Object deletedTags(@RequestParam("id") String id);

    @PostMapping("/BusProject/addProjectTag")
    Object addProjectTag(@RequestParam("tag") String tag);

    @PostMapping("/BusProject/getProjectTags")
    Object getProjectTags(@RequestParam("stage") String stage);

    @GetMapping("/BusProject/getProjectTagById/{tagId}")
    Object getProjectTagById(@PathVariable("tagId") String tagId);

    @PutMapping("/BusProject/updateProjectTag")
    Object updateProjectTag(@RequestBody @Valid ProjectTagUpdateReqVo vo);

    // ---------------------------------------智慧审批-办理事项---------------------------------------------

    @PostMapping("/BusProject/insertMatter")
    Object insertMatter(ProjectMatterAddReqVo vo);

    @DeleteMapping("/BusProject/deleteMatter/{id}")
    Object deleteMatter(@PathVariable("id") String id);

    @DeleteMapping("/BusProject/deleteMatters")
    Object deleteMatters(@RequestParam("ids") String ids);

    @PutMapping("/BusProject/updateMatter")
    Object updateMatter(BusProjectMatter busProjectMatter);

    @GetMapping("/BusProject/loadMatter/{id}")
    Object loadMatter(@PathVariable("id") String id);

    @GetMapping("/BusProject/loadMatterFiles/{id}")
    Object loadMatterFiles(@PathVariable("id") String id);

    @PostMapping("/BusProject/pageListMatter")
    Object pageListMatter(ProjectMatterPageReqVo vo);

    @PostMapping("/BusProject/saveProject")
    Object saveProject(ProjectDeclareReqVo vo);

    @PostMapping("/BusProject/submitProject")
    Object submitProject(ProjectDeclareReqVo vo);

    //--------------------------------------------------审批-办理流程----------------------------------------------------

    @GetMapping("/BusProject/getProcessRecords/{id}")
    Object getProcessRecords(@PathVariable("id") String id);

    @PostMapping("/BusProject/notAccept")
    Object notAccept(ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/BusProject/correction")
    Object correction(ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/BusProject/accept")
    Object accept(ProjectCheckInfoReqVo vo);

    @PostMapping("/BusProject/finish")
    Object finish(ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/BusProject/callback")
    Object callback(ProjectAssigneeOpinionReqVo vo);

    @PostMapping("/BusProject/check")
    Object check(ProjectCheckInfoReqVo vo);

    @PostMapping("/BusProject/approval")
    Object approval(ProjectCheckInfoReqVo vo);

    @PostMapping("/BusProject/saveCheckInfo")
    Object saveCheckInfo(ProjectCheckInfoReqVo vo);

    @PostMapping("/BusProject/proofreader")
    Object proofreader(ProjectAssigneeOpinionReqVo vo);

    @GetMapping("/BusProject/getProjectLicense/{id}")
    Object getProjectLicense(@PathVariable("id") String id);

    @GetMapping("/BusProject/getProjectCheckInfo/{id}")
    Object getProjectCheckInfo(@PathVariable("id") String id);

    //------------------------------------------------------------------------------------------------------

    @GetMapping("/sysAchievement")
    CommonResult<List<SysAchievement>> achievementTree(@RequestParam("name") String name);

    @PostMapping("/sysAchievement")
    CommonResult addAchievement(SysAchievementAddVO sysAchievementAddVO);

    @PutMapping("/sysAchievement")
    CommonResult updateAchievement(SysAchievementUpdateVO sysAchievementUpdateVO);

    @DeleteMapping("/sysAchievement")
    CommonResult deleteSysAchievement(@RequestParam("id") String id);

    @PostMapping("/sysAchievement/files")
    CommonResult findFiles(AchievementFilePageQueryVO achievementFilePageQueryVO);

    /**************************第三方系统接入Api*********************************/

    @PostMapping("/thirdPartySystem")
    CommonResult loadSystem(SysThirdPartySystemQueryVO queryVO);

    @PutMapping("/thirdPartySystem/insert")
    CommonResult insertSystem(SysThirdPartySystemServiceAddVO addVO);

    @PutMapping("/thirdPartySystem")
    CommonResult updateSystem(SysThirdPartySystemServiceUpdateVO updateVO);

    @DeleteMapping("/thirdPartySystem")
    CommonResult deleteSystem(String id);

    @PutMapping("/thirdPartySystem/apply")
    CommonResult applySystem(SysSystemApplyVO sysSystemApplyVO);

    @PutMapping("/thirdPartySystem/examine")
    CommonResult examineApply(SysSystemApplyExamineVO examineVO);
}
