package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.auth.VO.request.PermissionAddReqVO;
import com.ygkj.auth.VO.request.PermissionPageReqVO;
import com.ygkj.auth.VO.request.PermissionUpdateReqVO;
import com.ygkj.auth.VO.request.SysPermissionPageQueryVo;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysPermission;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.jwt.TokenSettings;
import com.ygkj.mapper.SysPermissionMapper;
import com.ygkj.service.PermissionService;
import com.ygkj.service.RedisService;
import com.ygkj.service.RolePermissionService;
import com.ygkj.service.UserRoleService;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.PermissionRespNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: PermissionServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:40
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:40
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    @Autowired
    private TokenSettings tokenSettings;

    /**
     * 根据用户查询拥有的权限
     * 先查出用户拥有的角色
     * 再去差用户拥有的权限
     * 也可以多表关联查询
     *
     * @param userId
     * @return java.util.List<SysPermission>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 11:42
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 11:42
     * @Version: 0.0.1
     */
    @Override
    public List<SysPermission> getPermission(String userId, String system) {
        List<String> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return null;
        }
        List<String> permissionIds = rolePermissionService.getPermissionIdsByRoles(roleIds);
        if (permissionIds.isEmpty()) {
            return null;
        }
        List<SysPermission> result = sysPermissionMapper.selectInfoByIds(permissionIds, system);
        return result;
    }

    /**
     * 新增菜单权限
     *
     * @param vo
     * @return SysPermission
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 12:24
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 12:24
     * @Version: 0.0.1
     */
    @Override
    public SysPermission addPermission(PermissionAddReqVO vo) {
        SysPermission sysPermission = new SysPermission();
        BeanUtils.copyProperties(vo, sysPermission);
        verifyForm(sysPermission);
        sysPermission.setId(UUID.randomUUID().toString());
        sysPermission.setCreateTime(new Date());
        int count = sysPermissionMapper.insertSelective(sysPermission);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysPermission;
    }

    /**
     * 操作后的菜单类型是目录的时候 父级必须为目录
     * 操作后的菜单类型是菜单的时候，父类必须为目录类型
     * 操作后的菜单类型是按钮的时候 父类必须为菜单类型
     */
    private void verifyFormPid(SysPermission sysPermission) {
        SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
        switch (sysPermission.getType()) {
            case 1:
                if (parent != null) {
                    if (parent.getType() != 1) {
                        throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                    }
                } else if (!sysPermission.getPid().equals("0")) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
                break;
            case 2:
                if (parent == null || parent.getType() != 1) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }

                break;
            case 3:
                if (parent == null || parent.getType() != 2) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if (StringUtils.isEmpty(sysPermission.getPerms())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getUrl())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if (StringUtils.isEmpty(sysPermission.getMethod())) {
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                break;
        }
    }

    /**
     * 编辑或者新增的时候检验
     *
     * @param sysPermission
     * @return void
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/10/31 21:22
     * @UpdateUser:
     * @UpdateDate: 2019/10/31 21:22
     * @Version: 0.0.1
     */
    private void verifyForm(SysPermission sysPermission) {

//        verifyFormPid(sysPermission);
        /**
         * id 不为空说明是编辑
         */
        if (!StringUtils.isEmpty(sysPermission.getId())) {
            List<SysPermission> list = sysPermissionMapper.selectChild(sysPermission.getId());
            if (!list.isEmpty()) {
                throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_UPDATE);
            }
        }

    }

    /**
     * 查询菜单权限详情
     *
     * @param permissionId
     * @return SysPermission
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 14:05
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 14:05
     * @Version: 0.0.1
     */
    @Override
    public SysPermission detailInfo(String permissionId) {

        return sysPermissionMapper.selectByPrimaryKey(permissionId);
    }

    /**
     * 更新菜单权限
     *
     * @param vo
     * @return void
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 14:04
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 14:04
     * @Version: 0.0.1
     */
    @Override
    public void updatePermission(PermissionUpdateReqVO vo) {

        SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(vo.getId());
        if (null == sysPermission) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysPermission update = new SysPermission();
        BeanUtils.copyProperties(vo, update);
        /**
         * 只有类型变更
         * 或者所属菜单变更
         */
        if (sysPermission.getType() != vo.getType() || !sysPermission.getPid().equals(vo.getPid())) {
            verifyForm(update);
        }
        update.setUpdateTime(new Date());
        int count = sysPermissionMapper.updateByPrimaryKeySelective(update);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        /**
         * 说明这个菜单权限 权鉴标识发生变化
         * 所有管理这个菜单权限用户将重新刷新token
         */
        if (StringUtils.isEmpty(vo.getPerms()) && !sysPermission.getPerms().equals(vo.getPerms())) {
            List<String> roleIds = rolePermissionService.getRoleIds(vo.getId());
            if (!roleIds.isEmpty()) {
                List<String> userIds = userRoleService.getUserIdsByRoleIds(roleIds);
                if (!userIds.isEmpty()) {
                    for (String userId : userIds) {
                        redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                        //清空权鉴缓存
                        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
                    }

                }
            }
        }

    }

    /**
     * 删除菜单权限
     * 判断是否 有角色关联
     * 判断是否有子集
     *
     * @param permissionId
     * @return void
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 14:04
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 14:04
     * @Version: 0.0.1
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleted(String permissionId) {
        SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(permissionId);
        if (null == sysPermission) {
            log.error("传入 的 id:{}不合法", permissionId);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<SysPermission> childs = sysPermissionMapper.selectChild(permissionId);
        if (!childs.isEmpty()) {
            throw new BusinessException(BaseResponseCode.ROLE_PERMISSION_RELATION);
        }
        sysPermission.setDeleted(0);
        sysPermission.setUpdateTime(new Date());
        int count = sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        /**
         * 删除和角色关联
         */
        rolePermissionService.removeByPermissionId(permissionId);
        List<String> roleIds = rolePermissionService.getRoleIds(permissionId);
        if (!roleIds.isEmpty()) {
            List<String> userIds = userRoleService.getUserIdsByRoleIds(roleIds);
            if (!userIds.isEmpty()) {
                for (String userId : userIds) {
                    redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                    //清空权鉴缓存
                    redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
                }

            }
        }
    }

    /**
     * 分页获取所有菜单权限
     *
     * @param vo
     * @return PageVO<SysPermission>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 14:03
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 14:03
     * @Version: 0.0.1
     */
    @Override
    public PageVO<SysPermission> pageInfo(PermissionPageReqVO vo) {
//        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<SysPermission> list = selectAll();
        return PageUtils.getPageVO(list);
    }

    /**
     * 获取所有菜单权限
     *
     * @param
     * @return java.util.List<SysPermission>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 14:03
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 14:03
     * @Version: 0.0.1
     */
    @Override
    public List<SysPermission> selectAll() {
        List<SysPermission> result = sysPermissionMapper.selectAll();
        if (!result.isEmpty()) {
            for (SysPermission sysPermission : result) {
                SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
                if (parent != null) {
                    sysPermission.setPidName(parent.getName());
                }
            }
        }
        return result;
    }

    /**
     * 获取权限标识
     *
     * @param userId
     * @return java.util.List<java.lang.String>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/20 23:13
     * @UpdateUser:
     * @UpdateDate: 2019/9/20 23:13
     * @Version: 0.0.1
     */
    @Override
    public Set<String> getPermissionsByUserId(String userId) {

        List<SysPermission> list = getPermission(userId, org.apache.commons.lang3.StringUtils.EMPTY);
        Set<String> permissions = new HashSet<>();
        if (null == list || list.isEmpty()) {
            return null;
        }
        for (SysPermission sysPermission : list) {
            if (!StringUtils.isEmpty(sysPermission.getPerms())) {
                permissions.add(sysPermission.getPerms());
            }

        }
        return permissions;
    }

    /**
     * 以树型的形式把用户拥有的菜单权限返回给客户端
     *
     * @param userId
     * @return java.util.List<PermissionRespNode>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/9/23 10:53
     * @UpdateUser:
     * @UpdateDate: 2019/9/23 10:53
     * @Version: 0.0.1
     */
    @Override
    public List<PermissionRespNode> permissionTreeList(String userId, String system) {
        List<SysPermission> list = getPermission(userId, system);
        return getTree(list, true);
    }

    /**
     * 递归获取菜单树
     *
     * @param all
     * @param type true:查询到菜单；false:查询到按钮
     * @return java.util.List<PermissionRespNode>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/10/27 18:27
     * @UpdateUser:
     * @UpdateDate: 2019/10/27 18:27
     * @Version: 0.0.1
     */
    private List<PermissionRespNode> getTree(List<SysPermission> all, boolean type) {

        List<PermissionRespNode> list = new ArrayList<>();
        if (all == null || all.isEmpty()) {
            return list;
        }
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals("0")) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
//                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setName(sysPermission.getName());
                permissionRespNode.setPath(permissionRespNode.getUrl());
                permissionRespNode.setSystem(sysPermission.getSystem());
                if (type) {
                    permissionRespNode.setChildren(getChildExcBtn(sysPermission.getId(), all));
                } else {
                    permissionRespNode.setChildren(getChildAll(sysPermission.getId(), all));
                }
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 递归遍历所有
     *
     * @param id
     * @param all
     * @return java.util.List<PermissionRespNode>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/10/25 16:51
     * @UpdateUser:
     * @UpdateDate: 2019/10/25 16:51
     * @Version: 0.0.1
     */
    private List<PermissionRespNode> getChildAll(String id, List<SysPermission> all) {

        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id)) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setName(sysPermission.getName());
                permissionRespNode.setPath(permissionRespNode.getUrl());
                permissionRespNode.setChildren(getChildAll(sysPermission.getId(), all));
                permissionRespNode.setSystem(sysPermission.getSystem());
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 只递归获取目录和菜单
     *
     * @param id
     * @param all
     * @return java.util.List<PermissionRespNode>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/10/25 16:51
     * @UpdateUser:
     * @UpdateDate: 2019/10/25 16:51
     * @Version: 0.0.1
     */
    private List<PermissionRespNode> getChildExcBtn(String id, List<SysPermission> all) {

        List<PermissionRespNode> list = new ArrayList<>();
        for (SysPermission sysPermission : all) {
            if (sysPermission.getPid().equals(id) && sysPermission.getType() != 4) {
                PermissionRespNode permissionRespNode = new PermissionRespNode();
                BeanUtils.copyProperties(sysPermission, permissionRespNode);
                permissionRespNode.setName(sysPermission.getName());
                permissionRespNode.setPath(permissionRespNode.getUrl());
                permissionRespNode.setChildren(getChildExcBtn(sysPermission.getId(), all));
                permissionRespNode.setSystem(sysPermission.getSystem());
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 获取所有菜单权限按钮
     *
     * @param
     * @return java.util.List<PermissionRespNode>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/10/27 18:29
     * @UpdateUser:
     * @UpdateDate: 2019/10/27 18:29
     * @Version: 0.0.1
     */
    @Override
    public List<PermissionRespNode> selectAllByTree() {

        List<SysPermission> list = selectAll();
        return getTree(list, false);
    }

    /**
     * 获取所有的目录菜单树排除按钮
     * 因为不管是新增或者修改
     * 选择所属菜单目录的时候
     * 都不可能选择到按钮
     * 而且编辑的时候 所属目录不能
     * 选择自己和它的子类
     *
     * @param
     * @return java.util.List<PermissionRespNode>
     * @throws
     * @Author: Huang.zh
     * @CreateDate: 2019/10/31 13:08
     * @UpdateUser:
     * @UpdateDate: 2019/10/31 13:08
     * @Version: 0.0.1
     */
    @Override
    public List<PermissionRespNode> selectAllMenuByTree(String permissionId) {

        List<SysPermission> list = selectAll();
        if (!list.isEmpty() && !StringUtils.isEmpty(permissionId)) {
            for (SysPermission sysPermission : list) {
                if (sysPermission.getId().equals(permissionId)) {
                    list.remove(sysPermission);
                    break;
                }
            }
        }
        List<PermissionRespNode> result = new ArrayList<>();
        //新增顶级目录是为了方便添加一级目录
        PermissionRespNode respNode = new PermissionRespNode();
        respNode.setId("0");
        respNode.setTitle("默认顶级菜单");
        respNode.setSpread(true);
        respNode.setChildren(getTree(list, true));
        result.add(respNode);
        return result;
    }

    @Override
    public List<PermissionRespNode> getRolesAllPermissions(String roleId) {
        List<String> permissionIds = rolePermissionService.getPermissionIdsByRoleId(roleId);
        List<SysPermission> list = sysPermissionMapper.selectAll().stream().filter(sysPermission ->
                permissionIds.contains(sysPermission.getId())).collect(Collectors.toList());
        List<PermissionRespNode> tree = getTree(list, false);
        return tree;
    }

    @Override
    public List<PermissionRespNode> getAllMenusTree(List<SysPermission> list) {
        List<PermissionRespNode> tree = getTree(list, false);
        return tree;
    }

    @Override
    public List<SysPermission> getRolesPermissionList(String roleId) {
        List<String> permissionIds = rolePermissionService.getPermissionIdsByRoleId(roleId);
        List<SysPermission> list = sysPermissionMapper.selectAll().stream().filter(sysPermission ->
                permissionIds.contains(sysPermission.getId())).collect(Collectors.toList());
        return list;
    }

    @Override
    public JSONArray polymerizationRoles() {
        JSONArray result = new JSONArray();
        List<Map<String, String>> list = sysPermissionMapper.polymerizationRoles(JwtTokenUtil.getCurrentUserId()
                .orElse(org.apache.commons.lang3.StringUtils.EMPTY));
        Map<String, List<Map<String, String>>> collect = list.stream().collect(Collectors.groupingBy(map -> map.get("TYPE")));
        collect.forEach((type, roles) -> {
            JSONObject object = new JSONObject();
            object.put("type", type);
            object.put("roles", roles);
            result.add(object);
        });
        return result;
    }

    @Override
    public PageVO<SysPermission> currentUserJobs(SysPermissionPageQueryVo queryVo) {
        List<SysPermission> permissions = getPermission(JwtTokenUtil.getCurrentUserId().orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                org.apache.commons.lang3.StringUtils.EMPTY);
        queryVo.setJob(Boolean.TRUE);
        queryVo.setIds(permissions.stream().map(SysPermission::getId).collect(Collectors.joining(",")));
        if (queryVo.getPageNum() != null && queryVo.getPageSize() != null) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<SysPermission> list = sysPermissionMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(list);
    }
}
