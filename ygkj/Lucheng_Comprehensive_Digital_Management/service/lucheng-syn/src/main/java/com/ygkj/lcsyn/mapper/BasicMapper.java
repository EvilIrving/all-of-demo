package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@DS("basic")
public interface BasicMapper {

    @Select("SELECT st_code FROM att_st_base WHERE wrp_waga = 1")
    List<String> listWrpSt();

    @Select("SELECT DISTINCT(tide_station_code) FROM att_seawall_base WHERE del_flag = 0")
    List<String> listSeawallTide();

    @Select("SELECT id FROM att_tide_fcst_base")
    Set<String> listFcstTide();

    void insertFcstTide(AttTideFcstBase attTideFcstBase);

    void batchInsertFcstTideVal(List<AttTideFcstValBase> fcstValBaseList);

    void batchInsertBmPrcdAccprcd(List<BmPrcdAccprcd> list);

    void batchInsertBmAccBuild(List<BmAccBuild> list);

    @Delete("DELETE FROM bm_prcd_accprcd")
    void delBmPrcdAccprcd();

    @Delete("DELETE FROM bm_acc_build")
    void delBmAccBuild();

    @Delete("DELETE FROM att_sixcharge_base")
    void delSixCharge();

    @Delete("${delSql}")
    void del(String delSql);

    @Insert("${insertSql}")
    void batchInsert(String insertSql);

    @Select("SELECT seawall_code seawallCode FROM att_seawall_base WHERE del_flag = 0")
    List<String> selectCode();

    @Delete("DELETE FROM bm_seawall_structinfo where prcd=#{prcd}")
    void delBmSeawallStructinfo(@Param("prcd") String prcd);

    void batchInsertBmSeawallStructinfo(List<BmSeawallStructinfo> list);

    void batchUpdateDikeInfo(@Param("item") AttDikeBase item);

    @Select("SELECT res_code FROM att_res_base")
    List<String> listAllResCode();

    void insertContract(BmPropertyContract base);

    @Select("select st_code from att_st_base where is_rain_station = 1 and del_flag = 0 ")
    List<String> listAllRainStationCode();

    Integer batchReplacePerennialRainfall(@Param("list") List<AttPerennialRainfall> list);

    Integer batchAttLastYearRainfall(@Param("list") List<AttLastYearRainfall> list);

    @Select("select * from att_sixcharge_base")
    List<AttSixchargeBase> listAllSixCharge();

    @Delete("DELETE FROM data_maintain_people_syn")
    void delDataMaintainPeopleSyn();

    void batchInsertDataMaintainPeopleSyn(@Param("list") List<DataMaintainPeopleSyn> dataMaintainPeopleSyns);
}
