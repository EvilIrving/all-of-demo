package com.ygkj.digitization.mapper;

import com.ygkj.digitization.model.RelRelPs;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelationshipMapper {

    /**
     * 新增关联的基础信息
     *
     * @param reqVo
     */
    void save(RelBaseReqVo reqVo);

    /**
     * 更新关联的基础信息
     *
     * @param reqVo
     */
    void update(RelBaseReqVo reqVo);

    /**
     * 删除旧的关联关系
     *
     * @param relId
     */
    void delRel(String relId);

    /**
     * 新增关联关系
     *
     * @param list
     */
    void saveRel(List<RelRelPs> list);

    /**
     * 删除关联关系
     *
     * @param list
     */
    void del(List<String> list);

    /**
     * 关联关系列表
     *
     * @param reqVo
     * @return
     */
    List<RelBaseResVo> list(RelListReqVo reqVo);

    /**
     * 水库名称
     *
     * @param list
     * @return
     */
    List<RelBaseResVo> selectResName(List<String> list);

    /**
     * 测站名称
     *
     * @param list
     * @return
     */
    List<RelBaseResVo> selectStName(List<String> list);

    /**
     * 旱情关联详细信息
     *
     * @param id
     * @return
     */
    List<RelDetailResVo> selectRes(String id);

    /**
     * 水位关联详细信息
     *
     * @param id
     * @return
     */
    List<RelDetailResVo> selectSt(String id);

    /**
     * 水库列表
     *
     * @param reqVo
     * @return
     */
    List<ResResVo> listRes(ProjectListReqVo reqVo);

    /**
     * 海塘列表
     *
     * @param reqVo
     * @return
     */
    List<SeawallResVo> listSeawall(ProjectListReqVo reqVo);

    /**
     * 测站列表
     *
     * @param reqVo
     * @return
     */
    List<StResVo> listSt(StListReqVo reqVo);

    /**
     * 清除之前和该测站关联的海塘的关联关系
     *
     * @param stCodes
     */
    void updateSeawallByStCode(String stCodes);

    /**
     * 保存测站和海塘的关联关系
     *
     * @param reqVo
     */
    void saveSeawallRel(RelSeawallReqVo reqVo);


    /**
     * 更新该测站的备注
     *
     * @param stCode
     * @param remark
     */
    void updateStRemark(@Param("stCode") String stCode, @Param("remark") String remark);

    /**
     * 有关联关系的潮位站
     *
     * @param reqVo
     * @return
     */
    List<RelSeawallListResVo> listTide(RelListReqVo reqVo);

    /**
     * 查询这些测站关联的海塘名称
     *
     * @param list
     * @return
     */
    List<RelSeawallListResVo> selectSeawallName(List<String> list);

    /**
     * 关联海塘详情
     *
     * @param stCode
     * @return
     */
    List<RelSeawallDetailResVo> selectTide(String stCode);
}
