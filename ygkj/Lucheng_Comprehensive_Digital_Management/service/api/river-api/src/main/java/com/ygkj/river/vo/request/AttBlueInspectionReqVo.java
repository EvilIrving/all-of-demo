package com.ygkj.river.vo.request;

import com.ygkj.supervision.vo.response.SysUserResVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2021-08-05 21:48
 * @description 蓝线管理巡查任务请求类
 */
@Data
@ApiModel("蓝线管理巡查任务请求类")
public class AttBlueInspectionReqVo {

    /**
     * 派发异常问题插入
     */
    @ApiModelProperty("派发异常问题列表插入，只需要获取对应 异常配置条目 名称即可")
    private List<String> problems;

    /**
     * 巡查阶段 1-开工 2-施工 3-完工
     */
    @ApiModelProperty("巡查阶段 1-前期 2-施工 3-完工")
    private Integer stage;

    /**
     * 指定首次巡查人员
     */
    @ApiModelProperty("指定首次巡查人员")
    private String inspector;

    /**
     * 首次巡查人员id
     */
    @ApiModelProperty("指定首次巡查人员id")
    private String inspectorId;

    @ApiModelProperty("派发所属项目id")
    private String projectId;

    @ApiModelProperty("派发所属项目名称")
    private String projectName;

    @ApiModelProperty("下次巡查时间的时候，添加为该时间")
    private Date patrolBeginTime;
    /**
     * 派发人
     */
    @ApiModelProperty("派发人名称")
    private String distributor;



    //复核检查时候添加
    @ApiModelProperty("本次巡查id")
    private String inspectionId;

    @ApiModelProperty("下发整改名称")
    private String rectifyName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("整改开始时间")
    private Date rectifyBeginTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("整改结束时间")
    private Date rectifyEndTime;

    @ApiModelProperty("复核巡检人员")
    private String reviewer;

    @ApiModelProperty("复核巡检人员")
    private String reviewerId;

    @ApiModelProperty("截止时间，天数 传integer")
    private String rectifyNoticeTime;

//    @ApiModelProperty("被通知人员信息列表")
//    private List<SysUserResVo> alertPeoples;

    @ApiModelProperty("被通知人员信息列表")
    private String alertPeoples;
    //案件结办接口添加参数

    @ApiModelProperty("结办人员id")
    private String coordinatorId;

    @ApiModelProperty("结办人员")
    private String coordinator;

    @ApiModelProperty("结办文件")
    private String coordinatorFile;

    @ApiModelProperty("结办文件ids")
    private String coordinatorFileIds;

    @ApiModelProperty("结办后是否通知 1-是 0-否'")
    private Integer coordinatorNotify;

    @ApiModelProperty("本次巡查是否已完成   1-是 0-否'")
    private String resultStatus;

    @ApiModelProperty("持续时间（天）")
    private Integer pendingTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("结办时间")
    private Date coordinatorDate;

    @ApiModelProperty("结办说明")
    private String coordinatorProduce;

//    @ApiModelProperty("结办通知人员列表")
//    private List<SysUserResVo> coordinatorPeoples;


}
