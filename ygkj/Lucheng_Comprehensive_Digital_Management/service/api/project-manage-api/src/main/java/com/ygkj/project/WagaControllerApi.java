package com.ygkj.project;

import com.ygkj.project.vo.request.WrpWagaQueryReqVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author xq
 * @Description
 * @Date 2021/9/14
 */
@Api(tags = "水闸工程接口")
public interface WagaControllerApi {
    @ApiOperation("导出水闸")
    void exportWrpWagaUnitTable(WrpWagaQueryReqVo reqVo, HttpServletResponse response);
}
