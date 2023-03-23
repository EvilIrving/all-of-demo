package com.ygkj.flood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-20 9:23
 * @description
 */
public interface AttWarehouseOutMapper extends BaseMapper<AttWarehouseOut> {

    @Select("select warehouse_id,material_name, sum(num) `num`,univalent,standard from att_warehouse_out where warehouse_id = #{warehouseId} group by warehouse_id,material_name,univalent")
    List<WarehouseMaterialResVo> staticsOutList(@Param("warehouseId") String warehouseId);
}
