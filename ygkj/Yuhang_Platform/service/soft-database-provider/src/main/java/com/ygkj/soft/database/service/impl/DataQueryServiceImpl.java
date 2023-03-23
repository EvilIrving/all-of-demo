package com.ygkj.soft.database.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.soft.database.dao.DataDao;
import com.ygkj.soft.database.service.DataQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Huang.zh
 * @date 2020/9/1 16:23
 * @Description: 数据查询服务具体实现
 */
@Service("dataQueryService")
public class DataQueryServiceImpl implements DataQueryService {

    @Autowired
    private DataDao dataDao;

    /**
     * @Author Huang.zh
     * @Description 根据globalId去查询对应的记录
     * @Date 2020/9/1 17:08
     */
    @Override
    public JSONObject queryByGlobalId(@NotNull(message = "globalId不允许为空！") String globalId,
                                      @NotNull(message = "tableName不允许为空！") String tableName) {
        Map<String,Object> params = new HashMap<>();

        params.put("globalId",globalId);
        List<JSONObject> list = dataDao.queryByCondition(params, tableName);
        return list.isEmpty()?new JSONObject():list.get(0);
    }

    /**
     * @Author Huang.zh
     * @Description 获取业务库id对应的最新版本数据
     * @Date 2020/9/1 17:09
     */
    @Override
    public JSONObject queryNewestVersionById(@NotNull(message = "id不允许为空！") String id,
                                             @NotNull(message = "tableName不允许为空！") String tableName) {
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        List<JSONObject> list = dataDao.queryByCondition(params, tableName, " LIMIT 1 ");
        return  list.isEmpty()?null:list.get(0);
    }

}
