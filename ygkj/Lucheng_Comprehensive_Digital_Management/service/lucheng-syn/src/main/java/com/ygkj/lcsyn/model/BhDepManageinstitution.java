package com.ygkj.lcsyn.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class BhDepManageinstitution {

    @TableId(type = IdType.INPUT)
    /**
     * 编号
     */
    private Integer id;

    /**
     * 附件
     */
    private String aid;

    /**
     * 编制时间
     */
    private Date preparationTime;

    /**
     * 单位编号
     */
    private Integer depId;

    /**
     * 制度名称
     */
    private String name;

    /**
     * pjcd
     */
    private String pjcd;
}
