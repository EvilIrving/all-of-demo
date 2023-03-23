package com.ygkj.flood;

import com.ygkj.flood.model.AttTeamDeviceBase;
import com.ygkj.flood.model.AttTeamPersonBase;
import com.ygkj.flood.vo.request.*;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "防汛队伍",value = "防汛队伍")
public interface TeamControllerApi {

    @ApiOperation("防汛队伍查询")
    CommonResult teams(AttTeamBasePageQueryVo queryVo);

    @ApiOperation("统计防汛队伍--用于组件详表表头")
    CommonResult stats4PagedTeams(AttTeamBasePageQueryVo queryVo);

    @ApiOperation("导出防汛队伍")
    void exportTeams(AttTeamBasePageQueryVo queryVo, HttpServletResponse response);

    @ApiOperation("防汛队伍新增")
    CommonResult insertTeam(AttTeamBaseAddVo attTeamBaseAddVo);
    @ApiOperation("防汛队伍更新")
    CommonResult updateTeam(AttTeamBaseAddVo attTeamBaseAddVo);
    @ApiOperation("防汛队伍删除")
    CommonResult deleteTeam(String ids);
    @ApiOperation("防汛队伍人员详细信息")
    CommonResult teamPersons(AttTeamPersonQueryVo attTeamPersonQueryVo,Integer pageSize, Integer pageNum);
    @ApiOperation("防汛队伍人员更新（新增通用）")
    CommonResult insertPersons(List<AttTeamPersonAddVo> list);
    @ApiOperation("防汛队伍人员删除")
    CommonResult deletePersons(List<AttTeamPersonBase> list);

    @ApiOperation("防汛队伍根据行政区划划分-队伍数量")
    CommonResult teamGroupByArea();
    @ApiOperation("防汛队伍根据类型划分-队伍数量")
    CommonResult teamGroupByType();
    @ApiOperation("防汛队伍根据行政区划划分-人员数量")
    CommonResult staticPeopleNumByArea();
    @ApiOperation("防汛队伍根据类型划分-人员数量")
    CommonResult staticPeopleNumByType();

    /**
     * 统计项（队伍总数，队伍总人数）
     * @return
     */
    @ApiOperation("防汛队伍统计")
    CommonResult teamCount();


    @ApiOperation("救援队伍C位指标项")
    CommonResult teamCountList();

    @ApiOperation("队伍设备列表")
    CommonResult teamDeviceList(AttTeamDeviceBaseReqVo vo);

    @ApiOperation("队伍设备类型统计")
    CommonResult teamDeviceByType();

    @ApiOperation("队伍设备查询")
    CommonResult selectteamDevice(AttTeamDeviceBaseReqVo vo);

    @ApiOperation("队伍设备新增")
    CommonResult insertteamDevice(AttTeamDeviceBase attTeamDeviceBase);

    @ApiOperation("队伍设备修改")
    CommonResult updateteamDevice(AttTeamDeviceBase attTeamDeviceBase);

    @ApiOperation("队伍设备删除")
    CommonResult deleteteamDevice(List<AttTeamDeviceBase> list);

    @ApiOperation("导出救援队伍人员")
    void exportTeamPersons(String teamId,String name,HttpServletResponse response);

    @ApiOperation("导出救援队伍设备")
    void exportTeamDevice(AttTeamDeviceBaseReqVo vo,HttpServletResponse response);
}
