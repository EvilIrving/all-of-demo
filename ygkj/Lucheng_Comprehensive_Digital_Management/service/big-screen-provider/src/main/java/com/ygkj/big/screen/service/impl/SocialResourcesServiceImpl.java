package com.ygkj.big.screen.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ygkj.big.screen.mapper.SocialResourcesMapper;
import com.ygkj.big.screen.model.SeawallRange;
import com.ygkj.big.screen.model.SocialResources;
import com.ygkj.big.screen.service.SocialResourcesService;
import com.ygkj.big.screen.utils.InRegionUtil;
import com.ygkj.big.screen.vo.request.SocialResourcesReqVo;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SocialResourcesServiceImpl implements SocialResourcesService {
    @Autowired
    private SocialResourcesMapper socialResourcesMapper;

    @Override
    public List<JSONObject> countByType(SocialResourcesReqVo reqVo) {
        List<SocialResources> socialResources = socialResourcesMapper.selectList(new QueryWrapper<>());
        List<String> typeList = socialResources.stream().map(SocialResources::getType).distinct().collect(Collectors.toList());
        List<SocialResources> resources = filterPoint(socialResources, reqVo);
        Map<String, List<SocialResources>> map = null;
        List<JSONObject> result = new ArrayList<>();
        JSONObject object = new JSONObject();
        object.put("name","全部");
        object.put("num",resources.size());
        result.add(object);
        if (CollectionUtils.isNotBlank(resources)){
            map = resources.stream().collect(Collectors.groupingBy(SocialResources::getType));
        }
        for (String type : typeList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",type);
            if (map != null && map.containsKey(type)){
                jsonObject.put("num",map.get(type).size());
            }else {
                jsonObject.put("num",0);
            }
            result.add(jsonObject);
        }
        return result;
    }

    @Override
    public JSONObject list(SocialResourcesReqVo reqVo) {
        JSONObject result = new JSONObject();
        List<SocialResources> socialResources = socialResourcesMapper.selectList(new QueryWrapper<>());
        List<SocialResources> resources = filterPoint(socialResources, reqVo);
        if (CollectionUtils.isBlank(resources)){
            result.put("list",resources);
            result.put("highestPoint","-");
            result.put("lowestPoint","-");
        }else {
            String highestPoint = "-";
            Double higher = 0d;
            String lowestPoint = "-";
            Double lower = 0d;
            List<SocialResources> resources1 = new ArrayList<>();
            for (SocialResources resource : resources) {
                if (StringUtils.isNotBlank(reqVo.getType())){
                    if(!reqVo.getType().contains(resource.getType())){
                        continue;
                    }
                }
                if (StringUtils.isNotBlank(reqVo.getKeyWord())){
                    if(!resource.getName().contains(reqVo.getKeyWord())){
                        continue;
                    }
                }
                if (resource.getElevation() != null){
                    if (higher == 0){
                        higher = resource.getElevation();
                        lower = resource.getElevation();
                        highestPoint = resource.getName();
                        lowestPoint = resource.getName();
                    } else {
                        if (higher < resource.getElevation()){
                            higher = resource.getElevation();
                            highestPoint = resource.getName();
                        }
                        if (lower > resource.getElevation()){
                            lower = resource.getElevation();
                            lowestPoint = resource.getName();
                        }
                    }
                }
                resources1.add(resource);
            }

            result.put("list",resources1);
            result.put("highestPoint",highestPoint);
            result.put("lowestPoint",lowestPoint);
        }
        return result;
    }

    private List<SocialResources> filterPoint(List<SocialResources> socialResources,SocialResourcesReqVo reqVo){
        List<SocialResources> resources = new ArrayList<>();
        SeawallRange seawallRange = socialResourcesMapper.querySeawallRange(reqVo);
        if (seawallRange != null && seawallRange.getPoint() != null){
            List<Double[]> doubles = JSONArray.parseArray(seawallRange.getPoint(), Double[].class);
            for (SocialResources socialResource : socialResources) {
                boolean isInRegion = InRegionUtil.IsInRegion(socialResource.getLongitude(), socialResource.getLatitude(), doubles);
                if (isInRegion){
                    resources.add(socialResource);
                }
            }
        }
        return resources;
    }
}
