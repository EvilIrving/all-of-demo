package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @ClassName: UserRoleOperationReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/20 14:40
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/20 14:40
 * @Version: 0.0.1
 */
@Data
public class UserRoleOperationReqVO {
    public UserRoleOperationReqVO(@NotBlank(message = "用户id不能为空") String userId,
                                  @NotEmpty(message = "角色id集合不能为空") List<String> roleIds) {
        this.userId = userId;
        this.roleIds = roleIds;
    }

    public UserRoleOperationReqVO() {
    }

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty(value = "角色id集合")
    @NotEmpty(message = "角色id集合不能为空")
    private List<String> roleIds;
}
