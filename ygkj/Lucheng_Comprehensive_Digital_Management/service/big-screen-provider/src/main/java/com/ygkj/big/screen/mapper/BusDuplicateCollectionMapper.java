package com.ygkj.big.screen.mapper;

import com.ygkj.big.screen.model.BusDuplicateCollection;
import com.ygkj.big.screen.vo.request.BusDuplicateCollectionReq;
import com.ygkj.big.screen.vo.response.BusDuplicateCollectionResp;

import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/16 11:44
 */
public interface BusDuplicateCollectionMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/06/16
     **/
    int insert(BusDuplicateCollection busDuplicateCollection);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/06/16
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/06/16
     **/
    int update(BusDuplicateCollection busDuplicateCollection);

    List<BusDuplicateCollectionResp> getList(BusDuplicateCollectionReq entity);
}
