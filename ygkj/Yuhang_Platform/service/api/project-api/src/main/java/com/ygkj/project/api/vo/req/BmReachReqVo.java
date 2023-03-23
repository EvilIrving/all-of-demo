package com.ygkj.project.api.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 河道基本信息表
 * @author hucong
 * @date 2022-04-27
 */
@Data
@ApiModel("河道基本信息表")
public class BmReachReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * dts_cmf_id
    */
    @ApiModelProperty("河道主键id")
    private String dtsCmfId;

    /**
    * 河道（段）编码
    */
    @ApiModelProperty("河道（段）编码")
    private String code;

    /**
    * 河道（段）名称
    */
    @ApiModelProperty("河道（段）名称")
    private String name;

    /**
    * 行政区划编码
    */
    @ApiModelProperty("行政区划编码")
    private String adcd;

    /**
    * 等级
    */
    @ApiModelProperty("等级")
    private String grade;

    /**
    * 干支流：（0、干流；1、一级支流；2、二级支流；3、三级支流；4、四级支流；5、五级支流；6、六级支流等；rn-1、平原河道）
    */
    @ApiModelProperty("干支流：（0、干流；1、一级支流；2、二级支流；3、三级支流；4、四级支流；5、五级支流；6、六级支流等；rn-1、平原河道）")
    private String mntrb;

    /**
    * 所属流域
    */
    @ApiModelProperty("所属流域")
    private String bas;

    /**
    * 归属科室
    */
    @ApiModelProperty("归属科室")
    private String dtsCmfDept;

    /**
    * 数据版本
    */
    @ApiModelProperty("数据版本")
    private Integer dtsCmfVersion;

    /**
    * 水域类型 1:河道 2:湖泊 3:水库 4:塘坝 5:其他
    */
    @ApiModelProperty("水域类型 1:河道 2:湖泊 3:水库 4:塘坝 5:其他")
    private Integer type;

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;


    public BmReachReqVo() {}
}