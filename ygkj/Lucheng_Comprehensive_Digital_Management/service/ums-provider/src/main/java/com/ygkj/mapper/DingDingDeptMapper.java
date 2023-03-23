package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.DingDingDept;
import com.ygkj.entity.SysDept;
import com.ygkj.vo.req.DingDingDeptVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface DingDingDeptMapper {

    /**
     * 查询本地已经同步的所有部门
     *
     * @return
     */
    @Select("SELECT * FROM sys_dept_copy")
    List<SysDept> listAll();

    /**
     * 查询本地已经同步的所有部门
     *
     * @return
     */
    @Select("SELECT * FROM sys_user")
    List<Map<String, Object>> listAll2();

    /**
     * 批量插入
     *
     * @param list
     */
    void batchInsert(List<JSONObject> list);

    /**
     * 批量更新
     *
     * @param list
     */
    void batchUpdate(List<JSONObject> list);

    /**
     * 钉钉部门和系统部门绑定
     *
     * @param dingDingDept
     */
    void bind(DingDingDept dingDingDept);

    /**
     * 批量绑定钉钉部门和系统部门
     *
     * @param list
     */
    void batchBind(List<DingDingDept> list);

    /**
     * 获取绑定关系列表
     *
     * @param vo
     * @return
     */
    List<DingDingDeptVo> listBind(DingDingDeptVo vo);

    /**
     * 根据id集合查询dept
     *
     * @return
     */
    List<DingDingDept> listByIds(List<String> list);
}
