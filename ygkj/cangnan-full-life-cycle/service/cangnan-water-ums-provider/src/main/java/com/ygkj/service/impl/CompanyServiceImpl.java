package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.constants.Constant;
import com.ygkj.entity.SysCompany;
import com.ygkj.entity.SysUser;
import com.ygkj.exception.BusinessException;
import com.ygkj.exception.code.BaseResponseCode;
import com.ygkj.mapper.SysCompanyMapper;
import com.ygkj.mapper.SysUserMapper;
import com.ygkj.service.CompanyService;
import com.ygkj.service.UserRoleService;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.PasswordUtils;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: huangzh
 * @Date: 2020/8/24 17:04
 * @Description:
 */
@Service
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private SysCompanyMapper sysCompanyMapper;

    /*@Autowired
    private UserService userService;*/

    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<SysCompany> selectAll(String userId) {
        // 查询当前用户是否为企业用户
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        String userType = sysUser.getUserType();
        if ("1".equals(userType)) {
            // 水利局用户，获取所用企业
            return sysCompanyMapper.selectAll();
        }
        // 企业用户
        List<SysCompany> list = new ArrayList<>();
        SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(sysUser.getDeptId());
        list.add(sysCompany);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changeStatus(CompanyUpdateReqVO vo) {
        // String socialCreditCode = vo.getSocialCreditCode();
        String status = vo.getStatus();
        try {
            if (StringUtils.isEmpty(status) || Integer.valueOf(status) <= 0 || Integer.valueOf(status) > 4) {
                throw new BusinessException(BaseResponseCode.DATA_ERROR);
            }
        } catch (Exception e) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        if ("2".equals(status)) {
            //修改该企业下管理员用户状态为启用状态，未启用之前只有管理员一个用户
            sysUserMapper.updateByDeptId(1, vo.getId());
        }
        if ("4".equals(status)) {
            //修改该企业下所有用户状态为停用状态
            sysUserMapper.updateByDeptId(2, vo.getId());
        }
        //修改企业状态
        int i = sysCompanyMapper.updateStatusById(vo);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public PageVO<SysUser> pageCompanyUserInfos(UserPageUserByDeptReqVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<SysUser> userListByDeptId = sysUserMapper.getUserListByDeptIdAndName(vo.getDeptId(), vo.getUsername());

        return PageUtils.getPageVO(userListByDeptId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysCompany addCompany(CompanyAddReqVO vo) {
        //新增注册企业
        SysCompany sysCompany = new SysCompany();
        BeanUtils.copyProperties(vo, sysCompany);
        sysCompany.setId(UUID.randomUUID().toString());
        sysCompany.setRegisterTime(new Date());
        sysCompany.setCreateTime(new Date());
        sysCompany.setStatus(1);

        int count = sysCompanyMapper.insertSelective(sysCompany);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        // 为注册管理员开户
        SysUser sysUser = new SysUser();
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setUsername(sysCompany.getPhone());
        sysUser.setSalt(PasswordUtils.getSalt());
        sysUser.setPassword(PasswordUtils.encode(Constant.DEFAULT_PASSWORD, sysUser.getSalt()));
        sysUser.setPhone(sysCompany.getPhone());
        sysUser.setDeptId(sysCompany.getId());
        sysUser.setRealName(sysCompany.getManagerName());
        sysUser.setStatus(1);
        sysUser.setUserType("2");
        sysUser.setAdmin(true);
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insertSelective(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        //为企业管理员初始化权限
        List<String> list = new ArrayList<>();
        list.add(Constant.SUPER_ADMIN_ROLE_ID);
        UserRoleOperationReqVO operationReqVO = new UserRoleOperationReqVO(sysUser.getId(), list);
        userRoleService.addUserRoleInfo(operationReqVO);
        return sysCompany;
    }

    @Override
    public SysCompany checkCompanyRegister(String socialCreditCode) {
        if (socialCreditCode == null || StringUtils.isEmpty(socialCreditCode)) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        return sysCompanyMapper.checkCompany(socialCreditCode);
    }

    @Override
    public SysCompany queryCompanyByUser(String userId) {
        //根据userId获取单位id（dept_id）
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (StringUtils.isEmpty(sysUser.getDeptId())) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }

        SysCompany sysCompany = sysCompanyMapper.checkCompany(sysUser.getDeptId());
        if (null == sysCompany) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysCompany;
    }

    @Override
    public void updateCompanyInfo(CompanyUpdateReqVO vo) {

        SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(vo.getId());
        if (null == sysCompany) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        int count = sysCompanyMapper.updateByPrimaryKeySelective(vo);

        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public SysUser getCompanyUser(String id) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        return sysUser;
    }

    @Override
    public void updateCompanyUser(UserUpdateReqVO vo, String userId) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(vo.getId());
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setUpdateTime(new Date());

        // 判断是否为管理员
        boolean admin = sysUser.isAdmin();
        if (admin) {
            // 管理员
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
        } else {
            // 不是管理员，比较是否为当前用户
            if (sysUser.getId().equals(userId)) {
                // 当前用户
                sysUserMapper.updateByPrimaryKeySelective(sysUser);
            } else {
                throw new BusinessException(500, "当前用户非企业管理员，无权更改他人信息");
            }
        }
    }

    @Override
    public void updateCompanyUserStatus(String id, Integer status) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", id);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        // 获取当前用户所在企业状态
        SysCompany sysCompany = sysCompanyMapper.selectByPrimaryKey(sysUser.getDeptId());
        if (null == sysCompany){
            throw new BusinessException(500, "操作失败，请联系管理员");
        }
        Integer companyStatus = sysCompany.getStatus();
        if (companyStatus == 2) {
            // 判断是否为管理员
            boolean admin = sysUser.isAdmin();
            if (admin) {
                // 管理员
                sysUserMapper.updateByDeptId(status, id);
            } else {
                throw new BusinessException(500, "当前用户非企业管理员，无权更改状态");
            }
        } else {
            throw new BusinessException(500, "当前用户企业状态下不允许修改用户状态");
        }
    }
}
