package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.CatalogResVo;
import com.ygkj.digitization.vo.response.RecordListResVo;
import com.ygkj.digitization.vo.response.RecordResVo;
import com.ygkj.entity.PageEntity;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface RecordService {

    /**
     * 获取档案目录树
     *
     * @return
     */
    CatalogResVo listCatalog();

    PageVO catalogs(CatalogPageQueryVo queryVo);

    /**
     * 编辑档案目录
     *
     * @param catalogReqVo
     */
    void editCatalog(CatalogReqVo catalogReqVo);

    /**
     * 逻辑删除档案目录
     *
     * @param id
     */
    void delCatalog(String id);

    /**
     * 编辑档案
     *
     * @param reqVo
     */
    void editRecord(RecordReqVo reqVo);

    /**
     * 删除档案
     *
     * @param id
     */
    void delRecord(String id);

    /**
     * 档案列表
     *
     * @param reqVo
     * @return
     */
    PageVO<RecordListResVo> listRecord(RecordListReqVo reqVo);

    /**
     * 查询档案详情
     *
     * @param id
     * @return
     */
    RecordResVo selectRecord(String id);

    Boolean collect(String id);

    Boolean unCollect(String objectId);

    PageVO myCollection(CollectionPageQueryVo queryVo);
}
