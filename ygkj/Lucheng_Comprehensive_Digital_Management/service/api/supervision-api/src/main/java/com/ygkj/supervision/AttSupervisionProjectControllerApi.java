package com.ygkj.supervision;

import com.ygkj.supervision.model.AttSupervisionProject;
import com.ygkj.supervision.vo.request.CountOverViewReqVo;
import com.ygkj.supervision.vo.request.ProjectCountReqVo;
import com.ygkj.supervision.vo.request.ProjectPageQueryVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author lxl
 * @date 2021/5/08
 */
@Api(tags = "监管项目", value = "监管项目")
public interface AttSupervisionProjectControllerApi {

    /**
     * 根据条件查询 监管列表 下有检查事项以及问题的列表
     *
     * @param queryVo
     * @return
     */
    @ApiOperation("监管列表 分页条件查询")
    CommonResult supervisionListByCondition(ProjectPageQueryVo queryVo);

    /**
     * 新增项目
     *
     * @param project
     * @return
     */
    @ApiOperation("新增项目")
    CommonResult addProject(AttSupervisionProject project);

    /**
     * 更新项目
     *
     * @param project
     * @return
     */
    @ApiOperation("更新项目")
    CommonResult updateProject(AttSupervisionProject project);


    /**
     * 统计分析
     *
     * @param vo
     * @return
     */
    @ApiOperation("统计分析")
    CommonResult countProject(ProjectCountReqVo vo);

    /**
     * 统计总览
     *
     * @param vo
     * @return
     */
    @ApiOperation("统计总览页面接口")
    CommonResult countOverView(CountOverViewReqVo vo);


    /**
     * 删除项目
     *
     * @param projectCode
     * @return
     */
    @ApiOperation("删除项目")
    CommonResult deleteProject(String projectCode);


    /**
     * 获取 在建稽查项目对应单位列表
     *
     * @param projectId
     * @return
     */
    @ApiOperation("获取单位列表")
    Object listUtils(String projectId);

    /**
     * 获取所有 工程名称
     *
     * @param queryVo
     * @return
     */
    @ApiOperation("获取所有项目工程名称")
    CommonResult getAllProject(ProjectPageQueryVo queryVo);

    /**
     * 根据分类统计对应的数量，页签使用
     *
     * @return
     */
    @ApiOperation("根据分类统计对应的数量，页签使用")
    CommonResult countNumByType();


}
