package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.flood.model.AttTeamDeviceBase;
import com.ygkj.flood.model.AttTeamPersonBase;
import com.ygkj.flood.vo.request.*;
import com.ygkj.flood.vo.response.AttTeamBaseResVo;
import com.ygkj.flood.vo.response.AttTeamDeviceBaseRespVo;
import com.ygkj.flood.vo.response.AttTeamPersonResVo;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AttTeamService  {

    PageVO<AttTeamBaseResVo> selectByCondition(AttTeamBasePageQueryVo queryVo);

    /**
     * 统计防汛队伍--用于组件详表表头
     * @param queryVo
     * @return
     */
    JSONObject stats4PagedTeams(AttTeamBasePageQueryVo queryVo);

    /**
     * 导出防汛队伍
     * @param queryVo
     * @param response
     */
    void exportTeams(AttTeamBasePageQueryVo queryVo, HttpServletResponse response);

    boolean insertTeam(AttTeamBaseAddVo attTeamBaseAddVo);

    boolean updateTeam(AttTeamBaseAddVo attTeamBaseAddVo);

    boolean deleteTeam(String ids);

    PageVO<AttTeamPersonResVo> teamPersons(AttTeamPersonQueryVo attTeamPersonQueryVo, Integer pageSize, Integer pageNum);

    boolean insertTeamPerson(List<AttTeamPersonAddVo> list);

    boolean deletePersons(List<AttTeamPersonBase> list);

    JSONArray teamGroupByArea();

    List<JSONObject> teamGroupByType();

    JSONArray staticPeopleNumByArea();

    List<JSONObject> staticPeopleNumByType();

    /**
     * 统计项（队伍总数，队伍总人数）
     * @return
     */
    Map<String,Object> teamCount();


    JSONObject teamCountList();

    List<AttTeamDeviceBaseRespVo> teamDeviceList(AttTeamDeviceBaseReqVo vo);

    JSONObject teamDeviceByType();

    List<AttTeamDeviceBaseRespVo> selectteamDevice(AttTeamDeviceBaseReqVo vo);

    Integer insertteamDevice(AttTeamDeviceBase attTeamDeviceBase);

    Integer updateteamDevice(AttTeamDeviceBase attTeamDeviceBase);

    boolean deleteteamDevice(List<AttTeamDeviceBase> list);

    void exportTeamPersons(String teamId,String name,HttpServletResponse response);

    void exportTeamDevice(AttTeamDeviceBaseReqVo vo,HttpServletResponse response);

}
