package com.ygkj.digitization.mapper;

import com.ygkj.digitization.vo.request.CatalogPageQueryVo;
import com.ygkj.digitization.vo.request.CatalogReqVo;
import com.ygkj.digitization.vo.request.RecordListReqVo;
import com.ygkj.digitization.vo.request.RecordReqVo;
import com.ygkj.digitization.vo.response.CatalogResVo;
import com.ygkj.digitization.vo.response.RecordListResVo;
import com.ygkj.digitization.vo.response.RecordResVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface RecordMapper {

    /**
     * 获取所有目录
     *
     * @return
     */
    @Select("SELECT * FROM att_record_catalog_base WHERE del_flag = 0 ORDER BY sort")
    List<CatalogResVo> listCatalog();

    /**
     * 查询某个目录下子目录的数量
     *
     * @param parentId
     * @return
     */
    @Select("SELECT COUNT(*) FROM att_record_catalog_base WHERE parent_id = #{parentId}")
    int countCatalogByPid(String parentId);

    /**
     * 保存目录
     *
     * @param catalogReqVo
     */
    void saveCatalog(CatalogReqVo catalogReqVo);

    /**
     * 修改目录名称
     *
     * @param catalogReqVo
     */
    @Update("UPDATE att_record_catalog_base SET `name` = #{name} WHERE id = #{id}")
    void updateCatalog(CatalogReqVo catalogReqVo);

    /**
     * 逻辑删除目录
     *
     * @param id
     */
    @Update("UPDATE att_record_catalog_base SET del_flag = 1 WHERE id = #{id}")
    void delCatalog(String id);

    /**
     * 保存档案
     *
     * @param reqVo
     */
    void saveRecord(RecordReqVo reqVo);

    /**
     * 修改档案
     *
     * @param reqVo
     */
    void updateRecord(RecordReqVo reqVo);

    /**
     * 逻辑删除id
     *
     * @param id
     */
    @Update("UPDATE att_record_base SET del_flag = 1 WHERE id = #{id}")
    void delRecord(String id);

    /**
     * 查询档案详情
     *
     * @param id
     * @return
     */
    RecordResVo selectRecord(String id);

    /**
     * 统计每个目录下的文件数量
     *
     * @return
     */
    List<CatalogResVo> countFileByCatalog();

    /**
     * 获取文件列表
     *
     * @param reqVo
     * @return
     */
    List<RecordListResVo> listRecord(RecordListReqVo reqVo);

    List<CatalogResVo> selectCatalogsByCondition(CatalogPageQueryVo queryVo);
}
