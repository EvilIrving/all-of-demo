package com.ygkj.flood.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.AttTeamBase;
import com.ygkj.flood.vo.request.AttTeamBasePageQueryVo;
import com.ygkj.flood.vo.request.AttTeamDeviceBaseReqVo;
import com.ygkj.flood.vo.response.AttTeamBaseResVo;
import com.ygkj.flood.vo.response.AttTeamDeviceBaseRespVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 抢险队伍信息
 * @author huangzh
 * @date 2021/01/18
 */
public interface AttTeamBaseMapper {

    /**
     * [新增]
     * @author huangzh
     * @date 2021/01/18
     **/
    int insert(AttTeamBase attTeamBase);

    /**
     * [刪除]
     * @author huangzh
     * @date 2021/01/18
     **/
    int delete(@Param("ids") String ids);

    /**
     * [更新]
     * @author huangzh
     * @date 2021/01/18
     **/
    int update(AttTeamBase attTeamBase);

    /**
     * [查询] 根据主键 id 查询
     * @author huangzh
     * @date 2021/01/18
     **/
    AttTeamBaseResVo load(String id);

    /**
     * [查询] 分页查询
     * @author huangzh
     * @date 2021/01/18
     **/
    List<AttTeamBase> pageList(int offset,int pagesize);

    /**
     * [查询] 分页查询 count
     * @author huangzh
     * @date 2021/01/18
     **/
    int pageListCount(int offset,int pagesize);

    List<AttTeamBaseResVo> selectByCondition(AttTeamBasePageQueryVo queryVo);

    @Select("SELECT SUM(existing_number) as existing_number, name FROM att_team_base WHERE del_flag = 0\n" +
            "GROUP BY `name`")
    List<Map<String,Object>> teamCount();

    List<JSONObject> teamGroupByType();

    List<JSONObject> staticPeopleNumByType();

    /**
     * 统计队伍数量
     * @return
     */
    @Select("select count(1) from att_team_base where del_flag = 0")
    Integer countTeamNum();

    /**
     * 统计队伍总人数
     * @return
     */
    @Select("select sum(existing_number) from att_team_base where del_flag = 0")
    Integer countExistingNumber();

    List<AttTeamDeviceBaseRespVo> teamDeviceList(AttTeamDeviceBaseReqVo vo);

    @Select("select type,sum(num) as num from att_team_device_base where del_flag = 0 group by type")
    List<Map<String,Object>> teamDeviceByType();
}

