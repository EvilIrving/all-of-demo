package com.ygkj.flood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.vo.response.WarehouseMaterialResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lxl
 * @create 2022-05-20 9:22
 * @description
 */
public interface AttWarehouseAddMapper extends BaseMapper<AttWarehouseAdd> {


    @Select("select warehouse_id,material_name, sum(num) `num`,univalent,standard,amount from att_warehouse_add where warehouse_id = #{warehouseId} group by warehouse_id,material_name,univalent")
    List<WarehouseMaterialResVo> staticsAddList(@Param("warehouseId") String warehouseId);

    @Update("update att_warehouse_material_standard set univalent = #{univalent} where `name` = #{materialName}")
    Integer setUnivalent(@Param("univalent") BigDecimal univalent,@Param("materialName") String materialName);
}
