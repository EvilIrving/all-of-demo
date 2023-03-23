package com.ygkj.flood.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.AttPustBase;
import com.ygkj.flood.model.AttResBase;
import com.ygkj.flood.model.AttWagaBase;
import com.ygkj.flood.vo.request.ProjectListReqVo;
import com.ygkj.flood.vo.response.PustListResVo;
import com.ygkj.flood.vo.response.StorageResVo;
import com.ygkj.flood.vo.response.WagaListResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FloodProjectMapper {

    /**
     * 水库的一些基本信息
     *
     * @return
     */
    List<StorageResVo> listRes(Integer resScal);

    /**
     * 水库最新水位
     *
     * @param stCodeSet
     * @param tableName
     * @return
     */
    @DS("business")
    List<StorageResVo> listLatestRsvr(@Param("stCodeSet") Set<String> stCodeSet, @Param("tableName") String tableName);

    /**
     * 水闸列表
     *
     * @param reqVo
     * @return
     */
    List<WagaListResVo> listWaga(ProjectListReqVo reqVo);

    /**
     * 泵站列表
     *
     * @param reqVo
     * @return
     */
    List<PustListResVo> listPust(ProjectListReqVo reqVo);

    /**
     * 水闸最新水位
     *
     * @param tableName
     * @param set
     * @return
     */
    @DS("business")
    List<WagaListResVo> listLatestWas(@Param("tableName") String tableName, @Param("set") Set<String> set);

    /**
     * 列出泵站工程列表
     *
     * @return
     */
    List<AttPustBase> listPustProj(String pustCode);

    /**
     * 列出水闸工程列表
     *
     * @return
     */
    List<AttWagaBase> listWagaProj();

    /**
     * 列出水库工程列表
     *
     * @return
     */
    List<AttResBase> listRsvrProj();


    /**
     * 根据水库编码查询对应的洪水预报id
     *
     * @param resCodes 水库编码，多个水库编码用逗号隔开
     * @return res_code, flood_id
     */
    @Select("select res_code,flood_id from att_res_base where FIND_IN_SET(res_code,#{resCodes})")
    List<Map<String, String>> listFloodIdByResCode(String resCodes);
}
