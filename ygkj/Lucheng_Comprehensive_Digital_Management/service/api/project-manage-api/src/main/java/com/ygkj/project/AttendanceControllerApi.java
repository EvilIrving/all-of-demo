package com.ygkj.project;

import com.ygkj.project.vo.request.AttendanceReqVo;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-07-05 14:30
 * @description
 */
@Api(tags = "考勤管理", value = "考勤管理")
public interface AttendanceControllerApi {

    /**
     *
     * @param
     * @return 返回该月份对应考勤日历
     */
    @ApiOperation("考勤日历")
    CommonResult attendanceCalendar(AttendanceReqVo reqVo);

    @ApiOperation("导出考勤")
    void exportAttendanceCalendar(HttpServletResponse response , AttendanceReqVo reqVo);

    @ApiOperation("考勤分析")
    CommonResult attendanceAnalysis(AttendanceReqVo reqVo);

    @ApiOperation("曲线分析")
    CommonResult curveAnalysis(AttendanceReqVo reqVo);

    @ApiOperation("考勤公司")
    CommonResult attendanceCompany();

    @ApiOperation("考勤个人详情")
    CommonResult attendancePeople(AttendanceReqVo reqVo);

    @ApiOperation("公司分析")
    CommonResult companyAnalysis(AttendanceReqVo reqVo);

    @ApiOperation("公司汇总")
    CommonResult companySummary(AttendanceReqVo reqVo);

}
