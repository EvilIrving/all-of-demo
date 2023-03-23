package com.ygkj.digitization;

import com.ygkj.digitization.vo.request.WarnUserReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "预警人员", value = "预警人员")
public interface WarnUserControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(WarnUserReqVo vo);

    @ApiOperation(value = "删除")
    CommonResult del(String id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(WarnUserReqVo vo);

    @ApiOperation(value = "人员导入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件"),
            @ApiImplicitParam(name = "deptId", value = "部门id")
    })
    CommonResult importUserExcel(@RequestParam("file") MultipartFile file, @RequestParam("deptId") String deptId);

}
