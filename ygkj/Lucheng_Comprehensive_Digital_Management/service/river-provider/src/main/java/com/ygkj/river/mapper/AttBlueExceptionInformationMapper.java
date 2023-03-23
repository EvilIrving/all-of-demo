package com.ygkj.river.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.river.model.AttBlueExceptionInformation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2021-08-06 9:35
 * @description
 */
public interface AttBlueExceptionInformationMapper extends BaseMapper<AttBlueExceptionInformation> {

    @Update("update att_blue_exception_information set del_flag = 1 where id = #{id}")
    Integer deleteExceptionInformation(@Param("id") String exceptionInformationIds);

    @Select("select max(sort) from att_blue_exception_information where del_flag = 0 ")
    Integer selectMaxSort();

    /**
     * 查询 升序的下一个排序 （ 有存在在上面的 ）
     *
     * @param id
     * @return
     */
    @Select("select sort from att_blue_exception_information where sort > (select sort from att_blue_exception_information where id = #{id} )  order by sort  limit 1")
    Integer selectUpToSort(@Param("id") String id);

    /**
     * 查询  降序的下一个排序
     *
     * @param id
     * @return
     */
    @Select("select sort from att_blue_exception_information where sort < (select sort from att_blue_exception_information where id = #{id}) order by sort desc limit 1")
    Integer selectDownToSort(@Param("id") String id);

    /**
     * 查询对应的旧排序的记录 替换为新的排序
     *
     * @param oldSort
     * @param newSort
     * @return
     */
    @Select("UPDATE att_blue_exception_information \n" +
            "SET sort = #{newSort} \n" +
            "WHERE\n" +
            "\tid = ( SELECT id from ( select * FROM att_blue_exception_information WHERE sort = #{oldSort})a )")
    Integer updateOldSort(@Param("oldSort") Integer oldSort, @Param("newSort") Integer newSort);
}
