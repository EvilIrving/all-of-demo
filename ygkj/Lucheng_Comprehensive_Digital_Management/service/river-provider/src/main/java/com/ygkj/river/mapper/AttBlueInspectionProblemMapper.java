package com.ygkj.river.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.river.model.AttBlueInspectionProblem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lxl
 * @create 2021-08-06 9:35
 * @description
 */
public interface AttBlueInspectionProblemMapper extends BaseMapper<AttBlueInspectionProblem> {

    /**
     * 查询 所有问题数量
     * @param projectId
     * @return
     */
    @Select("select count(1) from att_blue_inspection_problem where inspection_id in " +
            "(select id from att_blue_inspection where project_id = #{projectId}) and rectify != 0 and del_flag = 0 ")
    Integer countTotalProblemNum(@Param("projectId") String projectId);

    /**
     * 统计发现异常问题数量
     * @param projectId
     * @return
     */
    @Select("select count(1) from att_blue_inspection_problem where inspection_id in " +
            "(select id from att_blue_inspection where project_id = #{projectId}) and rectify = #{status} and del_flag = 0 ")
    Integer countTotalAbNormalNum(@Param("projectId") String projectId,@Param("status") Integer rectify);

    /**
     * 更新对应的问题为正常态   没有隐患 无需整改
     * @param problemIds
     * @return
     */
    @Update("update att_blue_inspection_problem set rectify = 0 where find_in_set( id ,#{problemIds} )")
    Integer setProblemNormal(@Param("problemIds") List<String> problemIds);

    /**
     * 未整改问题数量
     * @return
     */
    @Select("select count(1) from att_blue_inspection_problem where del_flag = 0 and rectify = #{rectify}")
    Integer countUnRectifyNumByAll(@Param("rectify") Integer rectify);

    /**
     * 发现异常问题数量
     * @return
     */
    @Select("select count(1) from att_blue_inspection_problem where del_flag = 0 and rectify != 0")
    Integer countHaveNumByAll();

    @Select("\n" +
            "    select DISTINCT c.river_id from att_blue_inspection_problem a\n" +
            "    INNER JOIN att_blue_inspection b on a.inspection_id=b.id\n" +
            "    INNER JOIN att_blue_project c on c.id=b.project_id\n" +
            "    where a.del_flag=0 and b.del_flag=0 and c.del_flag=0\n" +
            "    and c.river_id is not null and status=2 and rectify=1")
    List<String> selectRiverIdWithProblem();

    /**
     * 根据行政区划统计问题数量
     * @param adcd
     * @return
     */
    @Select("select count(tb2.id) from att_blue_project tb0 left join att_blue_inspection tb1 on tb0.id = tb1.project_id\n" +
            "left join att_blue_inspection_problem tb2 on tb1.id = tb2.inspection_id \n" +
            "where tb1.del_flag = 0 and tb2.del_flag = 0 and tb0.del_flag = 0 and tb2.status = 2 and tb0.adcd = #{adcd}")
    Integer countTotalByAdcd(@Param("adcd") String adcd);

    /**
     * 根据问题id 查询对应所属的工程id
     * @param s
     * @return
     */
    @Select("select b.project_id from att_blue_inspection_problem a left join att_blue_inspection b on a.inspection_id = b.id " +
            "where a.del_flag = 0 and b.del_flag = 0 and b.project_id is not null")
    String getProjectIdByProblemId(String s);


    @Update("update att_blue_inspection_problem set rectify = 3 where id in " +
            "(select * from ( select id from att_blue_inspection_problem where inspection_id in " +
            "( select id from att_blue_inspection where rectify_end_time < NOW() and del_flag = 0  ) " +
            "and del_flag = 0 and rectify = 2 ) a  )")
    Integer updateOutTimeProblem();
}
