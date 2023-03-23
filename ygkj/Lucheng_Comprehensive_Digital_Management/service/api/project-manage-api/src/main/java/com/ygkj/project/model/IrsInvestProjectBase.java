package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 投资项目基本信息表   --数据来源  irs数据平台
 * @author Huangzh
 * @date 2022-06-20
 */
@Data
public class IrsInvestProjectBase implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 赋码时间
     */
    private Date applyDate;

    /**
     * 总建筑面积（平方米）
     */
    private String builtArea;

    /**
     * 项目联系人
     */
    private String contactName;

    /**
     * 联系人电话
     */
    private String contactTel;

    /**
     * 该项目审批、核准、备案部门所在行政区划代码。
     */
    private String divisionCode;

    /**
     * 拟建成时间
     */
    private String endYear;

    /**
     * 项目单位名称
     */
    private String enterpriseName;

    /**
     * 代码取自国标行业（国民经济行业分类）2017年本。
     */
    private String industry;

    /**
     * 国标行业对应的名称。
     */
    private String industryName;

    /**
     * 是否为子项目 0：否；1：是。
     */
    private String isChildProject;

    /**
     * 总用地面积（亩）
     */
    private String landArea;

    /**
     * 证照编号
     */
    private String lerepCertno;

    /**
     * 证照类型 ( a05100：企业营业执照(工商注册号) a05201：组织机构代码证（企业rn法人）；a05202：组织机构代码证（国家机关法人）；a05203：组织机rn构代码证（事业单位法人）；a05204：组织机构代码证（社会团体法人）；a05300：统一社会信用代码；a05900：其他。)
     */
    private String lerepCerttype;

    /**
     * 建设地点区划代码
     */
    private String placeCode;

    /**
     * 建设地点详情
     */
    private String placeCodeDetail;

    /**
     * 项目负责人
     */
    private String principalName;

    /**
     * 负责人电话
     */
    private String principalTel;

    /**
     * 项目代码
     */
    @TableId
    private String projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 建设性质 ( 0：新建；1：扩建；2：迁建；3：改建；4：其它)
     */
    private String projectNature;

    /**
     * 项目类型 (a00001：审批；a00002：核准；a00003：备案。)
     */
    private String projectType;

    /**
     * 建设规模及内容
     */
    private String scaleContent;

    /**
     * 拟开工时间
     */
    private String startYear;

    /**
     * 总投资额（万元）
     */
    private String totalMoney;

    /**
     * 数据有效标识：0无效；1有效
     */
    private String validityFlag;

    /**
     * 经度
     */
    private BigDecimal prLong;

    /**
     * 纬度
     */
    private BigDecimal prLat;

    /**
     * 涉河水域
     */
    private String wadingWaters;

    /**
     * 占用水域面积
     */
    private String occupiedWaterArea;

    /**
     * 涉河时长
     */
    private String wadingTime;

    /**
     * 是否归档  0 未归档 1 已归档
     */
    private String archive;

    /**
     * 批复文号
     */
    @TableField(exist = false)
    private String approvalNumber;


    public IrsInvestProjectBase() {
    }

}
