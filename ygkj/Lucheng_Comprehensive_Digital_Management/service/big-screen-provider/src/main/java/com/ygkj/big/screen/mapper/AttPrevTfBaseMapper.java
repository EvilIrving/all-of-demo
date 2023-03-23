package com.ygkj.big.screen.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.big.screen.model.AttPrevTfBase;
import com.ygkj.big.screen.model.StPptnR;
import com.ygkj.big.screen.vo.request.PrevTfQueryVo;
import com.ygkj.big.screen.vo.response.MountainTorrentBaseInfoStats;
import com.ygkj.big.screen.vo.response.MountainTorrentDetailResVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @version 1.0
 * @date 2021/6/18 10:32
 */
public interface AttPrevTfBaseMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/06/18
     **/
    int insert(AttPrevTfBase attPrevTfBase);

    List<AttPrevTfBase> selectList(AttPrevTfBase entity);

    @DS("business")
    List<StPptnR> recentWaterLevelData(@Param("tableName") String tableName, @Param("stcds") List<String> stcds, @Param("endTime") String endTime);

    AttPrevTfBase flashFloodDetails(String nvi, String avi);

    @DS("business")
    List<StPptnR> selectHourRainfall(@Param("list") List<String> list, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("stationCode") String stationCode);

    List<MountainTorrentDetailResVo> selectByCondition(PrevTfQueryVo queryVo);

    @Select("SELECT \n" +
            "(select count(DISTINCT COUNTY) from att_prev_tf_base where COUNTY is not null and COUNTY !='') AS involveCounty,\n" +
            "(select count(DISTINCT TOWN) from att_prev_tf_base ) AS prevTown,\n" +
            "(select count(DISTINCT NVI) from att_prev_tf_base where rec_type='一般村落' or rec_type='重要村落' ) AS prevVillage,\n" +
            "(select SUM(tpo) from att_prev_tf_base) AS effectPop,\n" +
            "(select count(1) from att_prev_tf_base where rec_type='防治区域') AS prevAreaNum,\n" +
            "(select count(1) from att_prev_tf_base where rec_type='防治区域' and lvl=1) AS dangerArea,\n" +
            "(select count(1) from att_prev_tf_base where rec_type='防治区域' and lvl=2) AS highDangerArea,\n" +
            "(select count(1) from att_prev_tf_base where rec_type='防治区域' and lvl=3) AS superDangerArea")
    @ResultType(MountainTorrentBaseInfoStats.class)
    MountainTorrentBaseInfoStats baseInfoStats();

    @DS("business")
    @Insert("replace into mountain_flood_predict(`tm`,`predict_info`) values (#{tm},#{predictInfo})")
    int insertMountainTorrentPredict(@Param("tm") Date tm, @Param("predictInfo") String predictInfo);
}
