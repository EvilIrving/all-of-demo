package com.ygkj.river.vo.response;

import com.ygkj.river.model.AttBlueInspection;
import com.ygkj.river.model.AttBlueInspectionProblem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lxl
 */
@Data
public class AttBlueInspectionResVo extends AttBlueInspection {

    /**
     * 项目地址
     */
    private String address;

    /**
     * 法人负责人
     */
    private String legalRepresent;

    /**
     * 负责人手机号
     */
    private String phone;

    @ApiModelProperty("携带问题信息")
    private List<AttBlueInspectionProblem> problems;

}
