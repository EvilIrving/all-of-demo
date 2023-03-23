package com.ygkj.flood.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-05-09 10:02
 * @description
 */
@Data
@ApiModel("责任人请求类")
public class ResponsibleReqVo extends PageEntity {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 船名
     */
    @ApiModelProperty("船名")
    private String boatName;

    /**
     * 船舶所有人名单
     */
    @ApiModelProperty("船舶所有人名单")
    private String boatOwner;

    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String phone;


    /**
     * 种粮大户名单
     */
    @ApiModelProperty("种粮大户名单")
    private String name;

    /**
     * 城镇
     */
    @ApiModelProperty("城镇")
    private String city;

    /**
     * 村庄
     */
    @ApiModelProperty("村庄")
    private String village;

    @ApiModelProperty("模糊筛选字段")
    private String key;

}
