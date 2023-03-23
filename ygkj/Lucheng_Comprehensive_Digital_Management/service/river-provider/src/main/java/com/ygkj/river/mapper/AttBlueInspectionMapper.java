package com.ygkj.river.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.vo.response.AttBlueInspectionResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2021-08-06 9:33
 * @description 蓝线巡查
 */
public interface AttBlueInspectionMapper extends BaseMapper<AttBlueInspection> {

    /**
     * 根据项目编码统计 巡查记录数量
     * @param projectId
     * @return
     */
    @Select("select count(1) from att_blue_inspection where project_id = #{projectId}")
    Integer countNumByProjectId(@Param("projectId") String projectId);

    /**
     * 查询最后一次巡查时间
     * @param projectId
     * @return
     */
    @Select("select create_time from att_blue_inspection where project_id = #{projectId} order by create_time desc limit 1")
    String getLastTime(@Param("projectId") String projectId);

    /**
     * 查询总巡查次数
     * @return
     */
    @Select("<script>select count(1) from att_blue_inspection tbl LEFT JOIN sys_user tb2 " +
            "ON tbl.inspector_id = tb2.id LEFT JOIN sys_dept tb3 ON tb2.dept_id = tb3.id where tbl.del_flag = 0 " +
            "<if test=\"adcd != null and adcd != '' \" >\n" +
            "             AND tb3.adcd = #{adcd}\n" +
            "             </if></script>")
    Integer countNumByAll(String adcd);

    /**
     * 查询确认隐患的 数量有多少  表示提交到执法大队的数量
     * @return
     */
    @Select("<script>select count(1) from att_blue_inspection tbl LEFT JOIN sys_user tb2 ON tbl.inspector_id = tb2.id LEFT JOIN " +
            "sys_dept tb3 ON tb2.dept_id = tb3.id where tbl.del_flag = 0 and hidden_danger = 1 " +
            "<if test=\"adcd != null and adcd != '' \" >\n" +
            "             AND tb3.adcd = #{adcd}\n" +
            "             </if></script>")
    Integer countDangerNum(String adcd);

    @Select("select count(1) from att_blue_project where del_flag = 0 and regulatory_status = 4")
    Integer countToLawNum();

    @Select("select count(1) from att_blue_project where del_flag = 0 and regulatory_status = 4 and adcd = #{adcd}")
    Integer countToLawNumByAdcd(@Param("adcd") String adcd);

    /**
     * 根据当前用户 获取对应条件 巡查列表
     * @param userId
     * @param stage
     * @param type
     * @return
     */
    List<AttBlueInspectionResVo> selectListByCondition(@Param("userId") String userId,
                                                       @Param("stage") String stage,
                                                       @Param("type") String type,
                                                       @Param("result") Integer result);

    /**
     * 查询参与人员
     * @return
     */
    @Select("<script>select count(DISTINCT(distributor)) as num from att_blue_inspection tbl LEFT JOIN sys_user tb2 ON " +
            "tbl.inspector_id = tb2.id LEFT JOIN sys_dept tb3 ON tb2.dept_id = tb3.id where tbl.del_flag = 0 " +
            "<if test=\"adcd != null and adcd != '' \" >\n" +
            "             AND tb3.adcd = #{adcd}\n" +
            "             </if> </script>")
    Integer findCountPeopleNum(@Param("adcd") String adcd);

    /**
     * 按行政区划计算对应参与人
     * @return
     */
    Integer statisticsPeopleByArea(@Param("adcd") String adcd);

    Integer projectInspectionCountNum(@Param("time") String time, @Param("adcd") String adcd);

    /**
     * 获取所有的列表统计
     * @param userId
     * @return
     */
    List<AttBlueInspectionResVo> statusListCount(@Param("userId") String userId);

    /**
     * 查询七天前设置了下次巡检时间的记录
     * @return
     */
    @Select("select * from att_blue_inspection where DATE_FORMAT(now(),'%Y-%m-%d') = DATE_FORMAT(DATE_ADD(patrol_begin_time,INTERVAL -7 DAY),'%Y-%m-%d') and result_status = 0 and reviewer is null")
    List<AttBlueInspection> findRemindInspectionByTime();

}
