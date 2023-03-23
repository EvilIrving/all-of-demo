package com.ygkj.auth.api.department;

import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.VO.request.DeptAddReqVO;
import com.ygkj.auth.VO.request.DeptUpdateReqVO;
import com.ygkj.auth.VO.response.DeptRespNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/8/26 17:10
 * @Description: 科室服务调用方抽象
 */
@Api(tags = "科室服务")
public interface DepartmentControllerApi {

    @ApiOperation(value = "科室列表")
    List<SysDept> deptList(String deptId);

    @ApiOperation(value = "获取科室以及子部门---树形结构")
    List<DeptRespNodeVO> deptTree(String deptId);

    @ApiOperation(value = "新增科室信息")
    Object addDepartment(DeptAddReqVO deptAddReqVO);

    @ApiOperation(value = "更新科室信息")
    Object updateDepartment(DeptUpdateReqVO deptUpdateReqVO);

    @ApiOperation(value = "删除科室信息")
    Object deleteDepartment(String deptId);
}
