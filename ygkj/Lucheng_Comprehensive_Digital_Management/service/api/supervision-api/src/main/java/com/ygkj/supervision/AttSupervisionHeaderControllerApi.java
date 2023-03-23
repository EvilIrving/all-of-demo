package com.ygkj.supervision;


import com.ygkj.supervision.model.AttSupervisionHeader;
import com.ygkj.supervision.vo.request.HeaderReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @date 2021/5/11
 */
@Api(tags = "表单管理", value = "表单管理")
public interface AttSupervisionHeaderControllerApi {

    /**
     * 新增表单
     *
     * @param attSupervisionHeader
     * @return
     */
    @ApiOperation("新增表单")
    CommonResult insertHeader(AttSupervisionHeader attSupervisionHeader);

    /**
     * 条件查询表头配置列表
     *
     * @param vo
     * @return
     */
    @ApiOperation("条件查询表头配置列表")
    CommonResult getHeaderListByCondition(HeaderReqVo vo);

    /**
     * 更新表单
     *
     * @param attSupervisionHeader
     * @return
     */
    @ApiOperation("更新表单")
    CommonResult updateHeader(AttSupervisionHeader attSupervisionHeader);

    /**
     * 删除表单记录
     *
     * @param formCode
     * @return
     */
    @ApiOperation("逻辑删除表单记录")
    CommonResult deleteHeader(String formCode);
}
