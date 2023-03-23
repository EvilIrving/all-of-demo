package com.ygkj.auth.VO.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: LoginRespVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/7 23:05
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/7 23:05
 * @Version: 0.0.1
 */
@Data
public class LoginRespVO {
    @ApiModelProperty(value = "token")
    private String accessToken;
    @ApiModelProperty(value = "刷新token")
    private String refreshToken;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "电话")
    private String phone;
//    @ApiModelProperty(value = "用户所拥有的菜单权限(前后端分离返回给前端控制菜单和按钮的显示和隐藏)")
//    private List<PermissionRespNode> list;
    @ApiModelProperty(value = "用户头像URL")
    private String slogan;
    @ApiModelProperty(value = "用户类型 1水利局用户 2普通企业用户")
    private String userType;
    private String msg;
}
