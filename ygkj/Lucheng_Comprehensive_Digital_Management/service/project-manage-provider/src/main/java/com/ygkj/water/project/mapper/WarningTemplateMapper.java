package com.ygkj.water.project.mapper;

import com.ygkj.project.model.WarningTemplate;
import com.ygkj.project.vo.request.WarningTemplateAddVo;
import com.ygkj.project.vo.request.WarningTemplateQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报警模板配置
 *
 * @author huangzh
 * @date 2020/12/29
 */
public interface WarningTemplateMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2020/12/29
     **/
    int insert(WarningTemplateAddVo warningTemplateAddVo);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2020/12/29
     **/
    int delete(int id);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2020/12/29
     **/
    int update(WarningTemplateAddVo warningTemplateAddVo);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2020/12/29
     **/
    WarningTemplate load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2020/12/29
     **/
    List<WarningTemplate> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2020/12/29
     **/
    int pageListCount(int offset, int pagesize);

    List<WarningTemplate> selectByCondition(WarningTemplateQueryVo queryVo);

    int batchDelete(@Param("list") List<String> list);

    String loadContent(@Param("id") String id);

}
