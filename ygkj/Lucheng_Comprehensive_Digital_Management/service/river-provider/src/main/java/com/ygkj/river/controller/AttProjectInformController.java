package com.ygkj.river.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.river.AttProjectInformControllerApi;
import com.ygkj.river.model.AttProjectInform;
import com.ygkj.river.service.AttProjectInformService;
import com.ygkj.river.vo.request.InformReqVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-07-12 10:08
 * @description  项目举报
 */
@RestController
@RequestMapping(value = "/projectInform")
public class AttProjectInformController implements AttProjectInformControllerApi {


    @Resource
    private AttProjectInformService attProjectInformService;


    @Override
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdateInform(@Valid @RequestBody InformReqVo reqVo) {

        String userId = JwtTokenUtil.getCurrentUserId().orElse("-");
        String userName = JwtTokenUtil.getCurrentUserName().orElse("-");
        AttProjectInform inform = new AttProjectInform();
        BeanUtil.copyProperties(reqVo,inform);
        inform.setCreateTime(new Date());
        inform.setCreateId(userId);
        inform.setInitiatorId(userId);
        inform.setInitiator(userName);
        if (attProjectInformService.saveOrUpdate(inform)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }

    }

    @Override
    @PostMapping("/getInformList")
    public CommonResult getInformList(@Valid InformReqVo reqVo) {

        QueryWrapper<AttProjectInform> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag",0);
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getInformContent()),"inform_content", reqVo.getInformContent());
        queryWrapper.eq("project_id",reqVo.getProjectId());
        reqVo.page();
        List<AttProjectInform> list = attProjectInformService.list(queryWrapper);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @Override
    @GetMapping("/delete")
    public CommonResult deleteInform(String id) {

        AttProjectInform inform = new AttProjectInform();
        inform.setId(id);
        inform.setDelFlag(1);
        if (attProjectInformService.updateById(inform)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }

    @Override
    @GetMapping("/detail")
    public CommonResult detail(String id) {

        return CommonResult.success(attProjectInformService.getById(id));
    }
}
