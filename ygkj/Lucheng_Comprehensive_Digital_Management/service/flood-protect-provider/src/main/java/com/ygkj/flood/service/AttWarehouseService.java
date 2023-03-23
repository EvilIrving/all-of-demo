package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ygkj.flood.model.AttWarehouseAdd;
import com.ygkj.flood.model.AttWarehouseDeviceBase;
import com.ygkj.flood.model.AttWarehouseOut;
import com.ygkj.flood.vo.request.AttWarehouseAddVo;
import com.ygkj.flood.vo.request.AttWarehouseDeviceBaseReqVo;
import com.ygkj.flood.vo.request.AttWarehousePageQueryVo;
import com.ygkj.flood.vo.response.AttWarehouseDeviceBaseRespVo;
import com.ygkj.flood.vo.response.AttWarehouseResVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface AttWarehouseService {

    PageVO<AttWarehouseResVo> attWarehouses(AttWarehousePageQueryVo queryVo);

    /**
     * 统计防汛仓库--用于组件详表表头
     * @param queryVo
     * @return
     */
    JSONObject stats4PagedWareHouses(AttWarehousePageQueryVo queryVo);

    /**
     * 导出防汛仓库
     * @param queryVo
     * @param response
     */
    void exportWareHouses(AttWarehousePageQueryVo queryVo, HttpServletResponse response);

    boolean insert(AttWarehouseAddVo attWarehouseAddVo);

    boolean update(AttWarehouseAddVo attWarehouseAddVo);

    boolean delete(String ids);

    JSONArray warehousesGroupByArea();

    Map<String,Object> warehousesGroupByType();

    JSONArray staticAcreageByArea();

    List<JSONObject> staticAcreageByType();

    JSONArray staticMaterialPriceByArea();

    Map<String,Object> staticMaterialPriceByType();

    void wareHouseSendSms(String phone, String userName,String content);

    /**
     * 统计项（仓库总数、参考总面积、物资总价值）
     * @return
     */
    Map<String,Object> warehousesCount();

    PageVO<AttWarehouseDeviceBaseRespVo> selectDevice(AttWarehouseDeviceBaseReqVo vo);

    int insertOrUpdateDevice(AttWarehouseDeviceBase base,String warehouseId);

    int deleteDevice(AttWarehouseDeviceBaseReqVo vo);

    void exportDevice(AttWarehouseDeviceBaseReqVo vo, HttpServletResponse response);

    /**
     * 物资规格清单列表
     * @return
     */
    List<JSONObject> warehouseMaterialStandardList();

    /**
     * 仓库数量新增
     * @param attWarehouseAdd
     * @return
     */
    boolean addToDevice(AttWarehouseAdd attWarehouseAdd);

    /**
     * 仓库数量减少
     * @param attWarehouseOut
     * @return
     */
    boolean outToDevice(AttWarehouseOut attWarehouseOut);
}
