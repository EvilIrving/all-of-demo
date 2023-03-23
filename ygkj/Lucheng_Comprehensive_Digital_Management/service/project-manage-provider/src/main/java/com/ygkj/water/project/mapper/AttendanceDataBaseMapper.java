package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.AttendanceDataBase;
import com.ygkj.project.vo.request.AttendanceReqVo;
import com.ygkj.project.vo.response.AttendanceDataResVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2022-07-05 14:59
 * @description
 */
public interface AttendanceDataBaseMapper extends BaseMapper<AttendanceDataBase> {


    @Select("select a.`name`,a.corporation_name corporationName, DATE_FORMAT(a.attendance_time,'%Y-%m-%d') attendanceTime ,b.post,b.phone from attendance_data_base a left join attendance_personnel_base b on a.job_number = b.job_number " +
            "where attendance_time like concat(#{year},'-',#{month},'%') group by a.`name`,DATE_FORMAT(a.attendance_time,'%Y-%m-%d')")
    List<AttendanceDataResVo> findAnalysis(AttendanceReqVo reqVo);

    @Select("select a.name,a.device_name,a.temperature,a.checking_type,a.checking_desc,a.attendance_time from " +
            "(select *,DATE_FORMAT(attendance_time,'%Y-%m-%d')attendance_time1 from attendance_data_base " +
            "where name = #{name} and attendance_time like concat(#{year},'-',#{month},'%') having 1 order by attendance_time desc) a \n" +
            "group by a.attendance_time1 ORDER BY a.attendance_time desc")
    List<AttendanceDataBase> selectAttendaceData(AttendanceReqVo reqVo);

    @Select("select a.name,a.temperature,a.checking_desc,a.attendance_time1 attendance_time from \n" +
            "(select *,DATE_FORMAT(attendance_time,'%Y-%m-%d')attendance_time1 \n" +
            "from attendance_data_base\n" +
            "where  attendance_time like concat(#{year},'-',#{month},'%')\n" +
            "having 1 order by attendance_time desc) a \n" +
            "group by a.name,a.attendance_time1 ORDER BY a.attendance_time\n")
    List<AttendanceDataBase> curveAnalysis(AttendanceReqVo reqVo);
}
