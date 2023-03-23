package com.ygkj.big.screen.service.impl;

import com.ygkj.big.screen.service.BusDuplicateCollectionService;
import com.ygkj.big.screen.mapper.BusDuplicateCollectionMapper;
import com.ygkj.big.screen.model.BusDuplicateCollection;
import com.ygkj.big.screen.vo.request.BusDuplicateCollectionReq;
import com.ygkj.big.screen.vo.response.BusDuplicateCollectionResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 13:30
 */
@Service
public class BusDuplicateCollectionServiceImpl implements BusDuplicateCollectionService {


    @Autowired
    private BusDuplicateCollectionMapper busDuplicateCollectionMapper;


    @Override
    public void insert(BusDuplicateCollection busDuplicateCollection) {
        busDuplicateCollectionMapper.insert(busDuplicateCollection);
    }

    @Override
    public void delete(String id) {
        busDuplicateCollectionMapper.delete(id);
    }

    @Override
    public void update(BusDuplicateCollection busDuplicateCollection) {
        busDuplicateCollectionMapper.update(busDuplicateCollection);
    }

    @Override
    public List<BusDuplicateCollectionResp> getList(BusDuplicateCollectionReq entity) {
        return busDuplicateCollectionMapper.getList(entity);
    }
}
