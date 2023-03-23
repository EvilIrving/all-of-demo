package com.ygkj.consumer.auth.controller;

import com.ygkj.auth.VO.request.BusProjectPageReqVo;
import com.ygkj.auth.VO.request.ProjectFileReqVos;
import com.ygkj.auth.VO.request.ProjectTagUpdateReqVo;
import com.ygkj.auth.api.monitor.BusProjectControllerApi;
import com.ygkj.auth.api.monitor.model.BusProject;
import com.ygkj.consumer.auth.client.authentication.AuthenticationClient;
import com.ygkj.consumer.auth.client.authentication.WmsClient;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: fml
 * @Date: 2020/9/8 22:53
 * @Description:
 */
//@RestController
//@RequestMapping("/BusProject")
public class BusProjectController implements BusProjectControllerApi {

    @Autowired
    private AuthenticationClient authenticationClient;

    @Autowired
    private WmsClient wmsClient;

    @Override
    @PostMapping("/insert")
    public Object insertBusProject(BusProject busProject) {
        return authenticationClient.insertBusProject(busProject);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public Object deleteBusProject(@PathVariable("id") String id) {
        return authenticationClient.deleteBusProject(id);
    }

    @Override
    @PutMapping("/update")
    public Object updateBusProject(BusProject busProject) {
        return authenticationClient.updateBusProject(busProject);
    }

    @Override
    @GetMapping("/load/{id}")
    public Object loadBusProject(@PathVariable("id") String id) {
        return authenticationClient.loadBusProject(id);
    }

    @Override
    @GetMapping("/loadFiles/{id}")
    public Object loadFiles(@PathVariable("id") String id) {
        return authenticationClient.loadObjectFiles(id);
    }

    @Override
    @PostMapping("/pageList")
    public Object pageListBusProject(BusProjectPageReqVo vo) {
        return authenticationClient.pageListBusProject(vo);
    }

    /**
     * 查询 分页查询-已办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @Override
    @PostMapping("/queryDoneProject")
    public Object queryDoneProject(BusProjectPageReqVo vo) {
        return authenticationClient.queryDoneProject(vo);
    }

    /**
     * 查询 分页查询-补正项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @Override
    @PostMapping("/querycCorrectProject")
    public Object querycCorrectProject(BusProjectPageReqVo vo) {
        return authenticationClient.querycCorrectProject(vo);
    }

    /**
     * 查询 分页查询-退办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @Override
    @PostMapping("/querycBreakProject")
    public Object querycBreakProject(BusProjectPageReqVo vo) {
        return authenticationClient.querycBreakProject(vo);
    }

    /**
     * 查询 分页查询-已申报待办项目
     *
     * @author fml
     * @date 2020/09/08
     **/
    @Override
    @PostMapping("/querycDeclaredProject")
    public Object querycDeclaredProject(BusProjectPageReqVo vo) {
        return authenticationClient.querycDeclaredProject(vo);
    }

    /**
     * 上报
     *
     * @author fml
     * @date 2020/09/18
     **/
    @Override
    @PutMapping("/updateReportStatus/{id}")
    public Object updateReportStatus(@PathVariable("id") String id) {

        return authenticationClient.updateReportStatus(id);
    }

    /**
     * 上传资料
     *
     * @author fml
     * @date 2020/09/18
     **/
    @Override
    @PostMapping("/uploadFiles/{id}")
    public Object uploadFiles(@PathVariable("id") String id, ProjectFileReqVos vos) {

        return authenticationClient.uploadFiles(id, vos);
    }

    /**
     * 未申报项目修改时上报
     *
     * @author fml
     * @date 2020/09/18
     **/
    @Override
    @PostMapping("/updateReport")
    public Object updateReport(BusProject busProject) {
        return authenticationClient.updateReport(busProject);
    }

    @GetMapping("/queryClosestProject")
    @Override
    public CommonResult queryClosestProject(BusProject project) {
        return wmsClient.queryClosestProject();
    }

    @Override
    @PostMapping("/addTags")
    public Object addTags(@RequestParam("id") String id, @RequestParam("tagId") String tagId) {
        return authenticationClient.addTags(id, tagId);
    }

    @Override
    @PutMapping("/updateTags")
    public Object updateTags(@RequestParam("ids") String ids, @RequestParam("tagId") String tagId) {
        return authenticationClient.updateTags(ids, tagId);
    }

    @Override
    @DeleteMapping("/deletedTags")
    public Object deletedTags(@RequestParam("id") String id) {
        return authenticationClient.deletedTags(id);
    }

    @Override
    @PostMapping("/addProjectTag")
    public Object addProjectTag(@RequestParam("tag") String tag) {
        return authenticationClient.addProjectTag(tag);
    }

    @Override
    @PostMapping("/getProjectTags")
    public Object getProjectTags(@RequestParam("stage") String stage) {
        return authenticationClient.getProjectTags(stage);
    }

    @Override
    @GetMapping("/getProjectTagById/{tagId}")
    public Object getProjectTagById(@PathVariable("tagId") String tagId) {

        return authenticationClient.getProjectTagById(tagId);
    }

    @Override
    @PutMapping("/updateProjectTag")
    public Object updateProjectTag(ProjectTagUpdateReqVo vo) {
        return authenticationClient.updateProjectTag(vo);
    }
}
