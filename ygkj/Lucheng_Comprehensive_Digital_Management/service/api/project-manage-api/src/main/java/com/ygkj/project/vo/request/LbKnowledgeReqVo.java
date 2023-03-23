package com.ygkj.project.vo.request;

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
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="知识库", description="")
public class LbKnowledgeReqVo implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    private Integer id;


    @ApiModelProperty(value = "规范名称")
    private String canonicalName;


    @ApiModelProperty("页数")
    private Integer pageSize ;

    @ApiModelProperty("第几页")
    private Integer pageNum ;
}
