package com.ygkj.water.project.controller;

import com.ygkj.project.AttendanceControllerApi;
import com.ygkj.project.vo.request.AttendanceReqVo;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.AttendanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-07-05 14:29
 * @description
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController implements AttendanceControllerApi {

    @Resource
    private AttendanceService attendanceService;


    @Override
    @GetMapping("/attendanceCalendar")
    public CommonResult attendanceCalendar(AttendanceReqVo reqVo) {
        if (!reqVo.isPaged()){
            return CommonResult.failed("缺少分页参数");
        }
        return CommonResult.success(attendanceService.attendanceCalendar(reqVo));
    }

    @Override
    @PostMapping("/exportAttendanceCalendar")
    public void exportAttendanceCalendar(HttpServletResponse response, AttendanceReqVo reqVo) {
        attendanceService.exportAttendanceCalendar(response,reqVo);
    }

    @Override
    @PostMapping("/attendanceAnalysis")
    public CommonResult attendanceAnalysis(AttendanceReqVo reqVo) {
        if (!reqVo.isPaged()){
            return CommonResult.failed("缺少分页参数");
        }
//        if (StringUtils.isBlank(reqVo.getMonth()) || StringUtils.isBlank(reqVo.getYear())){
//            return CommonResult.failed("缺少年月参数");
//        }
        return CommonResult.success(attendanceService.attendanceAnalysis(reqVo));
    }

    @Override
    @GetMapping("/curveAnalysis")
    public CommonResult curveAnalysis(AttendanceReqVo reqVo) {
        return CommonResult.success(attendanceService.curveAnalysis(reqVo));
    }


    @Override
    @GetMapping("/attendanceCompany")
    public CommonResult attendanceCompany() {
        return CommonResult.success(attendanceService.attendanceCompany());
    }

    @Override
    @GetMapping("/attendancePeople")
    public CommonResult attendancePeople(AttendanceReqVo reqVo) {
        return  CommonResult.success(attendanceService.attendancePeople(reqVo));
    }


    @Override
    @PostMapping("/companyAnalysis")
    public CommonResult companyAnalysis(AttendanceReqVo reqVo) {

        return CommonResult.success(attendanceService.companyAnalysis(reqVo));
    }

    @Override
    @PostMapping("/companySummary")
    public CommonResult companySummary(AttendanceReqVo reqVo) {
        return CommonResult.success(attendanceService.companySummary(reqVo));
    }
}
