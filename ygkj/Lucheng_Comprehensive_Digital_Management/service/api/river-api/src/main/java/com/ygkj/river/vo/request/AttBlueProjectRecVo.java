package com.ygkj.river.vo.request;

import com.ygkj.river.model.AttBlueProject;
import com.ygkj.river.vo.response.RiverInfoResVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2021-08-13 17:02
 * @description
 */
@Data
public class AttBlueProjectRecVo extends AttBlueProject {


    @ApiModelProperty("河流信息")
    private RiverInfoResVo river;

}
