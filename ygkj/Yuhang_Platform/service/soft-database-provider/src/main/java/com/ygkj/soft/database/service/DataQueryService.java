package com.ygkj.soft.database.service;

import com.alibaba.fastjson.JSONObject;

import javax.validation.constraints.NotNull;

/**
 * @author Huang.zh
 * @date 2020/9/1 16:22
 * @Description: 数据查询服务抽象
 */
public interface DataQueryService {

    JSONObject queryByGlobalId(@NotNull(message = "globalId不允许为空！") String globalId,
                               @NotNull(message = "tableName不允许为空！") String tableName);

    JSONObject queryNewestVersionById(@NotNull(message = "id不允许为空！") String id,
                                      @NotNull(message = "tableName不允许为空！") String tableName);

}
