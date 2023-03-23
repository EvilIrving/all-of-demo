package com.ygkj.water.project.controller;

import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.UnitControllerApi;
import com.ygkj.project.model.UnitJobResponsibility;
import com.ygkj.project.model.UnitPositionSet;
import com.ygkj.project.model.UnitResponsibility;
import com.ygkj.project.vo.request.UnitPositionSetReqVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.AdcdBaseService;
import com.ygkj.water.project.service.UnitJobResponsibilityService;
import com.ygkj.water.project.service.UnitPositionSetService;
import com.ygkj.water.project.service.UnitResponsibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/operationUnit")
public class UnitController implements UnitControllerApi {

    @Autowired
    private UnitResponsibilityService responsibilityService;
    @Autowired
    private UnitJobResponsibilityService jobResponsibilityService;
    @Autowired
    private UnitPositionSetService positionSetService;
    @Autowired
    private AdcdBaseService adcdBaseService;

    @PostMapping("/lcArea")
    @Override
    public CommonResult selectLCArea() {
        return CommonResult.success(positionSetService.selectLCArea());
    }

    @PostMapping("/allProjects")
    @Override
    public CommonResult selectProjects() {
        return CommonResult.success(positionSetService.selectProjects());
    }

    @PostMapping("/saveOrUpdateUnit")
    @Override
    public CommonResult saveOrUpdateUnit(UnitResponsibility entity) {
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        try {
            if (StringUtils.isBlank(entity.getId())) {
                entity.setCreatorId(userId);
                responsibilityService.save(entity);
                return CommonResult.success();
            } else {
                entity.setUpdaterId(userId);
                responsibilityService.update(entity);
                return CommonResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed("新增更新失败");
    }

    @PostMapping("/queryUnit")
    @Override
    public CommonResult queryUnit(String id, String unitType,String deptId) {
        try {
            return CommonResult.success(responsibilityService.queryUnit(id, unitType,deptId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/delUnit")
    @Override
    public CommonResult delUnit(String id) {
        try {
            responsibilityService.delUnit(id);
            return CommonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/saveOrUpdateJob")
    @Override
    public CommonResult saveOrUpdateJob(UnitJobResponsibility entity) {
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        try {
            if (StringUtils.isBlank(entity.getId())) {
                entity.setCreatorId(userId);
                jobResponsibilityService.save(entity);
                return CommonResult.success();
            } else {
                entity.setUpdaterId(userId);
                jobResponsibilityService.update(entity);
                return CommonResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed("新增更新失败");
    }

    @PostMapping("/queryJob")
    @Override
    public CommonResult queryJob(String id, String unitType,String jobType,String deptId) {
        try {
            return CommonResult.success(jobResponsibilityService.queryJob(id, unitType,jobType,deptId));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/delJob")
    @Override
    public CommonResult delJob(String id) {
        try {
            jobResponsibilityService.delJob(id);
            return CommonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/saveOrUpdatePosition")
    @Override
    public CommonResult saveOrUpdatePosition(UnitPositionSet entity) {
        String userId = JwtTokenUtil.getCurrentUserId().orElse("");
        try {
            if (StringUtils.isBlank(entity.getId())) {
                entity.setCreatorId(userId);
                positionSetService.save(entity);
                return CommonResult.success();
            } else {
                entity.setUpdaterId(userId);
                positionSetService.update(entity);
                return CommonResult.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed("新增更新失败");
    }

//    @PostMapping("/queryPositions")
//    @Override
//    public CommonResult queryPositions(UnitPositionSetReqVo vo) {
//        try {
//            return CommonResult.success(positionSetService.queryPositions(vo));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return CommonResult.failed();
//    }
//
//    @PostMapping("/queryPosition")
//    @Override
//    public CommonResult queryPosition(UnitPositionSetReqVo vo) {
//        try {
//            return CommonResult.success(positionSetService.queryPosition(vo));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return CommonResult.failed();
//    }

    @PostMapping("/queryPositions")
    @Override
    public CommonResult queryPositions(UnitPositionSetReqVo vo) {
        try {
            return CommonResult.success(positionSetService.queryPositions(vo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/queryPosition")
    @Override
    public CommonResult queryPosition(UnitPositionSetReqVo vo) {
        try {
            return CommonResult.success(positionSetService.queryPosition(vo));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

    @PostMapping("/delPosition")
    @Override
    public CommonResult delPosition(String id) {
        try {
            positionSetService.delPosition(id);
            return CommonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.failed();
    }

}
