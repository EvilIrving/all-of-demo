package com.ygkj.flood.mapper;

import com.ygkj.flood.model.AttTeamPersonBase;
import com.ygkj.flood.vo.request.AttTeamPersonAddVo;
import com.ygkj.flood.vo.request.AttTeamPersonQueryVo;
import com.ygkj.flood.vo.response.AttTeamPersonResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 队伍人员信息
 * @author huangzh
 * @date 2021/01/18
 */
public interface AttTeamPersonBaseMapper {

    /**
     * [新增]
     * @author huangzh
     * @date 2021/01/18
     **/
    int insert(AttTeamPersonBase attTeamPersonBase);

    int batchInsert(@Param("list") List<AttTeamPersonAddVo> list);

    /**
     * [刪除]
     * @author huangzh
     * @date 2021/01/18
     **/
    int deletePersons(List<AttTeamPersonBase> list);

    int deleteByTeamId(String teamId);

    /**
     * [更新]
     * @author huangzh
     * @date 2021/01/18
     **/
    int update(AttTeamPersonBase attTeamPersonBase);

    /**
     * [查询] 根据主键 id 查询
     * @author huangzh
     * @date 2021/01/18
     **/
    AttTeamPersonBase load(int id);

    /**
     * [查询] 分页查询
     * @author huangzh
     * @date 2021/01/18
     **/
    List<AttTeamPersonBase> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author huangzh
     * @date 2021/01/18
     **/
    int pageListCount(int offset,int pagesize);

    List<AttTeamPersonResVo> selectByCondition(AttTeamPersonQueryVo queryVo);

}

