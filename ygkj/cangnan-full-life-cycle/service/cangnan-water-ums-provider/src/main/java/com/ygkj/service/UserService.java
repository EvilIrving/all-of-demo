package com.ygkj.service;

import com.ygkj.auth.VO.request.ValidateTicketVo;
import com.ygkj.entity.SysUser;
import com.ygkj.vo.req.*;
import com.ygkj.vo.resp.LoginRespVO;
import com.ygkj.vo.resp.PageVO;
import com.ygkj.vo.resp.UserOwnRoleRespVO;

import java.util.List;

/**
 * @ClassName: UserService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/7 22:55
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/7 22:55
 * @Version: 0.0.1
 */
public interface UserService {

    String register(RegisterReqVO vo);

    LoginRespVO login(LoginReqVO vo);


    String refreshToken(String refreshToken,String accessToken);

    void updateUserInfo(UserUpdateReqVO vo, String operationId, String userType, boolean userAdmin);


    PageVO<SysUser> pageInfo(UserPageReqVO vo);

    SysUser detailInfo(String userId);

    PageVO<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize,List<String> deptIds);

    void addUser(UserAddReqVO vo);

    void logout(String accessToken,String refreshToken);

    void updatePwd(UpdatePasswordReqVO vo,String userId,String accessToken, String refreshToken);

    List<SysUser> getUserListByDeptId(String deptId);

    List<SysUser> getUserListByDeptIds(List<String> deptIds);

    void deletedUsers(List<String> userIds,String operationId);

    UserOwnRoleRespVO getUserOwnRole(String userId);

    void setUserOwnRole(String userId,List<String> roleIds);

    /**
     * 水管理平台用户登录
     * @param vo
     * @return
     */
    LoginRespVO validateTicket(ValidateTicketVo vo);

    List<SysUser> users(String ids);

    void putUserChooseProject(String key, String jsonString);

    String getUserChooseProject(String key);

    /**
     * @description: 根据dept_id更新用户状态
     * @return: void
     * @Date: 2020/8/25 14:08
     * @params:[deptId]
     */
    /*void updateStatus(String deptId);*/

    /**
     * @description: 查找用户信息
     * @return: com.ygkj.entity.SysUser
     * @Date: 2020/8/26 9:33
     * @params:[userId]
     */
    /*SysUser selectUserInfoById(String userId);*/


}
