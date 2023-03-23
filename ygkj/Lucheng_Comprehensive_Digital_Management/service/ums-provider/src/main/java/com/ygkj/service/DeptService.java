package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.DeptRespNodeVO;
import com.ygkj.vo.resp.DeptRespVo;
import com.ygkj.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: DeptService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:38
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:38
 * @Version: 0.0.1
 */
public interface DeptService {

    SysDept addDept(DeptAddReqVO vo);

    void updateDept(DeptUpdateReqVO vo);

    SysDept detailInfo(String id);

    void deleted(String id);

    PageVO<SysDept> pageInfo(DeptPageReqVO vo);


    List<SysDept> deptTreeList(String deptId);

    List<DeptRespNodeVO> getDeptTree(String deptId);

    PageVO<SysUser> pageDeptUserInfo(UserPageUserByDeptReqVO vo);

    List<SysDept> selectAll();

    List<com.ygkj.auth.api.department.model.SysDept> selectDeptByIdList(List<String> idList);

    /**
     * 部门树列表
     *
     * @param reqVo
     * @return
     */
    List<DeptRespVo> getDeptTreeTableList(DeptReqVo reqVo);

    List<JSONObject> getUnitList();

}
