package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.MonitorContact;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xq
 * @Description 值班常用电话持久化层
 * @Date 2021/1/13
 */
@DS("business")
public interface MonitorContactMapper {
    /**
     * [新增]
     * @author xq
     * @date 2021/01/14
     **/
    int insert(MonitorContact monitorContact);

    /**
     * [刪除]
     * @author xq
     * @date 2021/01/14
     **/
    int delete(int id);

    /**
     * [更新]
     * @author xq
     * @date 2021/01/14
     **/
    int update(MonitorContact monitorContact);

    /**
     * [查询] 根据主键 id 查询
     * @author xq
     * @date 2021/01/14
     **/
    MonitorContact load(int id);

    /**
     * [查询] 分页查询
     * @author xq
     * @date 2021/01/14
     **/
    List<MonitorContact> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author xq
     * @date 2021/01/14
     **/
    int pageListCount(int offset,int pagesize);

    List<MonitorContact> queryByNameAndType(@Param("contactName")String contactName,@Param("contactType")Integer contactType);

    MonitorContact selectById(@Param("id")Integer id);

    int deleteManyByIdList(@Param("idList") List<Integer> idList);
}
