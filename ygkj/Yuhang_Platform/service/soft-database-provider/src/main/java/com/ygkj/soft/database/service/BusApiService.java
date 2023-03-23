package com.ygkj.soft.database.service;

import com.ygkj.soft.database.api.VO.ApiCategoryUpdateVo;
import com.ygkj.soft.database.api.VO.BusApiParamUpdateVo;
import com.ygkj.soft.database.api.VO.BusApiQueryVo;
import com.ygkj.soft.database.api.VO.BusApiUpdateVo;
import com.ygkj.soft.database.api.model.ApiCategory;
import com.ygkj.soft.database.api.model.BusApi;
import com.ygkj.soft.database.api.model.BusApiParam;
import com.ygkj.water.model.CommonResult;
import ygkj.com.assistant.pager.EasyuiPager;
import ygkj.com.assistant.pager.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @description 接口基础表
 * @author wd
 * @date 2020-09-18
 */
public interface BusApiService {

    /**
     * 新增
     */
    void insert(BusApiUpdateVo updateVo) throws Exception;

    /**
     * 删除
     */
    void delete(String id) throws Exception;

    /**
     * 更新
     */
    void update(BusApiUpdateVo updateVo) throws Exception;

    /**
     * 根据主键 id 查询
     */
    BusApi load(String id) throws Exception;

    /**
     * 分页查询
     */
    Pager pageList(BusApiQueryVo queryVo, EasyuiPager easyuiPager) throws Exception;

    /**
     * 保存接口参数
     * @param paramList
     */
    void saveApiParams(List<BusApiParamUpdateVo> paramList)  throws Exception;

    /**
     * 查询接口参数
     * @param apiId
     * @param bus_type
     * @return
     */
    List<BusApiParam> queryApiParams(String apiId, Integer bus_type)  throws Exception;

    /**
     * 接口参数id
     * @param apiParamId
     */
    void delApiParam(String apiParamId) throws Exception;

    /**
     * 接口分类树
     * @return
     * @throws Exception
     */
    List<ApiCategory> apiCategoryTree() throws Exception;

    /**
     * 保存接口分类
     * @param updateVo
     * @throws Exception
     */
    void saveApiCategory(ApiCategoryUpdateVo updateVo) throws Exception;

    /**
     * 更新接口分类
     * @param updateVo
     * @throws Exception
     */
    void updateApiCategory(ApiCategoryUpdateVo updateVo) throws Exception;

    /**
     * 删除接口分类
     * @param apiCategoryId
     * @throws Exception
     */
    void delApiCategory(String apiCategoryId) throws Exception;

    /**
     * 外部调用
     * @param outUrl
     * @return
     */
    CommonResult externalCall(HttpServletRequest request, String outUrl);

    CommonResult generateApi(String dataSourceName,String tableName,String desc);
}
