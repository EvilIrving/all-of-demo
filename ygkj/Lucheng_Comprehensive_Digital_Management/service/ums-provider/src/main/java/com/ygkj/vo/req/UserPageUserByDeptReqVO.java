package com.ygkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName: UserPageUserByDeptReqVO
 * TODO:类文件简单描述
 * @Author: Huang.zh
 * @CreateDate: 2019/9/20 17:00
 * @UpdateUser: Huang.zh
 * @UpdateDate: 2019/9/20 17:00
 * @Version: 0.0.1
 */
@Data
public class UserPageUserByDeptReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize = 10;

    @ApiModelProperty(value = "组织id")
    @NotBlank(message = "组织id不能为空")
    private String deptId;

    @ApiModelProperty(value = "人员名称")
    private String username;
}
