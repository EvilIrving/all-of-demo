package com.ygkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.mapper.SysDeptMapper;
import com.ygkj.service.DeptService;
import com.ygkj.service.RedisService;
import com.ygkj.service.UserService;
import com.ygkj.utils.CodeUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.DeptRespNodeVO;
import com.ygkj.vo.resp.DeptRespVo;
import com.ygkj.vo.resp.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @ClassName: DeptServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 13:38
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 13:38
 * @Version: 0.0.1
 */
@Service
@Slf4j
public class DeptServiceImpl implements DeptService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private UserService userService;

    @Override
    public SysDept addDept(DeptAddReqVO vo) {
        System.out.println("fds");
        String relationCode;
        long result = redisService.incrby(Constant.DEPT_CODE_KEY, 1);
        String deptCode = CodeUtil.deptCode(String.valueOf(result), 6, "0");
        SysDept parent = sysDeptMapper.selectByPrimaryKey(vo.getPid());
        if (vo.getPid().equals("0")) {
            relationCode = deptCode;
        } else if (null == parent) {
            log.error("传入的 pid:{}不合法", vo.getPid());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        } else {
            relationCode = parent.getRelationCode() + deptCode;
        }
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(vo, sysDept);
        sysDept.setCreateTime(new Date());
        sysDept.setId(UUID.randomUUID().toString());
        sysDept.setDeptNo(deptCode);
        sysDept.setRelationCode(relationCode);
        int count = sysDeptMapper.insertSelective(sysDept);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysDept;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptUpdateReqVO vo) {

        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(vo.getId());
        if (null == sysDept) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysDept update = new SysDept();
        BeanUtils.copyProperties(vo, update);
        update.setUpdateTime(new Date());
        int count = sysDeptMapper.updateByPrimaryKeySelective(update);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        /**
         * 说明层级发生了变化
         */
        if (!StringUtils.isEmpty(vo.getPid()) && !vo.getPid().equals(sysDept.getPid())) {
            SysDept parent = sysDeptMapper.selectByPrimaryKey(vo.getPid());
            if (!vo.getPid().equals("0") && null == parent) {
                log.error("传入 的 pid:{}不合法", vo.getId());
                throw new BusinessException(BaseResponseCode.DATA_ERROR);
            }
            SysDept oldParent = sysDeptMapper.selectByPrimaryKey(sysDept.getPid());
            String oldRelationCode;
            String newRelationCode;
            /**
             * 根目录降到其他目录
             */
            if (sysDept.getPid().equals("0")) {
                oldRelationCode = sysDept.getDeptNo();
                newRelationCode = parent.getRelationCode() + sysDept.getDeptNo();
            } else if (vo.getPid().equals("0")) {//其他目录升级到跟目录
                oldRelationCode = sysDept.getRelationCode();
                newRelationCode = sysDept.getDeptNo();
            } else {
                oldRelationCode = oldParent.getRelationCode();
                newRelationCode = parent.getRelationCode();
            }
            sysDeptMapper.updateRelationCode(oldRelationCode, newRelationCode, sysDept.getRelationCode());
        }
    }

    @Override
    public SysDept detailInfo(String id) {
        return sysDeptMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleted(String id) {
        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(id);
        if (null == sysDept) {
            log.error("传入 的 id:{}不合法", id);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<String> deptIds = sysDeptMapper.selectChildIds(sysDept.getRelationCode());
        List<SysUser> list = userService.getUserListByDeptIds(deptIds);
        if (!list.isEmpty()) {
            throw new BusinessException(BaseResponseCode.NOT_PERMISSION_DELETED_DEPT);
        }
        sysDept.setDeleted(0);
        sysDept.setUpdateTime(new Date());
        int count = sysDeptMapper.updateByPrimaryKeySelective(sysDept);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public PageVO<SysDept> pageInfo(DeptPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysDept> sysDepts = sysDeptMapper.selectAll();
        return PageUtils.getPageVO(sysDepts);
    }

    @Override
    public List<SysDept> deptTreeList(String deptId) {
        List<SysDept> list;
        if (StringUtils.isEmpty(deptId)) {
            list = sysDeptMapper.selectAll();
        } else {
            SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
            if (sysDept == null) {
                throw new BusinessException(BaseResponseCode.DATA_ERROR);
            }
            List<String> childIds = sysDeptMapper.selectChildIds(sysDept.getRelationCode());
            list = sysDeptMapper.selectChildDept(childIds);
        }
        //默认加一个顶级方便新增顶级部门
//        DeptRespNodeVO respNodeVO=new DeptRespNodeVO();
//        respNodeVO.setTitle("默认顶级部门");
//        respNodeVO.setId("0");
//        respNodeVO.setSpread(true);
//        respNodeVO.setChildren(getTree(list));
//        List<DeptRespNodeVO> result=new ArrayList<>();
//        result.add(respNodeVO);
//        return result;
        return list;
    }

    @Override
    public List<DeptRespNodeVO> getDeptTree(String deptId) {
        List<SysDept> list;
        if (StringUtils.isEmpty(deptId)) {
            list = sysDeptMapper.selectAll();
        } else {
            SysDept sysDept = sysDeptMapper.selectByPrimaryKey(deptId);
            if (sysDept == null) {
                throw new BusinessException(BaseResponseCode.DATA_ERROR);
            }
            List<String> childIds = sysDeptMapper.selectChildIds(sysDept.getRelationCode());
            list = sysDeptMapper.selectAllByNotContainChild(childIds);
        }
        //默认加一个顶级方便新增顶级部门
        DeptRespNodeVO respNodeVO = new DeptRespNodeVO();
        respNodeVO.setTitle("默认顶级部门");
        respNodeVO.setId("0");
        respNodeVO.setSpread(true);
        respNodeVO.setChildren(getTree(list));
        List<DeptRespNodeVO> result = new ArrayList<>();
        result.add(respNodeVO);
        return result;
    }

    private List<DeptRespNodeVO> getTree(List<SysDept> all) {
        List<DeptRespNodeVO> list = new ArrayList<>();
        for (SysDept sysDept : all) {
            if (sysDept.getPid().equals("0")) {
                DeptRespNodeVO deptTree = new DeptRespNodeVO();
                BeanUtils.copyProperties(sysDept, deptTree);
                deptTree.setTitle(sysDept.getName());
                deptTree.setSpread(true);
                deptTree.setChildren(getChild(sysDept.getId(), all));
                list.add(deptTree);
            }
        }
        return list;
    }

    private List<DeptRespNodeVO> getChild(String id, List<SysDept> all) {
        List<DeptRespNodeVO> list = new ArrayList<>();
        for (SysDept sysDept : all) {
            if (sysDept.getPid().equals(id)) {
                DeptRespNodeVO deptTree = new DeptRespNodeVO();
                BeanUtils.copyProperties(sysDept, deptTree);
                deptTree.setTitle(sysDept.getName());
                deptTree.setChildren(getChild(sysDept.getId(), all));
                list.add(deptTree);
            }
        }
        return list;
    }

    @Override
    public PageVO<SysUser> pageDeptUserInfo(UserPageUserByDeptReqVO vo) {

        SysDept sysDept = sysDeptMapper.selectByPrimaryKey(vo.getDeptId());
        if (null == sysDept) {
            log.error("传入 的 id:{}不合法", vo.getDeptId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
//        List<String> strings = sysDeptMapper.selectChildIds(sysDept.getRelationCode());

        return userService.selectUserInfoByDeptIds(vo.getDeptId());
    }

    @Override
    public List<SysDept> selectAll() {
        List<SysDept> list = sysDeptMapper.selectAll();
        for (SysDept sysDept : list) {
            SysDept parent = sysDeptMapper.selectByPrimaryKey(sysDept.getPid());
            if (parent != null) {
                sysDept.setPidName(parent.getName());
            }
        }
        return list;
    }

    @Override
    public List<com.ygkj.auth.api.department.model.SysDept> selectDeptByIdList(List<String> idList) {
        return sysDeptMapper.selectDeptByIdList(idList);
    }

    /**
     * 部门树列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<DeptRespVo> getDeptTreeTableList(DeptReqVo reqVo) {
        List<DeptRespVo> list = sysDeptMapper.getDeptTreeTableList(reqVo);

        List<DeptRespVo> result = new ArrayList<>();
        Map<String, DeptRespVo> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(list)) {

            for (DeptRespVo respVo : list) {
                if (Objects.equals(respVo.getPid(), reqVo.getPid())) {
                    result.add(respVo);
                }
                map.put(respVo.getId(), respVo);
            }

            for (DeptRespVo respVo : list) {
                String pid = respVo.getPid();
                if (map.containsKey(pid)) {
                    DeptRespVo parent = map.get(pid);
                    List<DeptRespVo> children = parent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parent.setChildren(children);
                    }
                    children.add(respVo);
                }
            }
        }

        return result;
    }

    @Override
    public List<JSONObject> getUnitList() {
        return sysDeptMapper.getUnitList();
    }
}
