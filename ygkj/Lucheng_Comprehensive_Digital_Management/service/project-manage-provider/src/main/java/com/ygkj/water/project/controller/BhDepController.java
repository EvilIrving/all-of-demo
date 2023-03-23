package com.ygkj.water.project.controller;

import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BhDepService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("bhDep")
@Api(tags = "标化平台的一些copy")
public class BhDepController {

    @Resource
    private BhDepService bhDepService;

    @ApiOperation("工程简介")
    @GetMapping("introduction/{seawallCode}")
    public CommonResult introduction(@PathVariable("seawallCode")String seawallCode){
        return CommonResult.success(bhDepService.introduction(seawallCode));
    }

    @ApiOperation(value = "组织架构",response = JobSettingResVo.class)
    @GetMapping("organization/{seawallCode}")
    public CommonResult organization(@PathVariable("seawallCode")String seawallCode){
        return CommonResult.success(bhDepService.organization(seawallCode));
    }

    @ApiOperation(value = "组织架构详情",response = JobSettingDetailResVo.class)
    @PostMapping("organizationDetail")
    public CommonResult organizationDetail(JobSettingReqVo reqVo){
        return CommonResult.success(bhDepService.organizationDetail(reqVo));
    }

    @ApiOperation(value = "人员信息",response = JobPersonResVo.class)
    @PostMapping("listPeople")
    public CommonResult listPeople(JobPersonReqVo reqVo){
        return CommonResult.success(bhDepService.listPeople(reqVo));
    }

    @ApiOperation(value = "年度培训",response = PersonTrainResVo.class)
    @PostMapping("personTrain")
    public CommonResult personTrain(PersonTrainReqVo reqVo){
        return CommonResult.success(bhDepService.personTrain(reqVo));
    }

    @ApiOperation(value = "管护经费",response = ManageFundResVo.class)
    @PostMapping("managefund")
    public CommonResult managefund(ManageFundReqVo reqVo){
        return CommonResult.success(bhDepService.managefund(reqVo));
    }

    @ApiOperation(value = "管理制度文件",response = ManageinstitutionResVo.class)
    @GetMapping("manageinstitution/{seawallCode}")
    public CommonResult manageinstitution(@PathVariable("seawallCode")String seawallCode){
        return CommonResult.success(bhDepService.manageinstitution(seawallCode));
    }

    @ApiOperation(value = "维修养护项目维护",response = MfpmResVo.class)
    @PostMapping("mfpm")
    public CommonResult mfpm(MfpmReqVo reqVo){
        return CommonResult.success(bhDepService.mfpm(reqVo));
    }

    @ApiOperation(value = "维修养护资金申报",response = MfdResVo.class)
    @PostMapping("mfd")
    public CommonResult mfd(MfdReqVo reqVo){
        return CommonResult.success(bhDepService.mfd(reqVo));
    }

    @ApiOperation(value = "维修养护资金使用",response = MfuResVo.class)
    @PostMapping("mfu")
    public CommonResult mfu(MfuReqVo reqVo){
        return CommonResult.success(bhDepService.mfu(reqVo));
    }

    @PostMapping("/getBhUser")
    @ApiOperation(value = "获取标化用户列表")
    public CommonResult userList(BhDeptUserReqVo reqVo){
        return CommonResult.success(bhDepService.userList(reqVo));
    }

    @PostMapping("/getBhDept")
    @ApiOperation(value = "获取标化部门列表")
    public CommonResult deptList(BhDeptUserReqVo reqVo){
        return CommonResult.success(bhDepService.deptList(reqVo));
    }


}
