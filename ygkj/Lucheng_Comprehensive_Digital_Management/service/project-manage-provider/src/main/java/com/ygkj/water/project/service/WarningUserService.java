package com.ygkj.water.project.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.WarningUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author  wgf
 * @date  2021/10/21 17:52
 */


public interface WarningUserService extends IService<WarningUser> {

    void insert(WarningUser warningUser);

    boolean delete(String id);

    boolean update(WarningUser warningUser);


    List<JSONObject> addressBookList(@Param("key") String key);

    Object userList(String name);
}

