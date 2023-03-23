package com.ygkj.controller;

import com.alibaba.excel.EasyExcel;
import com.ygkj.auth.VO.request.ArchiveLabelPageQueryVO;
import com.ygkj.auth.api.archiveLabel.model.SysArchiveLabel;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.SysArchiveLabelService;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description 档案标签表
 * @author hucong
 * @date 2021-01-19
 */
@RestController
@RequestMapping(value = "/ums/SysArchiveLabel")
@Api(tags = "档案标签模块")
public class SysArchiveLabelController {

    @Resource
    private SysArchiveLabelService sysArchiveLabelService;

    /**
     * 新增
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增接口")
    public CommonResult insert(SysArchiveLabel entity){
        try {
            if (entity.getId() == null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                String token = request.getHeader("authorization");
                if (StringUtils.isNotEmpty(token)) {
                    String userId = JwtTokenUtil.getUserId(token);
                    entity.setCreatorId(userId);
                }
                entity.setCreateTime(new Date());
                int i = sysArchiveLabelService.insert(entity);
                if (i > 0) {
                    return CommonResult.success(entity.getId());
                } else {
                    return CommonResult.failed("新增失败");
                }
            } else {
                entity.setUpdateTime(new Date());
                int i = sysArchiveLabelService.update(entity);
                if (i > 0) {
                    return CommonResult.success("更新成功");
                } else {
                    return CommonResult.failed("更新失败");
                }
            }

        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 刪除
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation(value = "删除接口")
    public CommonResult delete(int id){
        try {
            int i = sysArchiveLabelService.delete(id);
            if (i > 0) {
                return CommonResult.success("刪除成功");
            } else {
                return CommonResult.failed("刪除失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 更新
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation(value = "更新接口")
    public CommonResult update(SysArchiveLabel entity){
        try {
            entity.setUpdateTime(new Date());
            int i = sysArchiveLabelService.update(entity);
            if (i > 0) {
                return CommonResult.success("更新成功");
            } else {
                return CommonResult.failed("更新失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 查询 根据主键 id 查询
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/findById")
    @ResponseBody
    @ApiOperation(value = "根据主键id查找数据接口")
    public CommonResult findById(int id){
        try {
            SysArchiveLabel sysArchiveLabel = sysArchiveLabelService.load(id);
            return CommonResult.success(sysArchiveLabel);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    /**
     * 查询 分页查询
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/pageList")
    @ResponseBody
    @ApiOperation(value = "分页查询接口")
    public CommonResult pageList(ArchiveLabelPageQueryVO vo) {
        return CommonResult.success(sysArchiveLabelService.pageList(vo));
    }

    @PostMapping("/downLoad")
    @ResponseBody
    @ApiOperation(value = "标签导出")
    public CommonResult downLoad(@RequestBody ArchiveLabelPageQueryVO vo) {
        try {

            List<SysArchiveLabel> archiveLabels = sysArchiveLabelService.pageList(vo).getList();

            if (CollectionUtils.isNotEmpty(archiveLabels) && archiveLabels.size() >0) {
                File file1 = new File("");
                String fileName = file1.getCanonicalPath()+ File.separator+"xml\\" + System.currentTimeMillis() + ".xlsx";
                System.out.println(fileName);

                // 根据用户传入字段 假设我们要忽略 date
                Set<String> excludeColumnFiledNames = new HashSet<String>();
                excludeColumnFiledNames.add("date");
                excludeColumnFiledNames.add("id");
                excludeColumnFiledNames.add("type");
                excludeColumnFiledNames.add("creatorId");
                excludeColumnFiledNames.add("updateTime");
                excludeColumnFiledNames.add("delFlag");
                // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
                EasyExcel.write(fileName, SysArchiveLabel.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("标签")
                        .doWrite(archiveLabels);
            }

        	return CommonResult.success(null);
        } catch (Exception e) {
        	return CommonResult.failed(e.getMessage());
        }
    }

}

