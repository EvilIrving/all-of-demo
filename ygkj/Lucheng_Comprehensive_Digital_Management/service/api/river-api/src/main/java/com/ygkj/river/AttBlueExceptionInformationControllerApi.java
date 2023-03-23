package com.ygkj.river;

import com.ygkj.river.model.AttBlueExceptionInformation;
import com.ygkj.river.vo.request.AttBlueExceptionInformationReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2021-08-05 21:20
 * @description
 */
@Api(tags = "蓝线管理异常信息配置条目接口")
public interface AttBlueExceptionInformationControllerApi {

    /**
     * 新增异常信息配置条目
     *
     * @param attBlueExceptionInformation
     * @return
     */
    @ApiOperation("新增异常信息配置条目")
    CommonResult addExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation);


    /**
     * 更新异常信息配置条目
     *
     * @param attBlueExceptionInformation
     * @return
     */
    @ApiOperation("更新异常信息配置条目")
    CommonResult updateExceptionInformation(AttBlueExceptionInformation attBlueExceptionInformation);


    /**
     * 删除异常信息配置条目
     *
     * @param exceptionInformationIds
     * @return
     */
    @ApiOperation("根据id删除异常信息配置条目")
    CommonResult deleteExceptionInformation(String exceptionInformationIds);

    /**
     * 根据条件筛选获取异常配置项列表
     *
     * @param reqVo
     * @return
     */
    @ApiOperation("根据条件筛选获取异常配置项列表")
    CommonResult exceptionInformationList(AttBlueExceptionInformationReqVo reqVo);

    /**
     * 升降序某一配置项
     *
     * @param upOrDown
     * @param id
     * @return
     */
    @ApiOperation("升降序某一配置项")
    CommonResult updateSort(String upOrDown, String id);


}
