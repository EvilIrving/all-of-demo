package com.ygkj.auth.api.monitor;

import com.ygkj.auth.VO.request.BusProjectPageReqVo;
import com.ygkj.auth.VO.request.ProjectFileReqVos;
import com.ygkj.auth.VO.request.ProjectTagUpdateReqVo;
import com.ygkj.auth.api.monitor.model.BusProject;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: huangzh
 * @Date: 2020/9/8 22:26
 * @Description:
 */
@Api(value = "BusProjectControllerApi", tags = "审批-项目立项")
public interface BusProjectControllerApi {

    @ApiOperation(value = "新增待办项目信息接口")
    Object insertBusProject(BusProject busProject);

    @ApiOperation(value = "删除待办项目信息接口")
    Object deleteBusProject(@PathVariable("id") String id);

    @ApiOperation(value = "更新待办项目信息接口")
    Object updateBusProject(BusProject busProject);

    @ApiOperation(value = "根据项目id获取项目详情接口")
    Object loadBusProject(@PathVariable("id") String id);

    @ApiOperation(value = "根据项目id获取项目资料")
    Object loadFiles(@PathVariable("id") String id);

    @ApiOperation(value = "分页查询待办项目信息接口")
    Object pageListBusProject(BusProjectPageReqVo vo);

    /**
     * 查询 分页查询-已办项目
     * @author huangzh
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询已办项目信息接口")
    Object queryDoneProject(BusProjectPageReqVo vo);

    /**
     * 查询 分页查询-补正项目
     * @author huangzh
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询补正项目信息接口")
    Object querycCorrectProject(BusProjectPageReqVo vo);

    /**
     * 查询 分页查询-退办项目
     * @author huangzh
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询退办项目信息接口")
    Object querycBreakProject(BusProjectPageReqVo vo);

    /**
     * 查询 分页查询-已申报待办项目
     *
     * @author huangzh
     * @date 2020/09/08
     **/
    @ApiOperation(value = "分页查询已申报待办项目信息接口")
    Object querycDeclaredProject(BusProjectPageReqVo vo);

    /**
     * 上报
     *
     * @author huangzh
     * @date 2020/09/18
     **/
    @ApiOperation(value = "根据项目id上报")
    Object updateReportStatus(@PathVariable("id") String id);

    /**
     * 上传资料
     *
     * @author huangzh
     * @date 2020/09/18
     **/
    @ApiOperation(value = "根据项目id上传资料")
    Object uploadFiles(@PathVariable("id") String id, ProjectFileReqVos vos);

    @ApiOperation(value = "未申报项目修改时上报")
    Object updateReport(BusProject busProject);

    @ApiOperation(value = "查询最近的工程")
    CommonResult queryClosestProject(BusProject project);

    @ApiOperation(value = "根据项目id添加自定义标签")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "项目id"),
            @ApiImplicitParam(name = "tagId", value = "自定义标签id")})
    Object addTags(String id, String tagId);

    @ApiOperation(value = "根据项目ids(批量)修改自定义标签")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "项目id,多个以逗号分隔"),
            @ApiImplicitParam(name = "tagId", value = "自定义标签")})
    Object updateTags(String ids, String tagId);

    @ApiOperation(value = "根据项目id删除自定义标签")
    Object deletedTags(String id);

    @ApiOperation(value = "新增项目自定义标签")
    Object addProjectTag(String tag);

    @ApiOperation(value = "获取自定义标签（包含标签所存在项目数量）")
    @ApiImplicitParam(name = "stage", value = "项目阶段(1待办，2已办，3补正，4退办)")
    Object getProjectTags(String stage);

    @ApiOperation(value = "根据标签id获取自定义标签信息")
    @ApiImplicitParam(name = "tagId", value = "标签id")
    Object getProjectTagById(@PathVariable("tagId") String tagId);

    @ApiOperation(value = "修改自定义标签")
    Object updateProjectTag(ProjectTagUpdateReqVo vo);
}
