package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.ProjectDeclareReqVo;
import com.ygkj.auth.VO.request.ProjectMatterAddReqVo;
import com.ygkj.auth.VO.request.ProjectMatterPageReqVo;
import com.ygkj.auth.api.monitor.BusProjectMatterControllerApi;
import com.ygkj.auth.api.monitor.model.BusProjectMatter;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fml
 * @Date: 2020/9/17 11:07
 * @Description:
 */
@RestController
@RequestMapping("/projectMatter")
public class BusProjectMatterController implements BusProjectMatterControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @Override
    @PostMapping("/insert")
    public Object insert(ProjectMatterAddReqVo vo){

        return authenticationClient.insertMatter(vo);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public Object delete(@PathVariable("id") String id){
        return authenticationClient.deleteMatter(id);
    }

    @Override
    @DeleteMapping("/deleteMatters")
    public Object deleteMatters(@RequestParam("ids") String ids){
        return authenticationClient.deleteMatters(ids);
    }

    @Override
    @PutMapping("/update")
    public Object update(BusProjectMatter busProjectMatter){
        return authenticationClient.updateMatter(busProjectMatter);
    }

    @Override
    @GetMapping("/load/{id}")
    public Object load(@PathVariable("id") String id){
        return authenticationClient.loadMatter(id);
    }

    @GetMapping("/loadFiles/{id}")
    public Object loadFiles(@PathVariable("id") String id){
        return authenticationClient.loadMatterFiles(id);
    }

    @Override
    @PostMapping("/pageList")
    public Object pageList(ProjectMatterPageReqVo vo) {
        return authenticationClient.pageListMatter(vo);
    }

    @Override
    @PostMapping("/saveProject")
    public Object saveProject(ProjectDeclareReqVo vo){

        return authenticationClient.saveProject(vo);
    }

    @Override
    @PostMapping("/submitProject")
    public Object submitProject(ProjectDeclareReqVo vo){

        return authenticationClient.submitProject(vo);
    }
}
