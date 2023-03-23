package com.ygkj.water.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.DataMaintainDirectory;
import com.ygkj.project.model.DataMaintainInfo;
import com.ygkj.project.model.DataMaintainTask;
import com.ygkj.project.model.ThirdPartyUnitInfo;
import com.ygkj.project.vo.request.DataMaintainDirectoryVo;
import com.ygkj.utils.StringUtils;
import com.ygkj.warning.model.MsgSendRecord;
import com.ygkj.water.model.CommonResult;

import com.ygkj.water.project.feign.SendFeignClient;
import com.ygkj.water.project.service.DataMaintainDirectoryService;
import com.ygkj.water.project.service.DataMaintainInfoService;
import com.ygkj.water.project.service.DataMaintainTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "系统数据维护")
@RestController
@RequestMapping("/dataMaintenance")
public class SysDataMaintenanceController {
    @Resource
    private DataMaintainDirectoryService maintainDirectoryService;
    @Resource
    private DataMaintainInfoService infoService;
    @Resource
    private DataMaintainTaskService taskService;

    @Resource
    private SendFeignClient sendFeignClient;

    @ApiOperation(value = "新增目录")
    @PostMapping("/directory/insert")
    public CommonResult dirInsert(DataMaintainDirectory directory) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("directory_name",directory.getDirectoryName());
        wrapper.eq("pid",directory.getPid());
        DataMaintainDirectory one = maintainDirectoryService.getOne(wrapper);
        if (one != null){
            return CommonResult.failed("文件名不能重复！");
        }
        maintainDirectoryService.dirInsert(directory);
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id删除目录")
    @GetMapping("/directory/delete")
    @Deprecated
    public CommonResult dirDelete(String id) {
        List<String> strings = maintainDirectoryService.deptIds(id);
        if (maintainDirectoryService.removeByIds(strings)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "更新目录")
    @PostMapping("/directory/update")
    @Transactional
    public CommonResult dirUpdate(DataMaintainDirectory directory) {
        if ("c3d97e672114cada6f7d633dadec976b".equals(directory.getId())){
            return CommonResult.failed("该文件目录由于数据同步，无法修改！");
        }
        try {
            DataMaintainDirectory oldData = maintainDirectoryService.getById(directory.getId());
            //如果改了名字,这里会更新调所有下属目录路径
            if (!directory.getDirectoryName().equals(oldData.getDirectoryName())) {
                List<String> childIds = maintainDirectoryService.deptIds(directory.getId());
                List<String> collect = childIds.stream().filter(o -> !directory.getId().equals(o)).collect(Collectors.toList());
                if (collect.size() > 0) {
                    List<DataMaintainDirectory> directories = maintainDirectoryService.listByIds(collect);
                    String name = StringUtils.isNotBlank(oldData.getDirPath()) ? oldData.getDirPath() + "-" + oldData.getDirectoryName() : oldData.getDirectoryName();
                    String newName = StringUtils.isNotBlank(oldData.getDirPath()) ? oldData.getDirPath() + "-" + directory.getDirectoryName() : directory.getDirectoryName();
                    for (DataMaintainDirectory directory1 : directories) {
                        String dirId = directory1.getId();
                        String dirPath = directory1.getDirPath();
                        String directoryName = directory1.getDirectoryName();
                        String replace = dirPath.replace(name, newName);
                        //对下属维护信息及任务进行更新
                        maintainDirectoryService.updInfo(dirId,replace + directoryName);
                        maintainDirectoryService.updateTask(dirId,replace + directoryName);
                        directory1.setDirPath(replace);
                    }
                    maintainDirectoryService.saveOrUpdateBatch(directories);
                }

            }
            //自身及后续维护信息和任务更改
            maintainDirectoryService.updateById(directory);
            String newName = StringUtils.isNotBlank(oldData.getDirPath()) ? oldData.getDirPath() + "-" + directory.getDirectoryName() : directory.getDirectoryName();
            maintainDirectoryService.updInfo(directory.getId(),newName);
            maintainDirectoryService.updateTask(directory.getId(),newName);
            return CommonResult.success();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return CommonResult.failed();

    }


    @ApiOperation(value = "目录树查询")
    @PostMapping("/directory/tree")
    public CommonResult dirTree() {
        //查询根目录
        DataMaintainDirectory directory = maintainDirectoryService.getOne(new QueryWrapper<DataMaintainDirectory>().eq("pid", "0"));
        List<DataMaintainDirectory> directoryList = maintainDirectoryService.list();
        //递归查询目录树
        DataMaintainDirectoryVo directoryVo = new DataMaintainDirectoryVo();
        BeanUtils.copyProperties(directory, directoryVo);
        DataMaintainDirectoryVo maintainDirectoryVo = customVoList(directory, directoryVo, directoryList);
        return CommonResult.success(maintainDirectoryVo);
    }

    private DataMaintainDirectoryVo customVoList(DataMaintainDirectory directory, DataMaintainDirectoryVo directoryVo, List<DataMaintainDirectory> directorys) {
        if (directory != null) {
            //子集
            List<DataMaintainDirectoryVo> directoryVos = new ArrayList<>();
            for (DataMaintainDirectory directory1 : directorys) {
                if (directory1.getPid().equals(directory.getId())) {
                    DataMaintainDirectoryVo vo = new DataMaintainDirectoryVo();
                    BeanUtils.copyProperties(directory1, vo);
                    customVoList(directory1, vo, directorys);
                    directoryVos.add(vo);
                }
                if (directoryVos.size() > 0) {
                    directoryVo.setList(directoryVos);
                }
            }
        }
        return directoryVo;
    }

    @ApiOperation(value = "分页查询维护信息")
    @PostMapping("/info/queryPage")
    public CommonResult infonqueryPage(DataMaintainInfo info) {
        return CommonResult.success(infoService.queryPage(info));
    }

    @ApiOperation(value = "新增数据维护信息")
    @PostMapping("/info/insert")
    public CommonResult infoInsert(DataMaintainInfo info) {
        info.setId(IdWorker.get32UUID());
        infoService.save(info);
        return CommonResult.success();
    }

    @ApiOperation(value = "根据id删除数据维护信息")
    @GetMapping("/info/delete")
    public CommonResult infoDelete(String id) {
        if (infoService.removeById(id)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "更新数据维护信息")
    @PostMapping("/info/update")
    @Transactional
    public CommonResult infoUpdate(DataMaintainInfo info) {
        try {
            infoService.updateById(info);
            //更新任务相关信息
            infoService.updTask(info);
            return CommonResult.success();
        } catch (Exception e) {

        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "导出")
    @GetMapping("/exportMaintainInfo")
    public void exportMaintainInfo(HttpServletResponse response,DataMaintainInfo info) {
        infoService.exportMaintainInfo(response,info);
    }


    @ApiOperation(value = "数据维护任务新增")
    @PostMapping("/sendInfo/insert")
    public CommonResult sendInfoInsert(DataMaintainTask task) {
        try {
            task.setId(IdWorker.get32UUID());
            task.setStatus("0");
            task.setUnitHandleStatus("0");
            task.setDataMaintainStatus("0");
            taskService.sendInfoInsert(task);
            MsgSendRecord msgSendRecord = new MsgSendRecord();
            msgSendRecord.setSendType(1);
            msgSendRecord.setContext("【鹿城区水利局】温馨提示：您有一个"+task.getDataItem()+"的数据维护工单，请及时查看处理。");
            msgSendRecord.setBusinessType(2);
            //发送短信
            String unitDirectorPhone = task.getUnitDirectorPhone();
            if(StringUtils.isNotBlank(unitDirectorPhone)){
                msgSendRecord.setReceivePhone(unitDirectorPhone);
                sendFeignClient.synSend(msgSendRecord);
            }
            String dataMaintainPhone = task.getDataMaintainPhone();
            if(StringUtils.isNotBlank(dataMaintainPhone)){
                msgSendRecord.setReceivePhone(dataMaintainPhone);
                sendFeignClient.synSend(msgSendRecord);
            }
            String thirdPartyUnit = task.getThirdPartyUnit();
            if (StringUtils.isNotBlank(thirdPartyUnit)){
                List<ThirdPartyUnitInfo> thirdPartyUnitInfos = JSONArray.parseArray(thirdPartyUnit, ThirdPartyUnitInfo.class);
                for(ThirdPartyUnitInfo unitInfo : thirdPartyUnitInfos){
                    String phone = unitInfo.getUnitDirectorPhone();
                    if(StringUtils.isNotBlank(phone)){
                        msgSendRecord.setReceivePhone(phone);
                        sendFeignClient.synSend(msgSendRecord);
                    }
                }
            }
            return CommonResult.success();
        } catch (Exception e) {

        }
        return CommonResult.failed();
    }


    @ApiOperation(value = "数据维护任务删除")
    @GetMapping("/sendInfo/delete")
    public CommonResult sendInfodelete(String id) {
        //判断是否管理员用户,仅管理员用户可以删除
        String userId = JwtTokenUtil.getCurrentUserId().get();
        boolean res = infoService.judgeAdmin(userId);
        if (!res) {
            if (taskService.sendInfodelete(id)) {
                return CommonResult.success();
            } else {
                return CommonResult.failed();
            }
        } else {
            return CommonResult.failed("仅管理员用户可删除!");
        }
    }

    @ApiOperation(value = "数据维护任务更新---改为已处理")
    @PostMapping("/sendInfo/update")
    public CommonResult update(DataMaintainTask task) {
        try {
            String userId = JwtTokenUtil.getCurrentUserId().get();
            task.setHandleUserId(userId);
            taskService.updateTask(task);
            return CommonResult.success();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return CommonResult.failed();
    }

    @ApiOperation(value = "分页查询维护任务")
    @PostMapping("/sendInfo/queryPage")
    public CommonResult sendInfoqueryPage(DataMaintainTask task) {
        try {
        String userId = JwtTokenUtil.getCurrentUserId().get();
        task.setHandleUserId(userId);
        }catch (Exception r){
        }
        return CommonResult.success(taskService.queryPage(task));
    }
}
