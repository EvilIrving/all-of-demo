package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.AttServiceItem;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author lxl
 * @create 2022-04-12 13:53
 * @description
 */
public interface AttServiceItemMapper extends BaseMapper<AttServiceItem> {

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @Update("update att_service_item set del_flag = 1 where id = #{id}")
    int deleteItemById(@Param("id") String id);
}
