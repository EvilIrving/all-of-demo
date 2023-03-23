package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.DingDingUser;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DingDingUserMapper {

    /**
     * 根据deptId获取该部门所有已经同步的人员
     *
     * @param deptId
     * @return
     */
    @Select("SELECT * FROM dingding_user WHERE deptId = #{deptId}")
    List<DingDingUser> listByDeptId(String deptId);

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
