package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.GmAssign;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 日常业务管理-任务书下达
 * @author xq
 * @date 2021/02/01
 */
@DS("business")
public interface GmAssignMapper {

    /**
     * [新增]
     * @author xq
     * @date 2021/02/01
     **/
    int insert(GmAssign gmAssign);

    /**
     * [刪除]
     * @author xq
     * @date 2021/02/01
     **/
    int delete(int id);

    /**
     * [更新]
     * @author xq
     * @date 2021/02/01
     **/
    int update(GmAssign gmAssign);

    /**
     * [查询] 根据主键 id 查询
     * @author xq
     * @date 2021/02/01
     **/
    GmAssign load(int id);

    /**
     * [查询] 分页查询
     * @author xq
     * @date 2021/02/01
     **/
    List<GmAssign> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author xq
     * @date 2021/02/01
     **/
    int pageListCount(int offset,int pagesize);

    List<GmAssign> selectByAdcdItemNameExetime(@Param("adcd") String adcd,
                                               @Param("itemName") String itemName,
                                               @Param("etStart") Date etStart,
                                               @Param("etEnd") Date etEnd,
                                               @Param("taskStateList") List<Integer> taskStateList,
                                               @Param("fileName") String fileName);

    int virtualDeleteById(String id);

    GmAssign selectById(String id);

    int updateTaskStateFromState(@Param("oldState") int oldState,
                                 @Param("newState") int newState,
                                 @Param("id") String id);

    int insertMany(List<GmAssign> list);

    int insertOne(@Param("assMan") GmAssign assMan);

}
