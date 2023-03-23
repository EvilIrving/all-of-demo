package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.project.model.HardwareMaintenance;
import com.ygkj.project.model.SysFile;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.mapper.SysFileMapper;
import com.ygkj.water.project.service.HardwareMaintenanceService;
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
@Api(tags = "硬件设备维护")
@RestController
@RequestMapping("/hardwareMaintenance")
public class HardwareMaintenanceController {

    @Autowired
    private HardwareMaintenanceService hardwareMaintenanceService;
    @Resource
    private SysFileMapper sysFileMapper;

    @ApiOperation(value = "硬件设备维护新增或修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(HardwareMaintenance hardwareMaintenance) {
        if (StringUtils.isNotBlank(hardwareMaintenance.getId())) {
            if (!hardwareMaintenanceService.updateById(hardwareMaintenance)) {
                return CommonResult.failed("更新失败");
            }
        } else {
            if (!hardwareMaintenanceService.save(hardwareMaintenance)) {
                return CommonResult.failed("新增失败");
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "硬件设备维护列表")
    @PostMapping("/list")
    public CommonResult list(HardwareMaintenance hardwareMaintenance) {
        hardwareMaintenance.page();
        QueryWrapper<HardwareMaintenance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(hardwareMaintenance.getPid()), "prcd", hardwareMaintenance.getPid());
        queryWrapper.eq(hardwareMaintenance.getHandlingSuggestion() != null, "handling_suggestion", hardwareMaintenance.getHandlingSuggestion());
        queryWrapper.eq(hardwareMaintenance.getMaterialDate() != null, "material_date", hardwareMaintenance.getMaterialDate());
        queryWrapper.like(StringUtils.isNotBlank(hardwareMaintenance.getEquipmentName()),"equipment_name",hardwareMaintenance.getEquipmentName());
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByDesc("material_date");
        List<HardwareMaintenance> list = hardwareMaintenanceService.list(queryWrapper);
        PageVO<HardwareMaintenance> pageVO = PageUtils.getPageVO(list);
        List<HardwareMaintenance> list1 = pageVO.getList();
        list1.forEach(m->{
            if (StringUtils.isNotBlank(m.getAfterPhoto())) {
                String[] split = m.getAfterPhoto().split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                m.setAfterPhotoFiles(sysFiles);
            }
            if (StringUtils.isNotBlank(m.getBeforePhoto())) {
                String[] split = m.getBeforePhoto().split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                m.setBeforePhotoFiles(sysFiles);
            }
        });
        pageVO.setList(list1);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "硬件设备维护数据详情")
    @GetMapping("/getById")
    public CommonResult getById(String id) {
        HardwareMaintenance hardwareMaintenance = hardwareMaintenanceService.getById(id);
        if (hardwareMaintenance != null) {
            //查询工程名称
            String prcd = hardwareMaintenance.getPrcd();
            String name = hardwareMaintenanceService.getByPrcdName(prcd);
            hardwareMaintenance.setName(name);
            String afterPhoto = hardwareMaintenance.getAfterPhoto();
            String beforePhoto = hardwareMaintenance.getBeforePhoto();
            if (StringUtils.isNotBlank(afterPhoto)) {
                String[] split = afterPhoto.split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                hardwareMaintenance.setAfterPhotoFiles(sysFiles);
            }
            if (StringUtils.isNotBlank(beforePhoto)) {
                String[] split = beforePhoto.split(",");
                List<SysFile> sysFiles = sysFileMapper.listFile(Arrays.asList(split));
                hardwareMaintenance.setBeforePhotoFiles(sysFiles);
            }
        }
        return CommonResult.success(hardwareMaintenance);
    }

    @ApiOperation(value = "硬件设备维护删除")
    @GetMapping("/del")
    public CommonResult del(HardwareMaintenance hardwareMaintenance) {
        hardwareMaintenance.setDelFlag(1);
        if (!hardwareMaintenanceService.updateById(hardwareMaintenance)) {
            return CommonResult.failed("刪除失败");
        }
        return CommonResult.success();
    }
}
