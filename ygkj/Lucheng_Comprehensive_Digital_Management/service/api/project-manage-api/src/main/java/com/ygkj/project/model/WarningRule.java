package com.ygkj.project.model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author wgf
 * @date 2021/10/22 14:51
 * @description 预警规则配置表
 */
@Data
public class WarningRule extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 预警规则（1.安全鉴定超期，2.汛前检查超期，3.年度总结超期，4.巡查信息异常，5.工程隐患，6.海塘实时报警，7.海塘预报预警，8.时段雨量报警，9.河道水情报警，10.水库水情报警，11.潮位报警，12.测站关联报警，13.测站限值报警，14.预案资料提醒，15.日常检查提醒，16.汛后检查提醒，17.年度检查提醒，18.汛前检查提醒）
     */
    @ApiModelProperty(value = "预警规则（1.安全鉴定超期，2.汛前检查超期，3.年度总结超期，4.巡查信息异常，5.工程隐患，6.海塘实时报警，" +
            "7.海塘预报预警，8.时段雨量报警，9.河道水情报警，10.水库水情报警，11.潮位报警，12.测站关联报警，13.测站限值报警，" +
            "14.预案资料提醒，15.日常检查提醒，16.汛后检查提醒，17.年度检查提醒，18.汛前检查提醒）")
    private Integer warningrule;

    /**
     * 绑定站点/海塘工程的数目
     */
    @ApiModelProperty(value = "绑定站点/海塘工程的数目")
    private Integer bindcount;

    /**
     * 发送人数
     */
    @ApiModelProperty(value = "发送人数")
    private Integer sendcount;

    /**
     * 绑定的站点/海塘工程的code，多个逗号拼接
     */
    @ApiModelProperty(value = "绑定的站点/海塘工程的code，多个逗号拼接")
    private String bindcode;

    /**
     * 发送人员的id
     */
    @ApiModelProperty(value = "发送人员的id，多个逗号拼接")
    private String sendcode;

    /**
     * 报警启闭（1.开启、2关闭）
     */
    @ApiModelProperty(value = "报警启闭（1.开启、2关闭）")
    private Integer isopen;

    /**
     * 新增时间
     */
    @ApiModelProperty(value = "新增时间")
    private Date tm;

    @ApiModelProperty("配置海塘责任人1.政府负责人，2.水行政部门负责人，3.主管部门负责人，4.管理单位责任人，5.技术责任人，6.巡查责任人")
    private String htzrn;

    @ApiModelProperty("测站类型 1水位站 2雨量站 3流量站")
    private Integer stType;

    @ApiModelProperty("差额限值（上限）")
    private Double maximum;

    @ApiModelProperty("差额限值（下限）")
    private Double minimum;

    @ApiModelProperty("时段雨量报警统计时间")
    private Double rainhour;

    @ApiModelProperty("时段雨量报警累计降雨量值")
    private Double cumulativerain;

    @ApiModelProperty("河道水情报警超过警戒水位值")
    private Double overwarninglevel;

    @ApiModelProperty("河道水情报警超过保证水位值")
    private Double overguaranteelevel;

    @ApiModelProperty("水库水情报警超过汛限水位值")
    private Double overlimitlevel;

    @ApiModelProperty("水库水情报警超过防洪高水位值")
    private Double overfloodlevel;

    @ApiModelProperty("潮位报警超过蓝色警报水位值")
    private Double overbluelevel;

    @ApiModelProperty("潮位报警超过黄色警报水位值")
    private Double overyellowlevel;

    @ApiModelProperty("潮位报警超过橙色警报水位值")
    private Double overorangelevel;

    @ApiModelProperty("潮位报警超过红色警报水位值")
    private Double overredlevel;

    @ApiModelProperty("规则配置（1.报警2.提醒）")
    private Integer ruletype;

    @ApiModelProperty("提醒频率（1.1天/次、2.1周/次、3.半月/次、4.1月/次、5.1小时/次、6.2小时/次、7.3小时/次）")
    private Integer frequency;

    @TableField(exist = false)
    private List<WarningUser> users;

    @TableField(exist = false)
    private  List<JSONObject> bindList;

    @TableField(exist = false)
    private String warningRuleString;

    @TableField(exist = false)
    private String isOpenString;

    public WarningRule() {}

}
