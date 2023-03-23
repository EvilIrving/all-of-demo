package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: UpdatePasswordReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/10/27 0:27
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/10/27 0:27
 * @Version: 0.0.1
 */
@Data
public class UpdatePasswordReqVO {
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "旧密码")
    private String oldPwd;
    @ApiModelProperty(value = "新密码")
    private String newPwd;
    @ApiModelProperty(value = "是否加密")
    private Boolean isEncryption;
}
