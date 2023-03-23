package com.ygkj.project;

import com.ygkj.project.vo.request.MoRegularMonitorVo;
import com.ygkj.project.vo.request.ProjMonitorReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-28 14:25
 * @description 工程监测
 */
@Api(tags = "工程监测", value = "工程监测")
public interface ProjMonitorControllerApi {

    @ApiOperation("定期监测 - 列表")
    CommonResult regularMonitorList(ProjMonitorReqVo reqVo);

    @ApiOperation("定期监测 - 新增或更新")
    CommonResult regularMonitorSaveOrUpdate(MoRegularMonitorVo vo);

    @ApiOperation("定期监测 - 删除")
    CommonResult regularMonitorDelete(String id);

    @ApiOperation("定期监测 - 导出")
    void regularMonitorExport(HttpServletResponse response,ProjMonitorReqVo reqVo);

}
