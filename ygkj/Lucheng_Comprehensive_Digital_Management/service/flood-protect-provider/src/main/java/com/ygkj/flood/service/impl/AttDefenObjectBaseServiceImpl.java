package com.ygkj.flood.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ygkj.flood.mapper.AttDefenObjectBaseMapper;
import com.ygkj.flood.model.AttDefenObjectBase;
import com.ygkj.flood.service.AttDefenObjectBaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lxl
 * @create 2022-05-17 16:32
 * @description
 */
@Service
public class AttDefenObjectBaseServiceImpl extends ServiceImpl<AttDefenObjectBaseMapper, AttDefenObjectBase> implements AttDefenObjectBaseService {



    @Override
    public List<JSONObject> defenObjectTree() {

        List<AttDefenObjectBase> list = baseMapper.defenObjectTree();
        List<JSONObject> result = new ArrayList<>();

        Map<String, List<AttDefenObjectBase>> townMap = list.stream()
                .collect(Collectors.groupingBy(AttDefenObjectBase::getTown));

        Set<String> townSet = townMap.keySet();
        for (String town : townSet) {
            List<AttDefenObjectBase> townList = townMap.get(town);
            Map<String, List<AttDefenObjectBase>> naturalMap = townList.stream()
                    .collect(Collectors.groupingBy(AttDefenObjectBase::getNaturalAdnm));
            Set<String> naturalSet = naturalMap.keySet();
            JSONObject json = new JSONObject();
            json.put("name",town);
            List<JSONObject> naturalList = new ArrayList<>();
            for (String natural : naturalSet) {
                JSONObject nJson = new JSONObject();
                List<JSONObject> peopleList = new ArrayList<>();
                nJson.put("name",natural);
                List<AttDefenObjectBase> peoples = naturalMap.get(natural);
                for (AttDefenObjectBase people : peoples) {
                    JSONObject pJson = new JSONObject();
                    pJson.put("id", "defen" + people.getId());
                    pJson.put("name",people.getDefenObject());
                    pJson.put("phone",people.getContactTel());
                    peopleList.add(pJson);
                }
                nJson.put("list",peopleList);
                naturalList.add(nJson);
            }
            json.put("list",naturalList);
            result.add(json);
        }
        return result;
    }
}
