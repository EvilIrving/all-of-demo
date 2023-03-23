package com.ygkj.flood.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ygkj.flood.AttWarehouseControllerApi;
import com.ygkj.flood.feign.UmsClient;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.model.AttWarehouseDeviceBase;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.service.AttWarehouseAddService;
import com.ygkj.flood.service.AttWarehouseOutService;
import com.ygkj.flood.service.AttWarehouseService;
import com.ygkj.flood.vo.request.AttWarehouseReqVo;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-05-19 17:59
 * @description
 */
@RestController
@RequestMapping("/attWarehouse")
public class AttWarehouseController implements AttWarehouseControllerApi {

    @Autowired
    private AttWarehouseAddService attWarehouseAddService;

    @Autowired
    private AttWarehouseOutService attWarehouseOutService;

    @Autowired
    private AttWarehouseService attWarehouseService;
    @Autowired
    private UmsClient umsClient;


    @Override
    @PostMapping("/warehouseAdd")
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult warehouseAdd(AttWarehouseAdd attWarehouseAdd) {

        if (StringUtils.isBlank(attWarehouseAdd.getWarehouseId())){
            return CommonResult.failed("未绑定仓库");
        }
        if (ObjectUtils.isNull(attWarehouseAdd.getUnivalent(),attWarehouseAdd.getNum())){
            return CommonResult.failed("缺少单价或者数量");
        }
        //更新单价
        attWarehouseAddService.setUnivalent(attWarehouseAdd.getUnivalent(),attWarehouseAdd.getMaterialName());

        //设置总价
        attWarehouseAdd.setAmount(attWarehouseAdd.getUnivalent().multiply(BigDecimal.valueOf(attWarehouseAdd.getNum())));
        attWarehouseAdd.setId(IdWorker.get32UUID());
        if (attWarehouseAddService.save(attWarehouseAdd)){
            //统一更新仓库对应资源数量
            attWarehouseService.addToDevice(attWarehouseAdd);
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }

    }


    @Override
    @PostMapping("/addList")
    public CommonResult warehouseAddList(AttWarehouseReqVo reqVo) {

        QueryWrapper<AttWarehouseAdd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getWarehouseId()),"warehouse_id",reqVo.getWarehouseId());
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getId()),"id",reqVo.getId());
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getMaterialName()),"material_name",reqVo.getMaterialName());
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getType()),"type",reqVo.getType());
        queryWrapper.eq("del_flag",0);
        reqVo.page();
        List<AttWarehouseAdd> list = attWarehouseAddService.list(queryWrapper);
        for (AttWarehouseAdd attWarehouseAdd : list) {
            if (StringUtils.isNotBlank(attWarehouseAdd.getFileIds())){
                attWarehouseAdd.setFileList(getFileList(attWarehouseAdd.getFileIds()));
            }
        }
        return CommonResult.success(PageUtils.getPageVO(list));
    }


    private List getFileList(String fileIds){

        CommonResult load = umsClient.load(fileIds);
        if (CommonResult.success().getCode() == load.getCode()){
            List  files = (List) load.getData();
            return files;
        }else {
            return Arrays.asList();
        }
    }


    @Override
    @PostMapping("/outList")
    public CommonResult warehouseOutList(AttWarehouseReqVo reqVo) {
        QueryWrapper<AttWarehouseOut> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getWarehouseId()),"warehouse_id",reqVo.getWarehouseId());
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getId()),"id",reqVo.getId());
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getMaterialName()),"material_name",reqVo.getMaterialName());
        queryWrapper.eq(StringUtils.isNotBlank(reqVo.getType()),"type",reqVo.getType());
        queryWrapper.eq("del_flag",0);
        reqVo.page();
        List<AttWarehouseOut> list = attWarehouseOutService.list(queryWrapper);

        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @Override
    @PostMapping("/warehouseOut")
    public CommonResult warehouseOut(AttWarehouseOut attWarehouseOut) {

        if (StringUtils.isBlank(attWarehouseOut.getWarehouseId())){
            return CommonResult.failed("未绑定仓库");
        }
        QueryWrapper<AttWarehouseAdd> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("warehouse_id",attWarehouseOut.getWarehouseId());
        queryWrapper.eq("material_name",attWarehouseOut.getMaterialName());

        if (ObjectUtils.isNull(attWarehouseOut.getUnivalent(),attWarehouseOut.getNum())){
            return CommonResult.failed("物资未进行过入库");
        }
        //设置总价
        attWarehouseOut.setAmount(attWarehouseOut.getUnivalent().multiply(BigDecimal.valueOf(attWarehouseOut.getNum())));
        attWarehouseOut.setId(IdWorker.get32UUID());
        if (attWarehouseOutService.save(attWarehouseOut)){
            //统一更新仓库对应资源数量
            attWarehouseService.outToDevice(attWarehouseOut);

            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }

    @Override
    @GetMapping("/warehouseStaticsList")
    public CommonResult warehouseStaticsList(AttWarehouseReqVo reqVo) {
        String warehouseId = reqVo.getWarehouseId();
        if (StringUtils.isBlank(warehouseId)){
            return CommonResult.failed("未选择仓库");
        }
        List<JSONObject> result = new ArrayList<>();

        List<WarehouseMaterialResVo> addResVos = attWarehouseAddService.staticsAddList(warehouseId);
        List<WarehouseMaterialResVo> outResVos = attWarehouseOutService.staticsOutList(warehouseId);

        Map<String, List<WarehouseMaterialResVo>> addMaterialMap = addResVos.stream().collect(Collectors.groupingBy(WarehouseMaterialResVo::getMaterialName));
        Map<String, List<WarehouseMaterialResVo>> outMaterialMap = outResVos.stream().collect(Collectors.groupingBy(WarehouseMaterialResVo::getMaterialName));
        Set<String> materialSet = addMaterialMap.keySet();

        //取出物资名称，根据物资名称和单价做统计
        for (String material : materialSet) {
            List<WarehouseMaterialResVo> addVos = addMaterialMap.get(material);
            List<WarehouseMaterialResVo> outVos = outMaterialMap.get(material);
            JSONObject json = new JSONObject();
            BigDecimal addAmount = BigDecimal.ZERO;
            BigDecimal outAmount = BigDecimal.ZERO;
            Integer outSum = 0;
            addAmount = addVos.stream().map(WarehouseMaterialResVo::getAmount).reduce(addAmount, BigDecimal::add);
            Integer addSum = addVos.stream().mapToInt(WarehouseMaterialResVo::getNum).sum();
            if (CollectionUtils.isNotBlank(outVos)){
                outVos.forEach( o -> {
                    BigDecimal out = o.getUnivalent().multiply(BigDecimal.valueOf(o.getNum()));
                    o.setAmount(out);
                });
                outAmount = outVos.stream().map(WarehouseMaterialResVo::getAmount).reduce(outAmount, BigDecimal::add);
                outSum = outVos.stream().mapToInt(WarehouseMaterialResVo::getNum).sum();
            }
            json.put("num", addSum - outSum);
            json.put("amount",addAmount.subtract(outAmount));
            json.put("name",material);
            json.put("standard",addVos.get(0).getStandard());
            result.add(json);
        }
        if (reqVo.isPaged()){
            reqVo.limitPage(result);
        }
        return CommonResult.success(PageUtils.getPageVO(result));
    }



    @Override
    @GetMapping("/warehouseMaterialStandardList")
    public CommonResult warehouseMaterialStandardList() {
        return CommonResult.success(attWarehouseService.warehouseMaterialStandardList());
    }



}
