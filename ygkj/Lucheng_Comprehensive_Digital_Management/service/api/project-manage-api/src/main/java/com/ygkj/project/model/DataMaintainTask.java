package com.ygkj.project.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DataMaintainTask extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("id")
    private String id;

    /**
    目录id
    */
    private String directoryId;

    /**
     * 目录名称
     */
    private String directoryName;

    /**
     * 数据项
     */
    private String dataItem;

    /**
     * 详细描述
     */
    private String description;

    /**
     * 数据来源
     */
    private String source;

    /**
     * 任务内容
     */
    private String content;

    /**
     * 图片id
     */
    private String pictures;

    /**
     * 责任单位名称
     */
    private String unitName;


    /**
     * 责任单位负责人
     */
    private String unitDirector;

    /**
     * 责任单位负责人联系方式
     */
    private String unitDirectorPhone;

    /**
     * 责任单位反馈信息
     */
    private String unitFeedbackInfo;

    /**
     * 责任单位处理情况
     */
    private String unitHandleStatus;

    /**
     * 第三方单位json
     */
    private String thirdPartyUnit;

    /**
     * 发送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 状态 0-未处理 1-已处理 2-逾期未处理 3-取消任务
     */
    private String status;

    /**
     * 数据维护负责人处理情况
     */
    private String dataMaintainStatus;
    /**
     * 数据维护负责人
     */
    private String dataMaintainName;
    /**
     * 数据维护负责人联系电话
     */
    private String dataMaintainPhone;

    /**
    数据维护负责人反馈信息
    */
    private String dataMaintainFeedbackInfo;

    /**
    处理人信息
    */
    private String handleUserId;

    /**
    维护信息id
    */
    private String infoId;

    /**
     表示信息是否同步工程运管 0未同步 1同步的责任人数据
     */
    private String transManageDataType;

/*    @TableField(exist = false)
    private List<SysFile> sysFiles;*/


    @TableField(exist = false)
    private List<ThirdPartyUnitInfo> thirdPartyUnitInfos;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     第三方责任人反馈集合
     */
    @TableField(exist = false)
    private String feedbackInfo;
}
