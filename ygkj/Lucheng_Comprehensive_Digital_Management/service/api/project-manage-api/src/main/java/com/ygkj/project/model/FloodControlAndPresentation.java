package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FloodControlAndPresentation extends PageEntity {
    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 生成文件的时间
     */
    @ApiModelProperty("生成文件的时间")
    private String tm;

    /**
     * 文件表id
     */
    @ApiModelProperty("文件表id")
    private String fileId;

    /**
     * 1.防汛简报，2.水雨情简报
     */
    @ApiModelProperty("1.防汛简报，2.水雨情简报")
    private String type;

    @TableField(exist = false)
    private SysFile file;
}
