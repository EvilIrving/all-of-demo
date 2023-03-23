package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.MonitorPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xq
 * @Description 值班人员管理持久化层
 * @Date 2021/1/13
 */
@DS("business")
public interface MonitorPersonMapper {

    /**
     * [新增]
     * @author xq
     * @date 2021/01/15
     **/
    int insert(MonitorPerson monitorPerson);

    /**
     * [刪除]
     * @author xq
     * @date 2021/01/15
     **/
    int delete(int id);

    /**
     * [更新]
     * @author xq
     * @date 2021/01/15
     **/
    int update(MonitorPerson monitorPerson);

    /**
     * [查询] 根据主键 id 查询
     * @author xq
     * @date 2021/01/15
     **/
    MonitorPerson load(int id);

    /**
     * [查询] 分页查询
     * @author xq
     * @date 2021/01/15
     **/
    List<MonitorPerson> pageList(int offset,int pagesize);

    /**
     * [查询] 分页查询 count
     * @author xq
     * @date 2021/01/15
     **/
    int pageListCount(int offset,int pagesize);

    /**
     * 根据类型查询排班人员
     * @param personType
     * @return
     */
    List<MonitorPerson> selectByPersonTypeAndUsername(@Param("personType")Integer personType, @Param("userName") String userName);

    int deleteManyByUserIdList(@Param("userIdList")List<String> userIdList);

    List<String> selectAllUserId();

    int updatePersonTypeByUserId(@Param("userIdList")List<String> userIdList,@Param("personType")Integer personType);

}
