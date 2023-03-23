package com.ygkj.big.screen.controller;

import com.ygkj.big.screen.BusDuplicateCollectionControllerApi;
import com.ygkj.big.screen.model.BusDuplicateCollection;
import com.ygkj.big.screen.service.BusDuplicateCollectionService;
import com.ygkj.big.screen.vo.request.BusDuplicateCollectionReq;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 13:32
 */
@RestController
@RequestMapping("duplicateCollection")
public class BusDuplicateCollectionController implements BusDuplicateCollectionControllerApi {

    @Autowired
    private BusDuplicateCollectionService busDuplicateCollectionService;
    @Resource
    private GenerateDistributionIDService generateDistributionIDService;

    @PostMapping("saveOrUpdate")
    @Override
    public CommonResult saveOrUpdate(BusDuplicateCollectionReq entity) {
        BusDuplicateCollection busDuplicateCollection = new BusDuplicateCollection();
        BeanUtils.copyProperties(entity, busDuplicateCollection);
        if (!StringUtils.isEmpty(entity.getStartTime()) && !StringUtils.isEmpty(entity.getEndTime())) {
            try {
                busDuplicateCollection.setTimeInterval(DateUtils.getDays(entity.getStartTime(), entity.getEndTime()) + "");
            } catch (Exception e) {
            }
        }
        if (StringUtils.isEmpty(entity.getId())) {
            busDuplicateCollection.setId(generateDistributionIDService.nextID());
            busDuplicateCollectionService.insert(busDuplicateCollection);
            return CommonResult.success(busDuplicateCollection.getId());
        } else {
            busDuplicateCollectionService.update(busDuplicateCollection);
            return CommonResult.success(busDuplicateCollection.getId());
        }
    }

    @GetMapping("del")
    @Override
    public CommonResult del(String id) {
        busDuplicateCollectionService.delete(id);
        return CommonResult.success("删除成功");
    }

    @PostMapping("getList")
    @Override
    public CommonResult getList(BusDuplicateCollectionReq entity) {
        return CommonResult.success(busDuplicateCollectionService.getList(entity));
    }
}
