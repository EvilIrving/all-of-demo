package com.ygkj.big.screen.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.big.screen.mapper.AvoidancePointMapper;
import com.ygkj.big.screen.service.AvoidancePointService;
import com.ygkj.big.screen.vo.request.AvoidancePointReqVo;
import com.ygkj.big.screen.vo.response.AvoidancePointRespVo;
import com.ygkj.utils.PageUtils;
import com.ygkj.water.model.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pqf
 * @DESCRIPTION:
 * @create 2021-10-20 17:49
 */
@Service
public class AvoidancePointServiceImpl implements AvoidancePointService {
    @Autowired
    private AvoidancePointMapper avoidancePointMapper;

    @Override
    public CommonResult pageList(AvoidancePointReqVo reqVo) {
        if (reqVo.getPageNum() != null && reqVo.getPageNum() > 0
                && reqVo.getPageSize() != null && reqVo.getPageSize() > 0)
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        List<AvoidancePointRespVo> list=avoidancePointMapper.pageList(reqVo);
        if(reqVo.getId()!=null){
            List<String> strlist=new ArrayList<>();
            String str=list.get(0).getPointPhoto();
            if(str!=null){
                String[] strs=str.split(",");
                for (int i = 0; i <=strs.length -1 ; i++) {
                    strlist.add(strs[i]);
                }
                list.get(0).setPointPhotos(strlist);
            }
        }
        return CommonResult.success(PageUtils.getPageVO(list));
    }

    @Override
    public CommonResult load(AvoidancePointReqVo reqVo) {
        return CommonResult.success(PageUtils.getPageVO(avoidancePointMapper.load(reqVo)));
    }

    @Override
    public CommonResult totalList() {
        AvoidancePointReqVo reqVo=new AvoidancePointReqVo();
        List<AvoidancePointRespVo> list =avoidancePointMapper.pageList(reqVo);
        Map<String,Object> map=new HashMap<>();
        if(CollectionUtils.isEmpty(list)){
            map.put("sumCapacity",0);
            map.put("minPointId",0);
            map.put("minPoint","0");
            map.put("minCapacity",0);
            map.put("maxPointId",0);
            map.put("maxPoint","0");
            map.put("maxCapacity",0);
        }else {
            int sumCapacity=0;
            for(AvoidancePointRespVo vo:list){
                sumCapacity+=vo.getPointCapacity();
            }
            map.put("sumCapacity",sumCapacity);
            List<AvoidancePointRespVo> list1=list.stream().sorted(Comparator.comparing(AvoidancePointRespVo::getPointCapacity)).collect(Collectors.toList());
            List<AvoidancePointRespVo> list2=list.stream().sorted(Comparator.comparing(AvoidancePointRespVo::getPointCapacity).reversed()).collect(Collectors.toList());
            map.put("minPointId",list1.get(0).getId());
            map.put("minPoint",list1.get(0).getPointName());
            map.put("minCapacity",list1.get(0).getPointCapacity());
            map.put("maxPointId",list2.get(0).getId());
            map.put("maxPoint",list2.get(0).getPointName());
            map.put("maxCapacity",list2.get(0).getPointCapacity());
        }
        return CommonResult.success(map);
    }

    @Override
    public int saveOrUpdate(AvoidancePointReqVo reqVo) {
        int count=0;
        if(reqVo.getId()==null){
            count=avoidancePointMapper.insert(reqVo);
        }else{
            count=avoidancePointMapper.update(reqVo);
        }
        return count;
    }

    @Override
    public int delete(AvoidancePointReqVo reqVo) {
        return avoidancePointMapper.delete(reqVo);
    }


}
