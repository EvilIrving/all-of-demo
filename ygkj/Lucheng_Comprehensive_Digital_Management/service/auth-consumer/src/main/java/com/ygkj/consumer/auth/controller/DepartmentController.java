package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.DeptAddReqVO;
import com.ygkj.auth.VO.request.DeptUpdateReqVO;
import com.ygkj.auth.VO.response.DeptRespNodeVO;
import com.ygkj.auth.api.department.DepartmentControllerApi;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/8/26 17:22
 * @Description: 科室微服务调用方
 */
@RestController
@RequestMapping("/dept")
public class DepartmentController implements DepartmentControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @GetMapping("/list")
    @Override
    public List<SysDept> deptList(@RequestParam(required = false) String deptId) {
        return authenticationClient.deptList(deptId);
    }

    @GetMapping("/deptTree")
    @Override
    public List<DeptRespNodeVO> deptTree(@RequestParam(required = false) String deptId) {
        return authenticationClient.deptTree(deptId);
    }

    @PostMapping("/department")
    @Override
    public Object addDepartment(DeptAddReqVO deptAddReqVO) {
        return authenticationClient.addDepartment(deptAddReqVO);
    }

    @PutMapping("/department")
    @Override
    public Object updateDepartment(DeptUpdateReqVO deptUpdateReqVO) {
        return authenticationClient.updateDepartment(deptUpdateReqVO);
    }

    @DeleteMapping("/department/{deptId}")
    @Override
    public Object deleteDepartment(@PathVariable String deptId) {
        return authenticationClient.deleteDepartment(deptId);
    }
}
