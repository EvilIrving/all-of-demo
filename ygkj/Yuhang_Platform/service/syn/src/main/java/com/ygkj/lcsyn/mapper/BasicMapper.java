package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.lcsyn.model.St;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("basic")
public interface BasicMapper {

    @Select("SELECT stcd,sttp FROM dts_yh_basic_data_db_st_stbprp_b")
    List<St> listSt();

    @Insert("${sql}")
    void batchInsert(String sql);

}
