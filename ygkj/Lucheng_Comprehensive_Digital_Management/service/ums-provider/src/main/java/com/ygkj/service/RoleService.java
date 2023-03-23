package com.ygkj.service;

import com.ygkj.entity.SysRole;
import com.ygkj.vo.req.RoleAddReqVO;
import com.ygkj.vo.req.RolePageReqVO;
import com.ygkj.vo.req.RoleUpdateReqVO;
import com.ygkj.vo.resp.PageVO;

import java.util.List;

/**
 * @ClassName: RoleService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:38
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:38
 * @Version: 0.0.1
 */
public interface RoleService {

    SysRole addRole(RoleAddReqVO vo);

    void updateRole(RoleUpdateReqVO vo, String accessToken);

    SysRole detailInfo(String id);

    void deletedRole(String id);

    PageVO<SysRole> pageInfo(RolePageReqVO vo);

    List<SysRole> getRoleInfoByUserId(String userId);

    List<String> getRoleNames(String userId);

    List<SysRole> selectAllRoles();
}
