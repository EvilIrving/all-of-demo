package com.ygkj.flood.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ygkj.flood.TeamControllerApi;
import com.ygkj.flood.model.AttTeamDeviceBase;
import com.ygkj.flood.model.AttTeamPersonBase;
import com.ygkj.flood.service.AttTeamService;
import com.ygkj.flood.vo.request.*;
import com.ygkj.flood.vo.response.AttTeamDeviceBaseRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController implements TeamControllerApi {

    @Autowired
    private AttTeamService teamService;

    @PostMapping("/teams")
    @Override
    public CommonResult teams(AttTeamBasePageQueryVo queryVo) {
        return CommonResult.success(teamService.selectByCondition(queryVo));
    }

    @PostMapping("/stats4PagedTeams")
    @Override
    public CommonResult stats4PagedTeams(AttTeamBasePageQueryVo queryVo) {
        return CommonResult.success(teamService.stats4PagedTeams(queryVo));
    }

    @PostMapping("/exportTeams")
    @Override
    public void exportTeams(AttTeamBasePageQueryVo queryVo, HttpServletResponse response) {
        teamService.exportTeams(queryVo,response);
    }

    @PutMapping("/insertTeam")
    @Override
    public CommonResult insertTeam(AttTeamBaseAddVo attTeamBaseAddVo) {
        if (teamService.insertTeam(attTeamBaseAddVo)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/updateTeam")
    @Override
    public CommonResult updateTeam(AttTeamBaseAddVo attTeamBaseAddVo) {
        if (teamService.updateTeam(attTeamBaseAddVo)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/deleteTeam")
    @Override
    public CommonResult deleteTeam(String ids) {
        if (teamService.deleteTeam(ids)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @GetMapping("/teamPersons")
    @Override
    public CommonResult teamPersons(AttTeamPersonQueryVo attTeamPersonQueryVo, Integer pageSize, Integer pageNum) {
        return CommonResult.success(teamService.teamPersons(attTeamPersonQueryVo,pageSize,pageNum));
    }

    @PutMapping("/insertPersons")
    @Override
    public CommonResult insertPersons(@RequestBody List<AttTeamPersonAddVo> list) {
        if (teamService.insertTeamPerson(list)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/insertPerson")
    public CommonResult insertPerson(AttTeamPersonAddVo attTeamPersonAddVo) {
        ArrayList<AttTeamPersonAddVo> list = new ArrayList<>();
        list.add(attTeamPersonAddVo);
        if (teamService.insertTeamPerson(list)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/deletePersons")
    @Override
    public CommonResult deletePersons(@RequestBody List<AttTeamPersonBase> list) {
        if (teamService.deletePersons(list)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PutMapping("/deletePerson")
    public CommonResult deletePerson(AttTeamPersonBase attTeamPersonBase) {
        ArrayList<AttTeamPersonBase> list = new ArrayList<>();
        list.add(attTeamPersonBase);
        if (teamService.deletePersons(list)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @GetMapping("/teamGroupByArea")
    @Override
    public CommonResult teamGroupByArea() {
        return CommonResult.success(teamService.teamGroupByArea());
    }

    @GetMapping("teamGroupByType")
    @Override
    public CommonResult teamGroupByType() {
        return CommonResult.success(teamService.teamGroupByType());
    }

    @GetMapping("staticPeopleNumByArea")
    @Override
    public CommonResult staticPeopleNumByArea() {
        return CommonResult.success(teamService.staticPeopleNumByArea());
    }

    @GetMapping("staticPeopleNumByType")
    @Override
    public CommonResult staticPeopleNumByType() {
        return CommonResult.success(teamService.staticPeopleNumByType());
    }

    @Override
    @GetMapping("/teamCount")
    public CommonResult teamCount() {
        return CommonResult.success(teamService.teamCount());
    }

    @GetMapping("/teamCountList")
    @Override
    public CommonResult teamCountList() {
        return CommonResult.success(teamService.teamCountList());
    }

    @GetMapping("/teamDeviceList")
    @Override
    public CommonResult teamDeviceList(AttTeamDeviceBaseReqVo vo) {
        if(vo.getPageNum()!=null&&vo.getPageNum()>0
                &&vo.getPageSize()!=null&&vo.getPageSize()>0)
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        PageVO<AttTeamDeviceBaseRespVo> page=PageUtils.getPageVO(teamService.teamDeviceList(vo));
        return CommonResult.success(page);
    }

    @GetMapping("/teamDeviceByType")
    @Override
    public CommonResult teamDeviceByType() {
        return CommonResult.success(teamService.teamDeviceByType());
    }

    @PostMapping("/selectteamDevice")
    @Override
    public CommonResult selectteamDevice(AttTeamDeviceBaseReqVo vo) {
        if(vo.getPageNum()!=null&&vo.getPageNum()>0
                &&vo.getPageSize()!=null&&vo.getPageSize()>0)
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        PageVO<AttTeamDeviceBaseRespVo> page=PageUtils.getPageVO(teamService.selectteamDevice(vo));
        return CommonResult.success(page);
    }

    @PostMapping("/insertteamDevice")
    @Override
    public CommonResult insertteamDevice(AttTeamDeviceBase attTeamDeviceBase) {
        if (teamService.insertteamDevice(attTeamDeviceBase)>0){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/updateteamDevice")
    @Override
    public CommonResult updateteamDevice(AttTeamDeviceBase attTeamDeviceBase) {
        if (teamService.updateteamDevice(attTeamDeviceBase)>0){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/deleteteamDevice")
    @Override
    public CommonResult deleteteamDevice(@RequestBody List<AttTeamDeviceBase> list) {
        if (teamService.deleteteamDevice(list)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/deleteDevice")
    public CommonResult deleteDevice(AttTeamDeviceBase attTeamDeviceBase) {
        ArrayList<AttTeamDeviceBase> list = new ArrayList<>();
        list.add(attTeamDeviceBase);
        if (teamService.deleteteamDevice(list)){
            return CommonResult.success("操作成功");
        }
        return CommonResult.failed("操作失败");
    }

    @PostMapping("/exportTeamPersons")
    @Override
    public void exportTeamPersons(String teamId,String name,HttpServletResponse response) {
        teamService.exportTeamPersons(teamId,name,response);
    }

    @PostMapping("/exportTeamDevice")
    @Override
    public void exportTeamDevice(AttTeamDeviceBaseReqVo vo,HttpServletResponse response) {
        teamService.exportTeamDevice(vo,response);
    }
}
