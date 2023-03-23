package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.model.AttendanceDataBase;
import com.ygkj.project.vo.request.AttendanceReqVo;
import com.ygkj.project.vo.response.AttendanceDataResVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lxl
 * @create 2022-07-05 14:36
 * @description
 */
public interface AttendanceService {


    JSONObject attendanceCalendar(AttendanceReqVo reqVo);

    PageVO attendanceAnalysis(AttendanceReqVo reqVo);

    /**
     * 考勤公司
     * @return
     */
    List<String> attendanceCompany();

    /**
     * 考勤公司情况分析
     * @param reqVo
     * @return
     */
    List<JSONObject> companyAnalysis(AttendanceReqVo reqVo);

    List<AttendanceDataResVo> companySummary(AttendanceReqVo reqVo);

    /**
     * 导出
     * @param response
     * @param reqVo
     */
    void exportAttendanceCalendar(HttpServletResponse response, AttendanceReqVo reqVo);

    List<AttendanceDataBase> attendancePeople(AttendanceReqVo reqVo);

    List<JSONObject> curveAnalysis(AttendanceReqVo reqVo);
}
