package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.gragh.vo.request.AdcdQueryVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Huangzh
 * @description att_adcd_base
 * @date 2020-12-08
 */
public interface AttAdcdBaseMapper {

    @Select("SELECT adcd,adnm\n" +
            " FROM att_adcd_base\n" +
            " where adcd LIKE '3303%' AND level = 3 AND FIND_IN_SET(adnm,'市本级')=0\n" +
            " ORDER BY FIELD(CONCAT(SUBSTR( `adcd` from 1 for 6),'000000'),'330383000000','330327000000','330329000000','330326000000','330328000000','330324000000','330381000000','330382000000','330322000000','330304000000','330303000000','330302000000','330301000000') desc")
    List<AttAdcdBase> selectWenZhouCounty();

    @Select("SELECT adcd FROM att_adcd_base\n" +
            "where adcd LIKE '3303%' AND level = 3 AND adnm = #{areaName}")
    String getWenzhouAreaCodeByAdnm(String areaName);

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int insert(AttAdcdBase attAdcdBase);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int update(AttAdcdBase attAdcdBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    AttAdcdBase load(String adcd);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    List<AttAdcdBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int pageListCount(int offset, int pagesize);

    List<AttAdcdBase> selectAreaCode(AdcdQueryVo adcdQueryVo);

    List<AttAdcdBase> selectAdnmByAdcdList(@Param("adcdList") List<String> adcdList);

    List<AttAdcdBase> selectAllWzAdNotIncludeCommittee(@Param("icAdcdList") List<String> icAdcdList);

    @Select("select adcd from att_adcd_base where adcd LIKE '3303%' AND level = 3 AND adnm like concat('%',#{adnm},'%') limit 1")
    String selectWzAdcdByAdnm(String adnm);

}
