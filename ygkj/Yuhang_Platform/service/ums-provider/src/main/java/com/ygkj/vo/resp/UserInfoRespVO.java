package com.ygkj.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName: UserInfoRespVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/10/25 17:02
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/10/25 17:02
 * @Version: 0.0.1
 */
@Data
public class UserInfoRespVO {
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "真实姓名")
    private String realName;
    @ApiModelProperty(value = "所属机构id")
    private String deptId;
    @ApiModelProperty(value = "所属机构名称")
    private String deptName;
    @ApiModelProperty(value = "用户类型")
    private String userType;
}
