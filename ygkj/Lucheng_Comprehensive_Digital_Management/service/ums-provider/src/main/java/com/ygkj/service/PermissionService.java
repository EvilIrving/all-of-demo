package com.ygkj.service;

import com.alibaba.fastjson.JSONArray;
import com.ygkj.auth.VO.request.PermissionAddReqVO;
import com.ygkj.auth.VO.request.PermissionPageReqVO;
import com.ygkj.auth.VO.request.PermissionUpdateReqVO;
import com.ygkj.auth.VO.request.SysPermissionPageQueryVo;
import com.ygkj.entity.SysPermission;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.PermissionRespNode;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: PermissionService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:39
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:39
 * @Version: 0.0.1
 */
public interface PermissionService {

    List<SysPermission> getPermission(String userId, String system);

    SysPermission addPermission(PermissionAddReqVO vo);

    SysPermission detailInfo(String permissionId);

    void updatePermission(PermissionUpdateReqVO vo);

    void deleted(String permissionId);

    PageVO<SysPermission> pageInfo(PermissionPageReqVO vo);

    List<SysPermission> selectAll();

    Set<String> getPermissionsByUserId(String userId);

    List<PermissionRespNode> permissionTreeList(String userId, String system);

    List<PermissionRespNode> selectAllByTree();

    List<PermissionRespNode> selectAllMenuByTree(String permissionId);

    List<PermissionRespNode> getRolesAllPermissions(String roleId);

    List<PermissionRespNode> getAllMenusTree(List<SysPermission> list);

    List<SysPermission> getRolesPermissionList(String roleId);

    JSONArray polymerizationRoles();

    PageVO<SysPermission> currentUserJobs(SysPermissionPageQueryVo queryVo);
}
