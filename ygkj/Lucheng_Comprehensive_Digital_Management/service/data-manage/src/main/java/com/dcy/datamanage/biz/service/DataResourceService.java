package com.dcy.datamanage.biz.service;

import com.dcy.datamanage.api.model.BusDataResource;
import com.dcy.datamanage.biz.dto.input.BusDataResourceSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataResourceOutputDTO;
import com.dcy.datamanage.biz.mapper.DataResourceMapper;
import com.dcy.datamanage.biz.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class DataResourceService {

    @Autowired
    private DataResourceMapper dataResourceMapper;

    public Boolean save(BusDataResource busDataResource) {
        busDataResource.setId(SnowFlake.zero.nextId());
        dataResourceMapper.insert(busDataResource);
        return true;
    }

    public Boolean delById(String id) {
        dataResourceMapper.deleteById(id);
        return true;
    }

    public Boolean update(BusDataResource busDataResource) {
        dataResourceMapper.updateById(busDataResource);
        return true;
    }

    public List<BusDataResourceOutputDTO> list(BusDataResourceSearchInputDTO dto) {
        List<BusDataResourceOutputDTO> result = null;
        String parentId = dto.getParentId();
        List<BusDataResourceOutputDTO> allList = dataResourceMapper.list(dto);
        if (dto.isTree()) {
            if (!CollectionUtils.isEmpty(allList)) {
                Map<String, BusDataResourceOutputDTO> map = new HashMap<>(allList.size());
                //找到主层
                result = new ArrayList<>();
                for (BusDataResourceOutputDTO resource : allList) {
                    if (Objects.equals(parentId, resource.getParentId())) {
                        result.add(resource);
                    }
                    map.put(resource.getId(), resource);
                }
                //并到父节点上
                for (BusDataResourceOutputDTO resource : allList) {
                    String pId = resource.getParentId();
                    if (map.containsKey(pId)) {
                        BusDataResourceOutputDTO parent = map.get(pId);
                        List<BusDataResourceOutputDTO> subList = parent.getSubList();
                        if (subList == null) {
                            subList = new ArrayList<>();
                            parent.setSubList(subList);
                        }
                        subList.add(resource);
                    }
                }
            }
        }
        return result;
    }
}
