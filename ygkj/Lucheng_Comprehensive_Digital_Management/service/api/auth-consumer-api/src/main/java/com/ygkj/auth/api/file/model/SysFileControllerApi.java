package com.ygkj.auth.api.file.model;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author Huang.zh
 * @date 2020/11/26 16:36
 * @Description: 文件系统统一服务控制层抽象
 */
@Api(value = "文件服务", tags = "文件服务")
public interface SysFileControllerApi {
    @ApiOperation(value = "文件信息列表")
    CommonResult files(String fileIds);

    @ApiOperation(value = "档案数据统计", notes = "字段描述：archiveNum(档案文件数量)、imgNum(图像文件数量)、" +
            "videoNum(视频影像文件数量)、zipNum(zip文件数量)、cadNum(cad文件数量)、otherNum(其他文件数量)")
    CommonResult archivalDataStatistics();

    @ApiOperation(value = "数字档案上传统计", notes = "字段描述：time(时间)、num(数量)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份", paramType = "query"),
    })
    CommonResult statUploadFile(Integer year);

    @ApiOperation(value = "文件参数类型", notes = "字段描述：time(时间)、type(文件类型)")
    public CommonResult fileParameterType();
}
