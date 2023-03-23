package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.RainfallMigrate;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 *
 * @Date 2021/2/6
 */
@Deprecated
@DS("business")
public interface RainfallMigrateMapper {

    /**
     * [新增]
     * @author xq
     * @date 2021/02/06
     **/
    int insert(RainfallMigrate rainfallMigrate);

    /**
     * [刪除]
     * @author xq
     * @date 2021/02/06
     **/
    int delete(int id);

    /**
     * [更新]
     * @author xq
     * @date 2021/02/06
     **/
    int update(RainfallMigrate rainfallMigrate);

    /**
     * [查询] 根据主键 id 查询
     * @author xq
     * @date 2021/02/06
     **/
    RainfallMigrate load(int id);

    /**
     * [查询] 分页查询
     * @author xq
     * @date 2021/02/06
     **/
    List<RainfallMigrate> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author xq
     * @date 2021/02/06
     **/
    int pageListCount(int offset,int pagesize);

    @Select("select * from rainfall_migrate where tm=#{tm}")
    @ResultMap("BaseResultMap")
    List<RainfallMigrate> selectByTM(Date tm);

    int insertMany(@Param("list") List<RainfallMigrate> list);

    List<RainfallMigrate> selectByTmStcd(@Param("tm") Date tm, @Param("list") List<String> list);
}
