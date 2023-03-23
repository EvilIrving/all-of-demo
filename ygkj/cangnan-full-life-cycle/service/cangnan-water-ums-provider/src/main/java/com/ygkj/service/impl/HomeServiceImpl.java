package com.ygkj.service.impl;

import com.ygkj.entity.SysDept;
import com.ygkj.entity.SysUser;
import com.ygkj.service.DeptService;
import com.ygkj.service.HomeService;
import com.ygkj.service.PermissionService;
import com.ygkj.service.UserService;
import com.ygkj.vo.resp.HomeRespVO;
import com.ygkj.vo.resp.PermissionRespNode;
import com.ygkj.vo.resp.UserInfoRespVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: HomeServiceImpl
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/10/25 21:27
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/10/25 21:27
 * @Version: 0.0.1
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public HomeRespVO getHomeInfo(String userId,String system) {


        SysUser sysUser=userService.detailInfo(userId);
        UserInfoRespVO vo=new UserInfoRespVO();

        if(sysUser!=null){
            BeanUtils.copyProperties(sysUser, vo);
            SysDept sysDept = deptService.detailInfo(sysUser.getDeptId());
            if(sysDept!=null){
                vo.setDeptId(sysDept.getId());
                vo.setDeptName(sysDept.getName());
            }

        }

        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId, system);

        HomeRespVO respVO=new HomeRespVO();
        respVO.setMenus(menus);
        respVO.setUserInfo(vo);

        return respVO;
    }
}
