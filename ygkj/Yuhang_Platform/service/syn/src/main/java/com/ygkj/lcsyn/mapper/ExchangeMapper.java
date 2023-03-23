package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//@DS("basic")
@DS("exchange")
public interface ExchangeMapper {

//    @Select("SELECT TABLE_NAME FROM information_schema.`TABLES` WHERE TABLE_SCHEMA = 'yh_exchange_db' AND TABLE_NAME NOT IN ( 'undo_log','jw_question','jw_question_1','jw_question_2','jw_question_3','jw_task_situation')")
    @Select("SELECT TABLE_NAME FROM information_schema.`TABLES` WHERE TABLE_SCHEMA = 'yh_exchange_db' AND TABLE_NAME != 'undo_log'")
    List<String> listTableName();

    void addCol(String tableName);

    @Select("SELECT COLUMN_NAME FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = 'yh_exchange_db' AND TABLE_NAME = #{tableName} AND COLUMN_KEY = 'PRI'")
    String selectPri(String tableName);

    @Update("UPDATE ${tableName} SET dts_cmf_id = ${pri}")
    void updateId(@Param("tableName") String tableName,@Param("pri") String pri);

    @Update("UPDATE ${tableName} SET dts_cmf_id = UUID()")
    void initId(String tableName);

    @Delete("alter table ${tableName} drop primary key")
    void delPri(String tableName);

    @Update("ALTER TABLE ${tableName} ADD CONSTRAINT PK_dts_cmf_id PRIMARY KEY (dts_cmf_id)")
    void setPri(String tableName);

    @Update("RENAME TABLE ${tableName} TO dts_yh_basic_data_db_${tableName}")
    void renameTable(String tableName);

    @Update("ALTER TABLE ${tableName} MODIFY dts_cmf_id VARCHAR(64) FIRST")
    void firstDtsId(String tableName);
}
