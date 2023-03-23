package com.ygkj.river.feign;


import com.ygkj.supervision.model.SysUser;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 1xl
 */
@FeignClient(contextId = "authentication-system", name = "authentication-system")
public interface AuthenticationClient {

    /**
     * 文件上传
     *
     * @param uploadFile
     * @return
     */
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    Object upload(@RequestPart(value = "uploadFile") MultipartFile uploadFile);


//    @GetMapping("/dept/getDeptTree")
//    @ApiOperation(value = "组织列表接口")
//    List<DeptRespNodeVO> getDeptTree(@RequestParam(required = false) String deptId);

//    /**
//     * 获取部门树
//     * @param deptId
//     * @return
//     */
//    @GetMapping("sys/dept/getDeptTree")
//    List<DeptRespNodeVO> deptTree(@RequestParam(value = "deptId", required = false) String deptId);


    @GetMapping("sys/user/{id}")
    @ApiOperation(value = "查询用户详情接口")
    SysUser detailInfo(@PathVariable("id") String id);

    @PostMapping("/file/load")
    CommonResult load(@RequestParam("ids") String ids);

}
