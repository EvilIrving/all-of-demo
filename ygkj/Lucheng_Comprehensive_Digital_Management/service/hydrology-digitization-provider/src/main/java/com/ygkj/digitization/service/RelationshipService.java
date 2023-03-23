package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.*;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface RelationshipService {

    /**
     * 编辑关联关系
     *
     * @param reqVo
     */
    void edit(RelBaseReqVo reqVo);

    /**
     * 删除关联关系
     *
     * @param idList
     */
    void del(List<String> idList);

    /**
     * 关联关系列表
     *
     * @param reqVo
     * @return
     */
    PageVO<RelBaseResVo> list(RelListReqVo reqVo);

    /**
     * 关联关系详细信息
     *
     * @param reqVo
     * @return
     */
    List<RelDetailResVo> select(RelDetailReqVo reqVo);

    /**
     * 水库或海塘列表
     *
     * @param reqVo
     * @return
     */
    List<? extends ProjectResVo> listProject(ProjectListReqVo reqVo);

    /**
     * 测站列表
     *
     * @param reqVo
     * @return
     */
    List<StResVo> listSt(StListReqVo reqVo);

    /**
     * 编辑潮位关联关系
     *
     * @param reqVo
     */
    void editTide(RelSeawallReqVo reqVo);

    /**
     * 删除潮位关联关系
     *
     * @param stCodes
     */
    void delTide(String stCodes);

    /**
     * 关联潮位列表
     *
     * @param reqVo
     * @return
     */
    PageVO<RelSeawallListResVo> listTide(RelListReqVo reqVo);

    /**
     * 关联潮位详细信息列表
     *
     * @param stCode
     * @return
     */
    List<RelSeawallDetailResVo> selectTide(String stCode);
}
