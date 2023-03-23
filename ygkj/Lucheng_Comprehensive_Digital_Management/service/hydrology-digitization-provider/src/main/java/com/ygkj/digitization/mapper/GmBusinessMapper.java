package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.GmBusiness;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日常业务管理-业务管理
 *
 * @author xq
 * @date 2021/02/01
 */
@DS("business")
public interface GmBusinessMapper {

    /**
     * [新增]
     *
     * @author xq
     * @date 2021/02/01
     **/
    int insert(GmBusiness gmBusiness);

    /**
     * [刪除]
     *
     * @author xq
     * @date 2021/02/01
     **/
    int delete(int id);

    /**
     * [更新]
     *
     * @author xq
     * @date 2021/02/01
     **/
    int update(GmBusiness gmBusiness);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author xq
     * @date 2021/02/01
     **/
    GmBusiness load(int id);

    /**
     * [查询] 分页查询
     *
     * @author xq
     * @date 2021/02/01
     **/
    List<GmBusiness> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author xq
     * @date 2021/02/01
     **/
    int pageListCount(int offset, int pagesize);

    List<GmBusiness> selectByAdcdTaskNameExetime(@Param("adcd") String adcd,
                                                 @Param("taskName") String taskName,
                                                 @Param("etStart") Date etStart,
                                                 @Param("etEnd") Date etEnd,
                                                 @Param("taskStateList") List<Integer> taskStateList);

    int virtualDeleteById(String id);

    GmBusiness selectById(String id);

    int updateTaskStateFromState(@Param("oldState") int oldState,
                                 @Param("newState") int newState,
                                 @Param("id") String id);

    int insertMany(List<GmBusiness> list);

    int insertOne(@Param("busMan") GmBusiness busMan);
}
