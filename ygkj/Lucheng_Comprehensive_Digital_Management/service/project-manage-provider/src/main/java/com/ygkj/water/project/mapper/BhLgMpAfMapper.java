package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhLgMpAf;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-28 9:35
 * @description
 */
public interface BhLgMpAfMapper extends BaseMapper<BhLgMpAf> {

    /**
     * 根据id逻辑删除
     * @param id
     * @return
     */
    @Update("update bh_lg_mp_af set del_flag = 1 where id = #{id}")
    Integer deleteByAfId(@Param("id") String id);
}
