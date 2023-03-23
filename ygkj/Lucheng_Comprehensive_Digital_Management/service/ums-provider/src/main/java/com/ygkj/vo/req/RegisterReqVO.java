package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @ClassName: RegisterReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/4 14:40
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/4 14:40
 * @Version: 0.0.1
 */
@Data
public class RegisterReqVO {
    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    @ApiModelProperty(value = "电话")
    private String phone;
    @ApiModelProperty(value = "真实名称")
    private String realName;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "性别(1.男 2.女) ")
    private Integer sex;
    @ApiModelProperty(value = "创建来源(1.web 2.android 3.ios )")
    private Integer createWhere;
    @ApiModelProperty(value = "所属机构")
    @NotBlank(message = "所属机构不能为空")
    private String deptId;
    @ApiModelProperty(value = "用户类型 1水利局用户 2普通企业用户")
    private String userType;
    @ApiModelProperty(value = "是否拥有管理员身份")
    private boolean isAdmin;
    @ApiModelProperty(value = "头像url")
    private String slogan;

}
