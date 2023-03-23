package com.ygkj.controller;

import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.archivecollection.model.SysArchiveCollection;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.entity.SysUser;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.service.SysArchiveCollectionService;
import com.ygkj.auth.VO.request.SysArchiveCollectionReqVO;
import com.ygkj.vo.req.SysFileReqVo;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.water.model.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ygkj.com.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 档案收藏表
 * @author hucong
 * @date 2021-01-19
 */
@RestController
@RequestMapping(value = "/ums/SysArchiveCollection")
@Api(tags = "档案收藏模块")
public class SysArchiveCollectionController {

    @Resource
    private SysArchiveCollectionService sysArchiveCollectionService;

    /**
     * 新增
     * @author hucong
     * @date 2021/01/19
     **/
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增接口")
    public CommonResult insert(SysArchiveCollection entity){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("authorization");
            if (StringUtils.isNotEmpty(token)) {
                String userId = JwtTokenUtil.getUserId(token);
                String userName = JwtTokenUtil.getUserName(token);
                entity.setCreateName(userName);
                entity.setCreatorId(userId);
            }
            entity.setCreateTime(new Date());
            int i = sysArchiveCollectionService.insert(entity);
            if (i > 0) {
                return CommonResult.success("新增成功");
            } else {
                entity.setUpdateTime(new Date());
                return CommonResult.failed("新增失败");
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
            int i = sysArchiveCollectionService.delete(id);
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
    public CommonResult update(SysArchiveCollection entity){
        try {
            entity.setUpdateTime(new Date());
            int i = sysArchiveCollectionService.update(entity);
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
            SysArchiveCollection archiveCollection = sysArchiveCollectionService.load(id);
            return CommonResult.success(archiveCollection);
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
    public CommonResult pageList(SysFileReqVo vo) {
        return CommonResult.success(sysArchiveCollectionService.pageList(vo));
    }

    @PostMapping("/cancelCollection")
    @ApiOperation(value = "取消收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileId", value = "文件id", paramType = "query"),
            @ApiImplicitParam(name = "achievementId", value = "档案id", paramType = "query"),
    })
    public CommonResult cancelCollection(String fileId,String achievementId) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("authorization");
            String userId = null;
            if (StringUtils.isNotEmpty(token)) {
                userId = JwtTokenUtil.getUserId(token);
            }
            if (StringUtils.isEmpty(userId)) {
                return CommonResult.failed("用户id为空");
            }
            int i = sysArchiveCollectionService.cancelCollection(userId,achievementId,fileId);
            if (i > 0) {
                return CommonResult.success("取消成功");
            } else {
                return CommonResult.failed("取消失败");
            }
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

}

