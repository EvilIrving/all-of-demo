package com.ygkj.flood.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.AttMaterialBase;
import com.ygkj.flood.vo.request.AttMaterialBaseAddVo;
import com.ygkj.flood.vo.request.AttMaterialQueryVo;
import com.ygkj.flood.vo.request.MaterialDispatchRequestVo;
import com.ygkj.flood.vo.response.AttMaterialResVo;
import com.ygkj.flood.vo.response.AttWarehouseResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 物资信息
 *
 * @author huangzh
 * @date 2021/01/18
 */
public interface AttMaterialBaseMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int insert(AttMaterialBase attMaterialBase);

    int batchInsert(@Param("list") List<AttMaterialBaseAddVo> list);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int delete(@Param("ids") String ids);

    int spacialDelete(@Param("warehouseId") String warehouseId, @Param("typeId") String typeId);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int update(AttMaterialBase attMaterialBase);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    AttMaterialBase load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    List<AttMaterialBase> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2021/01/18
     **/
    int pageListCount(int offset, int pagesize);

    List<AttMaterialResVo> selectByCondition(AttMaterialQueryVo queryVo);

    List<String> selectMaterials();

    List<AttWarehouseResVo> matchMaterialWarehouses(@Param("list") List<MaterialDispatchRequestVo> list);


    List<AttWarehouseResVo> matchMaterialWarehouseAdd(@Param("list") List<MaterialDispatchRequestVo> list);

    @Select("SELECT COUNT(1) as num ,type,name FROM att_material_base \n" +
            "WHERE del_flag = 0 GROUP BY type,`name`")
    List<Map<String, Object>> emergencyCount();

    @Select("select a.`name`,  if(a.num - b.num, a.num - b.num , a.num) num  from " +
            "( select material_name `name` , sum(num) num from att_warehouse_add group by material_name ) a  left join " +
            "(  select material_name `name` , sum(num) num from att_warehouse_out group by material_name ) b on a.`name` = b.`name` ")
    List<JSONObject> findMaterials();
}

