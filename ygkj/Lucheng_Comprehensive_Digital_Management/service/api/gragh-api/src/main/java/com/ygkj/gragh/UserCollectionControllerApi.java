package com.ygkj.gragh;

import com.ygkj.gragh.vo.request.UserCollectionAddVo;
import com.ygkj.gragh.vo.request.UserCollectionDelVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "用户收藏", value = "用户收藏")
public interface UserCollectionControllerApi {

    @ApiOperation("收藏点位")
    CommonResult follow(UserCollectionAddVo addVo);

    @ApiOperation("取消收藏点位")
    CommonResult unFollow(UserCollectionDelVo delVo);
}
