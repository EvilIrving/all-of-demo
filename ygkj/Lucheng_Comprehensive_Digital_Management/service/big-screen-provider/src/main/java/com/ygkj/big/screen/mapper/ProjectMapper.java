package com.ygkj.big.screen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.AttResBase;
import com.ygkj.big.screen.model.AttWagaBase;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/24
 */
@DS("basic")
public interface ProjectMapper {
    /**
     * 根据水库编码查询水库信息
     *
     * @param resCodes 水库编码，多个水库编码用半角逗号隔开
     * @return
     */
    @Select("select * from att_res_base where FIND_IN_SET(res_code,#{resCodes})")
    @ResultType(AttResBase.class)
    List<AttResBase> selectRsvrByCode(String resCodes);

    /**
     * 查询所有的水库信息
     *
     * @return
     */
    @Select("select * from att_res_base where del_flag=0")
    @ResultType(AttResBase.class)
    List<AttResBase> selectAllRsvr();

    /**
     * 根据水闸编码查询水闸信息
     *
     * @param wagaCodes 水闸编码，多个水闸编码用半角逗号隔开
     * @return
     */
    @Select("select * from att_waga_base where FIND_IN_SET(waga_code,#{wagaCodes})")
    @ResultType(AttWagaBase.class)
    List<AttWagaBase> selectWagaByCode(String wagaCodes);

    /**
     * 统计水库工程数量
     *
     * @return
     */
    @Select("select count(1) from att_res_base where del_flag=0")
    Integer countRsvrNum();

    /**
     * 统计水闸工程数量
     *
     * @return
     */
    @Select("select count(1) from att_waga_base where del_flag=0")
    Integer countWagaNum();

    /**
     * 查询19座大中型水库信息
     *
     * @return
     */
    @Select("select * from att_res_base where del_flag=0 and eng_scal<4")
    @ResultType(AttResBase.class)
    List<AttResBase> selectLargeMidRsvrByCode();

    @Select("select res_code,res_name from att_res_base where del_flag=0 and find_in_set(res_code,#{resCodes})")
    @ResultType(AttResBase.class)
    List<AttResBase> selectCodeNameOfRsvrByCodes(String resCodes);
}
