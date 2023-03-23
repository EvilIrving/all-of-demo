package com.ygkj.service;

import com.ygkj.vo.req.UserRoleOperationReqVO;

import java.util.List;

/**
 * @ClassName: UserRoleService
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/19 11:39
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/19 11:39
 * @Version: 0.0.1
 */
public interface UserRoleService {

    int removeByRoleId(String roleId);

    List<String> getRoleIdsByUserId(String userId);


    void addUserRoleInfo(UserRoleOperationReqVO vo);

    int removeByUserId(String userId);


    List<String> getUserIdsByRoleIds(List<String> roleIds);

}
