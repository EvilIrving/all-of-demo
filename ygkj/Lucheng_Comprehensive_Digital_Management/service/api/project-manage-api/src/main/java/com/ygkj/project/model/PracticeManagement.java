package com.ygkj.project.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wgf
 * @date 2021/11/5 10:58
 * @Description: 演练管理
 */
@Data
public class PracticeManagement extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    /**
     * 主键
     */
    private String id;

    /**
     * 演练时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    /**
     * 演练地点
     */
    private String address;

    /**
     * 演练事由
     */
    private String reasons;

    /**
     * 参与人员
     */
    private String person;

    /**
     * 附件地址
     */
    private String fileurl;

    /**
     * 附件名称
     */
    private String filename;

    /**
     * 创建时间
     */
    private Date creattime;


    @TableField(exist = false)
    private String endtime;

    @TableField(exist = false)
    private String starttime;

    public PracticeManagement() {}
}
