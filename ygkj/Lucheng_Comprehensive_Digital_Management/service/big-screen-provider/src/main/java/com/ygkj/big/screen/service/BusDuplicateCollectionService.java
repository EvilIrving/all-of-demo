package com.ygkj.big.screen.service;

import com.ygkj.big.screen.model.BusDuplicateCollection;
import com.ygkj.big.screen.vo.request.BusDuplicateCollectionReq;
import com.ygkj.big.screen.vo.response.BusDuplicateCollectionResp;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 13:30
 */
public interface BusDuplicateCollectionService {
    /**
     * 新增
     */
    void insert(BusDuplicateCollection busDuplicateCollection);

    /**
     * 删除
     */
    void delete(String id);

    /**
     * 更新
     */
    void update(BusDuplicateCollection busDuplicateCollection);

    List<BusDuplicateCollectionResp> getList(BusDuplicateCollectionReq entity);
}
