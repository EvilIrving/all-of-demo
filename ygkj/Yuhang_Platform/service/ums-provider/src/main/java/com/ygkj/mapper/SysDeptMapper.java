package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    /**
     * 多个数据 要用 @Param
     * @param oldStr
     * @param newStr
     * @param relationCode
     * @return
     */
    int updateRelationCode(@Param("oldStr") String oldStr, @Param("newStr") String newStr, @Param("relationCode") String relationCode);

    List<SysDept> selectAll();

    List<String> selectChildIds(String relationCode);

    List<SysDept> selectAllByNotContainChild(List<String> list);

    List<SysDept> selectChildDept(List<String> list);

    /**
     * 批量插入部门信息
     * @param list
     */
    void batchInsert(List<JSONObject> list);

    /**
     * 批量更新部门信息
     * @param updateList
     */
    void batchUpdate(List<JSONObject> updateList);

    List<SysDept> findDataNotInIds(List<String> list);

    List<SysDept> findParentChild(String parentId);

    List<com.ygkj.auth.api.department.model.SysDept> findByIds(String deptIds);
}