package com.ygkj.supervision;


import com.ygkj.supervision.model.AttSupervisionProblem;
import com.ygkj.supervision.vo.request.InsertProblemReqVo;
import com.ygkj.supervision.vo.request.ProblemReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lxl
 * @date 2021/5/10
 */
@Api(tags = "存在问题", value = "存在问题")
public interface AttSupervisionProblemControllerApi {

    @ApiOperation("根据条件 获取问题列表")
    CommonResult problemListByCondition(ProblemReqVo vo);


    /**
     * 根据事项编码 获取问题列表总览
     *
     * @param matterCode
     * @return
     */
    @ApiOperation("根据事项编码，获取问题总览")
    CommonResult problemListByMatterCode(String matterCode);


    /**
     * 新增问题，添加事项、添加处理人员
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiOperation("新增问题，添加事项、添加处理人员")
    CommonResult insertProblem(InsertProblemReqVo vo);

    /**
     * 更新问题反馈，包括事项、检查人员、问题
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiModelProperty("更新问题反馈，包括事项、检查人员、问题")
    CommonResult updateMatterAndProblem(InsertProblemReqVo vo) throws Exception;

    /**
     * 更新问题 整改问题
     *
     * @param attSupervisionProblem
     * @return
     */
    @ApiOperation("更新问题")
    CommonResult updateProblem(HttpServletRequest request, AttSupervisionProblem attSupervisionProblem);


    /**
     * 根据行政区划获取整改率
     *
     * @param area
     * @param start
     * @param end
     * @return
     */
    @ApiOperation("获取整改率")
    CommonResult getRectificationRate(String area, String start, String end);

    /**
     * 删除问题
     *
     * @param problemCode
     * @return
     */
    @ApiModelProperty("删除问题")
    CommonResult deleteProblemByCode(String problemCode);

    @ApiModelProperty("问题图片以及文件上传")
    Object upload(@RequestParam(value = "uploadFile", required = false) MultipartFile uploadFile, HttpServletRequest request);

}
