package com.ygkj.flood.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.flood.DefenObjectControllerApi;
import com.ygkj.flood.model.AttDefenObjectBase;
import com.ygkj.flood.service.AttDefenObjectBaseService;
import com.ygkj.flood.vo.request.DefenObjectReqVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lxl
 * @create 2022-05-17 16:17
 * @description
 */
@RestController
@RequestMapping("/defenObject")
public class DefenObjectController implements DefenObjectControllerApi {

    @Resource
    private AttDefenObjectBaseService attDefenObjectBaseService;


    @Override
    @PostMapping("/list")
    public CommonResult defenObjectList(DefenObjectReqVo reqVo) {
        reqVo.page();
        QueryWrapper<AttDefenObjectBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag",0);
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getId()),"id",reqVo.getId());
        queryWrapper.and(StringUtils.isNotBlank(reqVo.getKey()),
                q -> q.like(StringUtils.isNotBlank(reqVo.getKey()),"defen_object",reqVo.getKey())
                        .or().like(StringUtils.isNotBlank(reqVo.getKey()),"admin_adnm",reqVo.getKey())
                        .or().like(StringUtils.isNotBlank(reqVo.getKey()),"tcharger",reqVo.getKey())
                        .or().like(StringUtils.isNotBlank(reqVo.getKey()),"town",reqVo.getKey())
                        .or().like(StringUtils.isNotBlank(reqVo.getKey()),"natural_adnm",reqVo.getKey()));

        queryWrapper.orderByDesc("create_time");
        List<AttDefenObjectBase> list = attDefenObjectBaseService.list(queryWrapper);

        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @Override
    @PostMapping("/saveOrUpdate")
    public CommonResult defenObjectSaveOrUpdate(AttDefenObjectBase base) {

        if (StringUtils.isNotBlank(base.getId())){
            if (attDefenObjectBaseService.updateById(base)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }else {
            base.setId(IdWorker.get32UUID());
            if (attDefenObjectBaseService.save(base)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }

    }

    @Override
    @GetMapping("/delete")
    public CommonResult defenObjectDelete(String id) {

        AttDefenObjectBase base = new AttDefenObjectBase();
        base.setId(id);
        base.setDelFlag(1);
        if (attDefenObjectBaseService.updateById(base)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }
}
