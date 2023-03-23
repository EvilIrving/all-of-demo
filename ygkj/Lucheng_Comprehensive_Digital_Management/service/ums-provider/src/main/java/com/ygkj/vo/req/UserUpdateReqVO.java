package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName: UserUpdateReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/20 16:42
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/20 16:42
 * @Version: 0.0.1
 */
@Data
public class UserUpdateReqVO {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "所属机构")
    private String deptId;

    @ApiModelProperty(value = "真实名称")
    private String realName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "账户状态(1.正常 2.锁定 )")
    private Integer status;

    @ApiModelProperty(value = "性别(1.男 2.女)")
    private Integer sex;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "所拥有的角色")
    private List<String> roleIds;

    @ApiModelProperty(value = "用户类型（1水利局用户 2普通企业用户）")
//    @NotBlank(message = "用户类型不能为空")
    private String userType;
    @ApiModelProperty(value = "是否拥有管理员身份")
    private boolean isAdmin;
    @ApiModelProperty(value = "头像url")
    private String slogan;
    @ApiModelProperty(value = "用户职务")
    private String job;
    @ApiModelProperty(value = "排序号")
    private Integer sort;
    @ApiModelProperty(value = "身份证号")
    private String identityNumber;
}
