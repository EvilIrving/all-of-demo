package com.ygkj.flood;

import com.ygkj.flood.model.AttDefenObjectBase;
import com.ygkj.flood.vo.request.DefenObjectReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @create 2022-05-17 16:05
 * @description
 */
@Api(tags = "山洪灾害防御对象清单")
public interface DefenObjectControllerApi {

    @ApiOperation("山洪灾害防御对象清单 - 列表")
    CommonResult defenObjectList(DefenObjectReqVo reqVo);

    @ApiOperation("山洪灾害防御对象清单 - 新增或者删除")
    CommonResult defenObjectSaveOrUpdate(AttDefenObjectBase base);

    @ApiOperation("山洪灾害防御对象清单 - 删除")
    CommonResult defenObjectDelete(String id);


}
