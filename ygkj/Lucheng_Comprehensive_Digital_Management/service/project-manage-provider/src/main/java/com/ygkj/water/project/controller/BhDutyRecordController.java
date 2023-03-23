package com.ygkj.water.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.project.model.BhDutyRecord;
import com.ygkj.project.model.ProjectCodeMapping;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import com.ygkj.water.project.service.BhDutyRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2022/4/26 14:01
 * @Description:
 */
@Api(tags = "值班管理")
@RestController
@RequestMapping("/bhDutyRecord")
public class BhDutyRecordController {

    @Autowired
    private BhDutyRecordService bhDutyRecordService;

    @ApiOperation(value = "值班管理列表 传id 查单条")
    @PostMapping("/list")
    public CommonResult list(BhDutyRecord bhDutyRecord){
        PageVO<BhDutyRecord> list = bhDutyRecordService.getList(bhDutyRecord);
        return CommonResult.success(list);
    }

    @ApiOperation(value = "值班管理新增和修改")
    @PostMapping("/saveOrUpdate")
    public CommonResult saveOrUpdate(BhDutyRecord bhDutyRecord){
        String id = bhDutyRecord.getId();
        String pid = bhDutyRecord.getPid();
        Date dutyDate = bhDutyRecord.getDutyDate();
        String patrolType = bhDutyRecord.getPatrolType();
        if (StringUtils.isBlank(pid) || dutyDate == null ||StringUtils.isBlank(patrolType)){
            return CommonResult.failed("工程编码、值班日期、巡查类型必填");
        }
        //首先将工程编码替换成标化的
        pid = bhDutyRecordService.selectForeignCode(
                ProjectCodeMapping.builder().type(1)
                        .projectType(null)
                        .localPrcd(pid)
                        .build());
        pid = pid == null?bhDutyRecord.getPid():pid;
        bhDutyRecord.setPid(pid);
        List<String> types = new ArrayList<>();
        if(bhDutyRecord.getPatrolType().contains("日常巡查")){
            types.add("1");
        }else if (bhDutyRecord.getPatrolType().contains("汛前检查")){
            types.add("2");
        }else if (bhDutyRecord.getPatrolType().contains("年度检查")){
            types.add("3");
        }else if (bhDutyRecord.getPatrolType().contains("特别检查")){
            types.add("4");
        }
        bhDutyRecord.setPatrolType(String.join(",",types));
        if (StringUtils.isNotBlank(id)){
            BhDutyRecord service = bhDutyRecordService.getById(id);
            if (service != null && service.getSourceType() == 1){
                return CommonResult.failed("不能修改同步进来的数据");
            }
            if(bhDutyRecordService.updateById(bhDutyRecord)){
                return CommonResult.success();
            }
        } else {
            //新增 先判断该工程下的该时间的数据是否存在。
            QueryWrapper<BhDutyRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PID", pid);
            queryWrapper.eq("LEFT(DUTY_DATE,10)", DateUtils.format(dutyDate,"yyyy-MM-dd"));
            BhDutyRecord dutyRecord = bhDutyRecordService.getOne(queryWrapper);
            if (dutyRecord != null){
                return CommonResult.failed("所选的工程和值班日期的数据已存在");
            }
            bhDutyRecord.setId(IdWorker.get32UUID());
            bhDutyRecord.setSourceType(2);
            if(bhDutyRecordService.save(bhDutyRecord)){
                return CommonResult.success();
            }
        }
        return CommonResult.failed("新增或修改失败");
    }

    @ApiOperation(value = "值班管理删除")
    @GetMapping("/del")
    public CommonResult del(BhDutyRecord bhDutyRecord) {
        BhDutyRecord service = bhDutyRecordService.getById(bhDutyRecord.getId());
        int type = service.getSourceType();
        if (type == 1){
            return CommonResult.failed("不能删除同步进来的数据");
        }
        if (bhDutyRecordService.removeById(bhDutyRecord.getId())){
            return CommonResult.success();
        } else {
            return CommonResult.failed("删除失败");
        }
    }

    @ApiOperation(value = "导出")
    @PostMapping("/exportDuty")
    public void exportDuty(HttpServletResponse response,@RequestBody BhDutyRecord bhDutyRecord){
        bhDutyRecordService.exportDuty(response,bhDutyRecord);
    }

    @ApiOperation(value = "导入")
    @PostMapping("/importDuty")
    public CommonResult importDuty(MultipartFile file,String pid) throws Exception {
        try {
            return CommonResult.success(bhDutyRecordService.importDuty(file,pid));
        }catch (RuntimeException r){
            return CommonResult.failed(r.getMessage());
        }

    }


}
