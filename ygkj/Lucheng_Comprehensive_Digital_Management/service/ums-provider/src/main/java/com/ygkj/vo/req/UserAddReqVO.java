package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName: UserAddReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/22 16:46
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/22 16:46
 * @Version: 0.0.1
 */
@Data
public class UserAddReqVO {
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "账号不能为空")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phone;
    @ApiModelProperty(value = "创建来源(1.web 2.android 3.ios )")
    private Integer createWhere;
    @ApiModelProperty(value = "所属机构，（物业化公司id）")
    @NotBlank(message = "所属机构不能为空")
    private String deptId;
    @ApiModelProperty(value = "性别(1.男 2.女)")
    private String sex;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "用户类型（1水利局用户 2普通企业用户，3物业化公司用户）")
    @NotBlank(message = "用户类型不能为空")
    private String userType;
    @ApiModelProperty(value = "所拥有的角色")
    private List<String> roleIds;
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
    @ApiModelProperty(value = "岗位")
    private String post;
}
