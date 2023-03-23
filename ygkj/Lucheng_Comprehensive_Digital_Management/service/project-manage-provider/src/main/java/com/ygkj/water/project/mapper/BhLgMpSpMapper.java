package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhLgMpSp;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-28 9:36
 * @description
 */
public interface BhLgMpSpMapper extends BaseMapper<BhLgMpSp> {

    /**
     * 删除
     * @param id
     * @return
     */
    @Update("update bh_lg_mp_sp set del_flag = 1 where id = #{id}")
    Integer deleteBySpId(@Param("id") String id);
}
