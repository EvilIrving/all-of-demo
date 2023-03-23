package com.ygkj.project;


import com.ygkj.project.model.LbKnowledge;
import com.ygkj.project.vo.request.LbKnowledgeReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *
 * @author
 * @since 2021-011-12
 */
@Api(tags = "知识库", value = "知识库")
public interface LbKnowledgeControllerApi {

    @ApiOperation(value = "新增或修改")
    CommonResult saveOrUpdate(LbKnowledge vo);

    @ApiOperation(value = "删除")
    CommonResult del(Integer id);

    @ApiOperation(value = "分页查找")
    CommonResult loadPage(LbKnowledgeReqVo vo);

}

