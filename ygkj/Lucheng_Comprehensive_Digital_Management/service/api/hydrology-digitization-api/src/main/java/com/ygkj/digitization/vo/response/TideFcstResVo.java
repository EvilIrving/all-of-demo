package com.ygkj.digitization.vo.response;

import com.ygkj.digitization.model.AttTideFcstBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Data
@ApiModel("风暴潮预报回参")
public class TideFcstResVo extends AttTideFcstBase {
    private static final long serialVersionUID = -3236628583365715443L;

    @ApiModelProperty("风暴潮手动预报值")
    List<TideFcstValResVo> manualValueList;
}
