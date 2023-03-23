package com.ygkj.auth.api.monitor;

import com.ygkj.auth.VO.request.ProjectDeclareReqVo;
import com.ygkj.auth.VO.request.ProjectMatterAddReqVo;
import com.ygkj.auth.VO.request.ProjectMatterPageReqVo;
import com.ygkj.auth.api.monitor.model.BusProjectMatter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: fml
 * @Date: 2020/9/17 11:11
 * @Description:
 */
@Api(value = "BusProjectMatterControllerApi", tags = "审批-办理事项")
public interface BusProjectMatterControllerApi {

    /**
     * 新增办理事项
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "新增办理事项接口")
    Object insert(ProjectMatterAddReqVo vo);

    /**
     * 刪除办理事项
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "刪除办理事项接口")
    Object delete(String id);

    /**
     * 批量删除办理事项
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "批量刪除办理事项(id以 , 拼接)")
    Object deleteMatters(String ids);

    /**
     * 修改办理事项
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "修改办理事项接口")
    Object update(BusProjectMatter busProjectMatter);

    /**
     * 根据id查询办理事项
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "根据id查询办理事项")
    Object load(String id);

    /**
     * 分页查询办理事项
     * @author fml
     * @date 2020/09/16
     **/
    @ApiOperation(value = "分页查询办理事项")
    Object pageList(ProjectMatterPageReqVo vo);

    /**
     * 申报-保存（未上报）
     * @author fml
     * @date 2020/09/17
     **/
    @ApiOperation(value = "申报-保存（未上报）")
    Object saveProject(ProjectDeclareReqVo vo);

    /**
     * 申报-上报 (保存并在流程记录表中添加记录）
     * @author fml
     * @date 2020/09/17
     **/
    @ApiOperation(value = "申报-上报")
    Object submitProject(ProjectDeclareReqVo vo);

}
