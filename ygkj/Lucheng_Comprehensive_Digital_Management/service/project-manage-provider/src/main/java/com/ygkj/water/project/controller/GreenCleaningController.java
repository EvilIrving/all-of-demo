package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.GreenCleaning;
import com.ygkj.project.model.SysFile;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.GreenCleaningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author wgf
 * @date 2022/4/24 10:32
 * @Description:
 */
@Api(tags = "绿化保洁")
@RestController
@RequestMapping("/greenCleaning")
public class GreenCleaningController {

    @Autowired
    private GreenCleaningService greenCleaningService;
    @Resource
    private SysFileMapper sysFileMapper;

    @ApiOperation(value = "绿化保洁新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(GreenCleaning greenCleaning) {
        if (StringUtils.isNotBlank(greenCleaning.getId())) {
            if (!greenCleaningService.updateById(greenCleaning)) {
                return CommonResult.failed("更新失败");
            }
        } else {
            if (!greenCleaningService.save(greenCleaning)) {
                return CommonResult.failed("新增失败");
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "绿化保洁列表")
    @PostMapping("/list")
    public CommonResult list(GreenCleaning greenCleaning) {
        greenCleaning.page();
        QueryWrapper<GreenCleaning> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(greenCleaning.getImplementer()), "implementer", greenCleaning.getImplementer());
        queryWrapper.eq(StringUtils.isNotBlank(greenCleaning.getPrcd()), "prcd", greenCleaning.getPrcd());
        queryWrapper.eq(greenCleaning.getMaterialDate() != null, "material_date", greenCleaning.getMaterialDate());
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByDesc("material_date");
        List<GreenCleaning> list = greenCleaningService.list(queryWrapper);
        list.forEach(m->{
            if (StringUtils.isNotBlank(m.getStartPhoto())) {
                String[] split = m.getStartPhoto().split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                m.setStartPhotoFiles(sysFiles);
            }
            if (StringUtils.isNotBlank(m.getEndPhoto())) {
                String[] split = m.getEndPhoto().split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                m.setEndPhotoFiles(sysFiles);
            }
        });
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @ApiOperation(value = "绿化保洁数据详情")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        GreenCleaning greenCleaning = greenCleaningService.getById(id);
        if (greenCleaning != null) {
            //查询工程名称
            String prcd = greenCleaning.getPrcd();
            String name = greenCleaningService.getByPrcdName(prcd);
            greenCleaning.setName(name);
            String startPhoto = greenCleaning.getStartPhoto();
            String endPhoto = greenCleaning.getEndPhoto();
            if (StringUtils.isNotBlank(startPhoto)) {
                String[] split = startPhoto.split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                greenCleaning.setStartPhotoFiles(sysFiles);
            }
            if (StringUtils.isNotBlank(endPhoto)) {
                String[] split = endPhoto.split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                greenCleaning.setEndPhotoFiles(sysFiles);
            }
        }
        return CommonResult.success(greenCleaning);
    }

    @ApiOperation(value = "绿化保洁删除")
    @GetMapping("/del")
    public CommonResult del(GreenCleaning greenCleaning) {
        greenCleaning.setDelFlag(1);
        if (!greenCleaningService.updateById(greenCleaning)) {
            return CommonResult.failed("刪除失败");
        }
        return CommonResult.success();
    }
}
