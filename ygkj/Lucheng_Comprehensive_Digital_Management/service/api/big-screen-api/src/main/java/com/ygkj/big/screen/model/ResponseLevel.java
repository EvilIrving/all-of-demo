package com.ygkj.big.screen.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wgf
 * @date 2022/5/9 11:27
 * @Description:
 */
@Data
public class ResponseLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    private String id;

    /**
     * 响应级别
     */
    private Integer responseLevel;

    /**
     * 级别说明
     */
    private String explainData;

    /**
     * 0否，1是
     */
    private Integer whetherOpen;

    private String name;

    public ResponseLevel() {}
}
