package com.ygkj.flood.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.flood.model.AttDefenObjectBase;

import java.util.List;

/**
 * @author lxl
 * @create 2022-05-17 16:19
 * @description
 */
public interface AttDefenObjectBaseService extends IService<AttDefenObjectBase> {

    List<JSONObject> defenObjectTree();


}
