package com.ygkj.project;

import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-11-11 13:54
 */
@Api(tags = "海塘交叉建筑", value = "海塘交叉建筑")
public interface BmAccBuildControllerApi {
    CommonResult selectBmAccBuildByCondition(String proType,String prcd);
}
