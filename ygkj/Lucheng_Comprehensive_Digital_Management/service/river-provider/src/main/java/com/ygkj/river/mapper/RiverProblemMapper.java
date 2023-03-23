package com.ygkj.river.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.entity.SingletonMap;
import com.ygkj.river.model.RiverProblem;
import com.ygkj.river.vo.request.PatrolProblemQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 河长通-问题信息表
 * @date 2021-08-09
 */
@DS("business")
public interface RiverProblemMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/09
     **/
    int insert(RiverProblem riverProblem);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/09
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/09
     **/
    int update(RiverProblem riverProblem);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/09
     **/
    RiverProblem load(String id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/09
     **/
    List<RiverProblem> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/09
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 根据条件查询巡河问题
     *
     * @param reqVo
     * @return
     */
    List<RiverProblem> selectByCondition(PatrolProblemQueryVo reqVo);


    @Select("select area_code as `key` ,count(1) as value " +
            " from river_problem " +
            " where create_date between #{start} and #{end} " +
            " group by area_code")
    List<SingletonMap<String, Long>> countByAreaCodeWithTimeInterval(Date start, Date end);


    /**
     * 根据条件统计level数
     *
     * @param reqVo
     * @return
     */
    List<SingletonMap<String, Long>> countByLevelWithCondition(PatrolProblemQueryVo reqVo);

    /**
     * 根据条件统计问题总数
     *
     * @param reqVo
     * @return
     */
    Long totalNum(PatrolProblemQueryVo reqVo);


    @Select("select concat(state,'') as `key` ,count(1) as value " +
            " from river_problem " +
            " where create_date between #{start} and #{end} and state is not null " +
            " group by state")
    List<SingletonMap<String, Long>> countByStateWithTimeInterval(Date start, Date end);


    @Select("select DATE_FORMAT(create_date,'%Y-%m') as `key` ,count(1) as value " +
            " from river_problem " +
            " where create_date between #{start} and #{end} " +
            " group by DATE_FORMAT(create_date,'%Y-%m')")
    List<SingletonMap<String, Long>> countByMonthWithTimeInterval(Date start, Date end);

}
