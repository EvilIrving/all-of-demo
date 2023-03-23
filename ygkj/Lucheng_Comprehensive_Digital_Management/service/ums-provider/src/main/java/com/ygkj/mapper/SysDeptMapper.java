package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SysDept;
import com.ygkj.project.model.CompanyBindingPrcd;
import com.ygkj.vo.req.DeptReqVo;
import com.ygkj.vo.resp.DeptRespVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     *
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
     *
     * @param list
     */
    void batchInsert(List<JSONObject> list);

    /**
     * 批量更新部门信息
     *
     * @param updateList
     */
    void batchUpdate(List<JSONObject> updateList);

    List<com.ygkj.auth.api.department.model.SysDept> findByIds(String deptIds);

    List<SysDept> findDataNotInIds(List<String> list);

    List<SysDept> findParentChild(String parentId);

    @Select("SELECT * FROM company_binding_prcd WHERE wyh_id = #{wyhId}")
    CompanyBindingPrcd selectCompany(CompanyBindingPrcd companyBindingPrcd);

    List<com.ygkj.auth.api.department.model.SysDept> selectDeptByIdList(List<String> list);

    List<DeptRespVo> getDeptTreeTableList(DeptReqVo reqVo);

    List<JSONObject> getUnitList();
}