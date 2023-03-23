package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-18 14:05
 * @description
 */
@Data
@ApiModel
public class OverhaulReqVo extends PageEntity {

    @ApiModelProperty("更新使用id")
    private String id;

    @ApiModelProperty("请求类别：1-电气操作 2-机械操作")
    private Integer type;

    @ApiModelProperty("关联的工程id")
    private String pid;

    @ApiModelProperty("姓名模糊筛选")
    private String name;

    @ApiModelProperty("时间筛选")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operator;

    /**
     * 操作人id
     */
    @ApiModelProperty("操作人id")
    private String operatorId;

    /**
     * 监护人
     */
    @ApiModelProperty("监护人")
    private String guardian;

    /**
     * 监护人id
     */
    @ApiModelProperty("监护人id")
    private String guardianId;

    /**
     * 值班领导
     */
    @ApiModelProperty("值班领导")
    private String leader;

    /**
     * 值班领导id
     */
    @ApiModelProperty("值班领导id")
    private String leaderId;

    /**
     * 文件id，多文件用 ， 隔开存储
     */
    @ApiModelProperty("文件id，多文件用 ， 隔开存储")
    private String fileIds;


    /**
     * 工作负责人
     */
    @ApiModelProperty("工作负责人")
    private String principal;

    /**
     * 工作负责人id
     */
    @ApiModelProperty("工作负责人id")
    private String principalId;

    /**
     * 签发负责人
     */
    @ApiModelProperty("签发负责人")
    private String issueMan;

    /**
     * 签发负责人id
     */
    @ApiModelProperty("签发负责人id")
    private String issueManId;

    /**
     * 签发日期
     */
    @ApiModelProperty("签发日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date issueDate;

    /**
     * 三个人选不能重复
     * @return
     */
    public boolean isRepeatMan(){
        if (type == 1){
            if (operatorId.equals(guardianId) || operatorId.equals(leaderId) || guardianId.equals(leaderId)){
                return true;
            }
        }else {
            if (issueManId.equals(principalId)){
                return true;
            }
        }
        return false;
    }

}
