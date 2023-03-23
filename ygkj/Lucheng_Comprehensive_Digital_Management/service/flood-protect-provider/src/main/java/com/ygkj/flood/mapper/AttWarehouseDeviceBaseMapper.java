package com.ygkj.flood.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.AttWarehouseDeviceBase;
import com.ygkj.flood.vo.request.AttWarehouseDeviceBaseReqVo;
import com.ygkj.flood.vo.response.AttWarehouseDeviceBaseRespVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author paf
 * @DESCRIPTION:
 * @create 2021-10-22 15:53
 */
public interface AttWarehouseDeviceBaseMapper {
   List<AttWarehouseDeviceBaseRespVo> selectDevice(AttWarehouseDeviceBaseReqVo vo);

   List<AttWarehouseDeviceBaseRespVo> batchSelect(List<String> list);

   int insertDevice(AttWarehouseDeviceBase base);

   int updateDevice(AttWarehouseDeviceBase base);

   int deleteDevice(AttWarehouseDeviceBaseReqVo vo);

   @Select("select `name`,`standard`,`univalent` from att_warehouse_material_standard")
    List<JSONObject> warehouseMaterialStandardList();


   AttWarehouseDeviceBase selectOneByCondition(AttWarehouseDeviceBase base);

}
