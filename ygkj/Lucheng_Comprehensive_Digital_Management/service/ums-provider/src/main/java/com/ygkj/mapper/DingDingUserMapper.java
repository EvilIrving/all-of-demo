package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.DingDingUser;
import com.ygkj.entity.SysUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DingDingUserMapper {

    /**
     * 获取所有用户
     *
     * @return
     */
    @Select("SELECT * FROM sys_user_copy")
    List<SysUser> listAllUser();

    /**
     * 批量插入
     *
     * @param saveList
     */
    void batchInsert(List<JSONObject> saveList);

    /**
     * 批量更新
     *
     * @param updateList
     */
    void batchUpdate(List<JSONObject> updateList);

    /**
     * 查询所有还没绑定到系统中或者修改过了的用户
     *
     * @return
     */
    List<DingDingUser> listBind();
}
