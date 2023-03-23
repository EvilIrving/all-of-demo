package com.dcy.datamanage.biz.feign;


import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.user.model.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(contextId = "authentication-system", name = "authentication-system")
@RequestMapping("sys")
public interface UmsFeign {


    @PostMapping("selectDeptByIdList")
    List<SysDept> selectDeptByIdList(List<String> deptIdList);

    @PostMapping("selectUserByIdList")
    List<SysUser> selectUserByIdList(List<String> userIdList);
}
