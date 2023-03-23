package com.ygkj.flood.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.flood.model.AttWarehouseBase;
import com.ygkj.flood.model.SysUser;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.flood.vo.response.AttWarehouseResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 防汛仓库信息
 * @author huangzh
 * @date 2021/01/18
 */
public interface AttWarehouseBaseMapper {

    /**
     * [新增]
     * @author huangzh
     * @date 2021/01/18
     **/
    int insert(AttWarehouseBase attWarehouseBase);

    /**
     * [刪除]
     * @author huangzh
     * @date 2021/01/18
     **/
    int delete(@Param("ids") String ids);

    /**
     * [更新]
     * @author huangzh
     * @date 2021/01/18
     **/
    int update(AttWarehouseBase attWarehouseBase);

    /**
     * [查询] 根据主键 id 查询
     * @author huangzh
     * @date 2021/01/18
     **/
    AttWarehouseBase load(int id);

    /**
     * [查询] 分页查询
     * @author huangzh
     * @date 2021/01/18
     **/
    List<AttWarehouseBase> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     * @author huangzh
     * @date 2021/01/18
     **/
    int pageListCount(int offset,int pagesize);

    List<AttWarehouseResVo> selectWarehouse(AttWarehousePageQueryVo queryVo);

    List<JSONObject> warehousesGroupByType();

    List<JSONObject> staticAcreageByType();

    List<JSONObject> staticMaterialPriceByType();

    @DS("main")
    SysUser findUserByPhone(@Param("phone") String phone);

    /**
     * 统计仓库数量
     * @return
     */
    @Select("select count(1) from att_warehouse_base where del_flag = 0")
    Integer countWarehousesNum();

    /**
     * 查询统计面积
     * @return
     */
    @Select("select sum(acreage) from att_warehouse_base where del_flag = 0 and area_name !=''")
    BigDecimal countTotalArea();

    /**
     * 查询参考总价值
     * @return
     */
    @Select("select sum(material_price) from att_warehouse_base where del_flag = 0 and area_name !=''")
    BigDecimal countMaterialPrice();
}

