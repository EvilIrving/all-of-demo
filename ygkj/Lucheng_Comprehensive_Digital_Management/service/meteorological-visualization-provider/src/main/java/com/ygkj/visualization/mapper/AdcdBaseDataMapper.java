package com.ygkj.visualization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.gragh.model.AttAdcdBase;
import com.ygkj.visualization.model.AntiTyphoonInfo;
import com.ygkj.visualization.model.GdpInfo;
import com.ygkj.visualization.model.PopulationInfo;
import com.ygkj.visualization.vo.response.AdcdStatisticsInfo;
import com.ygkj.visualization.vo.response.AreaGdpStatistics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AdcdBaseDataMapper {

    List<AdcdStatisticsInfo> getAdcds(String adnm);

    @DS("business")
    List<AdcdStatisticsInfo > getPopInfos(String adnm);

    @DS("business")
    List<GdpInfo> getGdpInfos(String year);

    @DS("business")
    List<AntiTyphoonInfo> getAntiTyphoonInfos(String adnm);

    @DS("business")
    List<AreaGdpStatistics> getAreaGdps(String adnm);

    /**
     * 查询温州下辖各个区县的adcd，adnm
     * @return
     */
    @Select("SELECT adcd,adnm\n" +
            "FROM att_adcd_base\n" +
            "where del_flag=0 and parent_adcd='330300000000'")
    List<Map<String,Object>> selectWzChildAd();
    @Select("SELECT adcd,adnm\n" +
            "FROM att_adcd_base\n" +
            "where adcd LIKE '3303%' AND level = 3 AND FIND_IN_SET(adnm,'市本级')=0 " +
            "ORDER BY FIELD(CONCAT(SUBSTR( `adcd` from 1 for 6),'000000'),'330383000000','330327000000','330329000000','330326000000','330328000000','330324000000','330381000000','330382000000','330322000000','330304000000','330303000000','330302000000') desc")
    List<AttAdcdBase> selectWenZhouCounty();

    @Select("SELECT adcd " +
            "FROM att_adcd_base " +
            "where adcd LIKE '3303%' AND level = 3 AND adnm = #{areaName}")
    String selectAdcdByAdnm(String areaName);

    @DS("business")
    List<GdpInfo> getGdpByYearRangeAndAdcd(@Param("startYear") String startYear,
                                           @Param("endYear")  String endYear,
                                           @Param("adcd") String adcd);

    @DS("business")
    List<PopulationInfo > getPopByYearRangeAndAdcd(@Param("startYear") String startYear,
                                                       @Param("endYear")  String endYear,
                                                       @Param("adcd") String adcd,
                                                       @Param("tag") Integer tag);

    @DS("business")
    List<GdpInfo> getGdpByYearRange(@Param("startYear") String startYear,
                                           @Param("endYear")  String endYear);

    @DS("business")
    List<PopulationInfo > getPopByYearRange(@Param("startYear") String startYear,
                                                   @Param("endYear")  String endYear,
                                                   @Param("tag") Integer tag);
}
