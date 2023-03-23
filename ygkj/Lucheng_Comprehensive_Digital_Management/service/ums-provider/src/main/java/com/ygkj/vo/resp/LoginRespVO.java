package com.ygkj.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
    @ApiModelProperty(value = "用户所拥有的菜单权限(前后端分离返回给前端控制菜单和按钮的显示和隐藏)")
    private List<PermissionRespNode> list;
    @ApiModelProperty(value = "用户头像URL")
    private String slogan;
    @ApiModelProperty(value = "用户类型 1水利局用户 2普通企业用户")
    private String userType;
    @ApiModelProperty(value = "部门名称")
    private String department;
    @ApiModelProperty(value = "部门id")
    private String deptId;
    @ApiModelProperty(value = "角色id")
    private String jobId;
    @ApiModelProperty(value = "钉钉用户唯一标识")
    private String openid;
    @ApiModelProperty(value = "浙政钉用户是否绑定")
    private Boolean isBind;
    @ApiModelProperty(value = "锁定信息")
    private String lockMsg;
    @ApiModelProperty(value = "票据，浙里办老化版用")
    private String ticketId;
    public LoginRespVO() {
    }

    public LoginRespVO(String username, String phone) {
        this.username = username;
        this.phone = phone;
    }
}
