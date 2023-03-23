package com.ygkj.project.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author yjx
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="LbKnowledge对象", description="")
public class LbKnowledgeRespVo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "规范名称")
    private String canonicalName;

    @ApiModelProperty(value = "附件")
    private String appendix;

    @ApiModelProperty(value = "附件地址")
    private String appendixUrl;

    @ApiModelProperty(value = "附件名称")
    private String appendixName;
}
