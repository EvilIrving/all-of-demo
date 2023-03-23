package com.ygkj.flood.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ygkj.flood.ResponsibleControllerApi;
import com.ygkj.flood.feign.ProjectManageClient;
import com.ygkj.flood.model.AttBoatBase;
import com.ygkj.flood.model.AttGrowGrainBase;
import com.ygkj.flood.service.AttBoatBaseService;
import com.ygkj.flood.service.AttDefenObjectBaseService;
import com.ygkj.flood.service.AttGrowGrainService;
import com.ygkj.flood.service.TorrentialFloodService;
import com.ygkj.flood.vo.request.PrevTfInfoReqVo;
import com.ygkj.flood.vo.request.ResponsibleReqVo;
import com.ygkj.flood.vo.response.PvPchargeResVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-05-09 10:00
 * @description
 */
@RestController
@RequestMapping("/responsible")
public class ResponsibleController implements ResponsibleControllerApi {

    @Autowired
    private AttBoatBaseService attBoatBaseService;

    @Autowired
    private AttGrowGrainService attGrowGrainService;

    @Autowired
    private ProjectManageClient projectManageClient;

    @Autowired
    TorrentialFloodService torrentialFloodService;

    @Autowired
    private AttDefenObjectBaseService attDefenObjectBaseService;

    private static final String SPLIT = "-";

    @Override
    @PostMapping("/boatList")
    public CommonResult boatList(ResponsibleReqVo boatBase) {
        boatBase.page();
        QueryWrapper<AttBoatBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag",0);
        queryWrapper.eq(StringUtils.isNotBlank(boatBase.getBoatOwner()),"boat_owner",boatBase.getBoatOwner());
        queryWrapper.eq(StringUtils.isNotBlank(boatBase.getId()),"id",boatBase.getId());
        queryWrapper.and(StringUtils.isNotBlank(boatBase.getKey()),
                q -> q.like(StringUtils.isNotBlank(boatBase.getKey()),"boat_name",boatBase.getKey())
                        .or().like(StringUtils.isNotBlank(boatBase.getKey()),"boat_owner",boatBase.getKey()));
        queryWrapper.orderByDesc("create_time");
        List<AttBoatBase> list = attBoatBaseService.list(queryWrapper);

        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @Override
    @PostMapping("/boatSaveOrUpdate")
    public CommonResult boatSaveOrUpdate(ResponsibleReqVo boatBase) {

        AttBoatBase base = new AttBoatBase();
        BeanUtil.copyProperties(boatBase,base);
        if (StringUtils.isNotBlank(base.getId())){
            if (attBoatBaseService.updateById(base)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }else {
            base.setId(IdWorker.get32UUID());
            if (attBoatBaseService.save(base)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }
    }

    @Override
    @GetMapping("/boatDelete")
    public CommonResult boatDelete(String id) {
        AttBoatBase base = new AttBoatBase();
        base.setId(id);
        base.setDelFlag(1);
        if (attBoatBaseService.updateById(base)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }

    @Override
    @PostMapping("/growGrainList")
    public CommonResult growGrainList(ResponsibleReqVo base) {

        base.page();
        QueryWrapper<AttGrowGrainBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag",0);
        queryWrapper.eq(StringUtils.isNotBlank(base.getName()),"name",base.getName());
        queryWrapper.eq(StringUtils.isNotBlank(base.getId()),"id",base.getId());
        queryWrapper.and(StringUtils.isNotBlank(base.getKey()),
                q -> q.like(StringUtils.isNotBlank(base.getKey()),"name",base.getKey())
                        .or().like(StringUtils.isNotBlank(base.getKey()),"city",base.getKey())
                        .or().like(StringUtils.isNotBlank(base.getKey()),"village",base.getKey()));
        queryWrapper.orderByDesc("create_time");
        List<AttGrowGrainBase> list = attGrowGrainService.list(queryWrapper);
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @Override
    @PostMapping("/growGrainSaveOrUpdate")
    public CommonResult growGrainSaveOrUpdate(ResponsibleReqVo base) {

        AttGrowGrainBase growGrainBase = new AttGrowGrainBase();
        BeanUtil.copyProperties(base,growGrainBase);
        if (StringUtils.isNotBlank(base.getId())){
            if (attGrowGrainService.updateById(growGrainBase)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }else {
            if (attGrowGrainService.save(growGrainBase)){
                return CommonResult.success("操作成功");
            }else {
                return CommonResult.failed("操作失败");
            }
        }
    }

    @Override
    @GetMapping("/growGrainDelete")
    public CommonResult growGrainDelete(String id) {
        AttGrowGrainBase base = new AttGrowGrainBase();
        base.setId(id);
        base.setDelFlag(1);
        if (attGrowGrainService.updateById(base)){
            return CommonResult.success("操作成功");
        }else {
            return CommonResult.failed("操作失败");
        }
    }


    @Override
    @GetMapping("/responsibleListByType")
    public CommonResult responsibleListByType(String type, String key) {
        List<JSONObject> result = new ArrayList<>();

        List<JSONObject> boatList = new ArrayList<>();
        QueryWrapper<AttBoatBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag",0);
        queryWrapper.eq(StringUtils.isNotBlank(key),"boat_owner",key);
        queryWrapper.orderByDesc("create_time");
        List<AttBoatBase> list = attBoatBaseService.list(queryWrapper);
        list = list.stream().filter( a-> a.getPhone() != null).collect(Collectors.toList());
        list.forEach( e -> {
            JSONObject json = new JSONObject();
            json.put("name",e.getBoatOwner() + SPLIT + e.getBoatName());
            json.put("phone",e.getPhone());
            json.put("id","boat" + e.getId());
            boatList.add(json);
        });
        JSONObject boatJson = new JSONObject();
        boatJson.put("name","渔船");
        boatJson.put("list",boatList);
        result.add(boatJson);

        List<JSONObject> farmerList = new ArrayList<>();
        QueryWrapper<AttGrowGrainBase> growQueryWrapper = new QueryWrapper<>();
        growQueryWrapper.eq("del_flag",0);
        growQueryWrapper.eq(StringUtils.isNotBlank(key),"name",key);
        growQueryWrapper.orderByDesc("create_time");
        List<AttGrowGrainBase> growGrainBases = attGrowGrainService.list(growQueryWrapper);
        growGrainBases.forEach( g -> {
            JSONObject json = new JSONObject();
            json.put("name",g.getName() + SPLIT + g.getCity() + SPLIT + g.getVillage());
            json.put("phone",g.getPhone());
            json.put("id", "farmer" + g.getId());
            farmerList.add(json);
        });
        JSONObject farmerJson = new JSONObject();
        farmerJson.put("name","种粮大户");
        farmerJson.put("list",farmerList);
        result.add(farmerJson);

        List<JSONObject> sljList = projectManageClient.addressBookList(key);
        JSONObject sljJson = new JSONObject();
        sljJson.put("name","区农业农村局");
        sljJson.put("list",sljList);
        result.add(sljJson);

        List<JSONObject> torrentList = new ArrayList<>();
        PrevTfInfoReqVo reqVo = new PrevTfInfoReqVo();
        PageVO<PvPchargeResVo> resVoPageVO = torrentialFloodService.pvPchargeList(reqVo);
        List<PvPchargeResVo> pchargeResVos = resVoPageVO.getList();
        Map<String, List<PvPchargeResVo>> typeMap = pchargeResVos.stream().collect(Collectors.groupingBy(p -> p.getChargeType()));
        Set<String> typeSet = typeMap.keySet();
        for (String chargeType : typeSet) {
            List<JSONObject> typeChildList = new ArrayList<>();
            List<PvPchargeResVo> pvPchargeResVos = typeMap.get(chargeType);
            pvPchargeResVos.forEach( p -> {
                JSONObject json = new JSONObject();
                json.put("name",p.getPchargeName() + SPLIT + p.getNaturalAdnm() + SPLIT + p.getChargeType());
                json.put("phone",p.getMobile());
                json.put("id","torrent" + p.getPchargeCode());
                typeChildList.add(json);
            });
            JSONObject typeJson = new JSONObject();
            typeJson.put("name",chargeType);
            typeJson.put("list",typeChildList);
            torrentList.add(typeJson);
        }

        JSONObject torrentJson = new JSONObject();
        torrentJson.put("name","山洪灾害防御责任人");
        torrentJson.put("list",torrentList);
        result.add(torrentJson);

        List<JSONObject> threeList = projectManageClient.threeChargeTree(key);
        JSONObject threeJson = new JSONObject();
        threeJson.put("name","水利工程巡查责任人");
        threeJson.put("list",threeList);
        result.add(threeJson);

        List<JSONObject> defenObjectList = attDefenObjectBaseService.defenObjectTree();
        JSONObject defenJson = new JSONObject();
        defenJson.put("name","重要村落防御对象");
        defenJson.put("list",defenObjectList);
        result.add(defenJson);

        return CommonResult.success(result);
    }
}
