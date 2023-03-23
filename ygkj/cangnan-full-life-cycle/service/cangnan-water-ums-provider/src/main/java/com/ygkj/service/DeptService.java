package com.ygkj.service;

import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.vo.req.DeptAddReqVO;
import com.ygkj.vo.req.DeptPageReqVO;
import com.ygkj.vo.req.DeptUpdateReqVO;
import com.ygkj.vo.req.UserPageUserByDeptReqVO;
import com.ygkj.vo.resp.DeptRespNodeVO;
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
}
