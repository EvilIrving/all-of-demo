package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hucong
 * @description 山塘基础信息表
 * @date 2021-08-08
 */
@Data
@ApiModel("山塘基础信息表 ")
@NoArgsConstructor
public class AttMnpdBaseReqVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在县")
    private String countryAdnm;

    /**
     * 山塘代码
     */
    @ApiModelProperty("山塘代码")
    private String mnpdCode;

    /**
     * 山塘名称
     */
    @ApiModelProperty("山塘名称")
    private String mnpdName;

    /**
     * 坝顶中点经度
     */
    @ApiModelProperty("坝顶中点经度")
    private BigDecimal dtMidLong;

    /**
     * 坝顶中点维度
     */
    @ApiModelProperty("坝顶中点维度")
    private BigDecimal dtMidLat;

    /**
     * 所在乡镇（街道）名称
     */
    @ApiModelProperty("所在乡镇（街道）名称")
    private String strName;

    /**
     * 地区
     */
    @ApiModelProperty("地区")
    private String dscd;

    /**
     * 所在村名称
     */
    @ApiModelProperty("所在村名称")
    private String vilName;

    /**
     * 主要功能
     */
    @ApiModelProperty("主要功能")
    private String zygn;

    /**
     * 所有权人（管理责任主体）
     */
    @ApiModelProperty("所有权人（管理责任主体）")
    private String syqr;

    /**
     * 所有权人电话
     */
    @ApiModelProperty("所有权人电话")
    private String syqrdh;

    /**
     * 产权证发证机关、编号
     */
    @ApiModelProperty("产权证发证机关、编号")
    private String cqzfzjgbh;

    /**
     * 巡查员姓名
     */
    @ApiModelProperty("巡查员姓名")
    private String xcyxm;

    /**
     * 巡查员电话
     */
    @ApiModelProperty("巡查员电话")
    private String xcydh;

    /**
     * 主管部门
     */
    @ApiModelProperty("主管部门")
    private String zgbm;

    /**
     * 是否进行山塘整治
     */
    @ApiModelProperty("是否进行山塘整治")
    private String sfjxstzz;

    /**
     * 设计单位
     */
    @ApiModelProperty("设计单位")
    private String sjdw;

    /**
     * 校核洪水（p）
     */
    @ApiModelProperty("校核洪水（p）")
    private BigDecimal zlcd;

    /**
     * 校核洪水（水位）
     */
    @ApiModelProperty("校核洪水（水位）")
    private BigDecimal jhhsP;

    /**
     * 校核洪水（容积）
     */
    @ApiModelProperty("校核洪水（容积）")
    private BigDecimal jhhsRj;

    /**
     * 设计洪水（p）
     */
    @ApiModelProperty("设计洪水（p）")
    private BigDecimal sjhsP;

    /**
     * 设计洪水（水位）
     */
    @ApiModelProperty("设计洪水（水位）")
    private BigDecimal sjhsSw;

    /**
     * 设计洪水（容积）
     */
    @ApiModelProperty("设计洪水（容积）")
    private BigDecimal sjhsRj;

    /**
     * 正常蓄水位（水位）
     */
    @ApiModelProperty("正常蓄水位（水位）")
    private BigDecimal zcsxswSw;

    /**
     * 正常蓄水位（容积）
     */
    @ApiModelProperty("正常蓄水位（容积）")
    private BigDecimal zcxswRj;

    /**
     * 影响区域（km2）
     */
    @ApiModelProperty("影响区域（km2）")
    private BigDecimal yxqy;

    /**
     * 影响区域内村庄个数（个）
     */
    @ApiModelProperty("影响区域内村庄个数（个）")
    private Integer yxqynczgs;

    /**
     * 影响人口（人）
     */
    @ApiModelProperty("影响人口（人）")
    private Integer yxrk;

    /**
     * 日供水能力（t）
     */
    @ApiModelProperty("日供水能力（t）")
    private BigDecimal rgsnl;

    /**
     * 灌溉面积（亩）
     */
    @ApiModelProperty("灌溉面积（亩）")
    private BigDecimal ggmj;

    /**
     * 坝型
     */
    @ApiModelProperty("坝型")
    private String bx;

    /**
     * 背水坡脚高程（m）
     */
    @ApiModelProperty("背水坡脚高程（m）")
    private BigDecimal bspgc;

    /**
     * 迎水坡坡比
     */
    @ApiModelProperty("迎水坡坡比")
    private String ysppg;

    /**
     * 背水坡坡比
     */
    @ApiModelProperty("背水坡坡比")
    private String bsppg;

    /**
     * 是否兼做坝顶公路
     */
    @ApiModelProperty("是否兼做坝顶公路")
    private String sfjzbdgl;

    /**
     * 泄洪建筑物-溢流宽度（m）
     */
    @ApiModelProperty("泄洪建筑物-溢流宽度（m）")
    private BigDecimal xhjzwYlkd;

    /**
     * 输水建筑物-型式
     */
    @ApiModelProperty("输水建筑物-型式")
    private String ssjzwXs;

    /**
     * 输水建筑物-长度（m）
     */
    @ApiModelProperty("输水建筑物-长度（m）")
    private BigDecimal ssjzwCd;

    /**
     * 输水建筑物-断面尺寸（m）
     */
    @ApiModelProperty("输水建筑物-断面尺寸（m）")
    private BigDecimal ssjzwDmcc;

    /**
     * 是否有金属结构
     */
    @ApiModelProperty("是否有金属结构")
    private String sfyjsjg;

    /**
     * 是否存在渗漏
     */
    @ApiModelProperty("是否存在渗漏")
    private String sfczsl;

    /**
     * 渗漏位置
     */
    @ApiModelProperty("渗漏位置")
    private String slwz;

    /**
     * 管理房
     */
    @ApiModelProperty("管理房")
    private String glf;

    /**
     * 管理房面积（m2）
     */
    @ApiModelProperty("管理房面积（m2）")
    private BigDecimal glfmj;

    /**
     * 管理房位置
     */
    @ApiModelProperty("管理房位置")
    private String glfwz;

    /**
     * 是否完成标准化管理
     */
    @ApiModelProperty("是否完成标准化管理")
    private String sfwcbzhgl;

    /**
     * 观测设施
     */
    @ApiModelProperty("观测设施")
    private String gcss;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String filenam;

    /**
     * 照片-整体面貌
     */
    @ApiModelProperty("照片-整体面貌")
    private String picZtmm;

    /**
     * 照片-坝顶面貌
     */
    @ApiModelProperty("照片-坝顶面貌")
    private String picBdmm;

    /**
     * 照片-上游概况
     */
    @ApiModelProperty("照片-上游概况")
    private String picSygk;

    /**
     * 照片-下游面貌
     */
    @ApiModelProperty("照片-下游面貌")
    private String picXymm;

    /**
     * 照片-溢洪道
     */
    @ApiModelProperty("照片-溢洪道")
    private String picYhd;

    /**
     * 照片-放水设施
     */
    @ApiModelProperty("照片-放水设施")
    private String picFsss;

    /**
     * 照片-排水棱体
     */
    @ApiModelProperty("照片-排水棱体")
    private String picPslt;

    /**
     * 工程任务
     */
    @ApiModelProperty("工程任务")
    private String projectTask;

    /**
     * 大坝类型按材料分
     */
    @ApiModelProperty("大坝类型按材料分")
    private String damTypeByMaterials;

    /**
     * 建成时间(年/月)
     */
    @ApiModelProperty("建成时间(年/月)")
    private String completeTime;

    /**
     * 整治开始时间
     */
    @ApiModelProperty("整治开始时间")
    private Date remstTime;

    /**
     * 整治完成时间
     */
    @ApiModelProperty("整治完成时间")
    private Date remfiTime;

    /**
     * 坝址以上集雨面积
     */
    @ApiModelProperty("坝址以上集雨面积")
    private BigDecimal rcdsArea;

    /**
     * 总容积
     */
    @ApiModelProperty("总容积")
    private BigDecimal totalVol;

    /**
     * 山塘类别
     */
    @ApiModelProperty("山塘类别")
    private String mnpdCate;

    /**
     * 山塘功能
     */
    @ApiModelProperty("山塘功能")
    private String mnpdFunc;

    /**
     * 坝顶高程
     */
    @ApiModelProperty("坝顶高程")
    private BigDecimal dmtpEle;

    /**
     * 最大坝高
     */
    @ApiModelProperty("最大坝高")
    private BigDecimal madmHeight;

    /**
     * 坝顶宽度
     */
    @ApiModelProperty("坝顶宽度")
    private BigDecimal madmWidth;

    /**
     * 坝长
     */
    @ApiModelProperty("坝长")
    private BigDecimal damLength;

    /**
     * 主要泄洪建筑物型式
     */
    @ApiModelProperty("主要泄洪建筑物型式")
    private String dscncltp;

    /**
     * 溢洪道堰顶高程
     */
    @ApiModelProperty("溢洪道堰顶高程")
    private BigDecimal spdmEle;

    /**
     * 溢洪道进口宽度
     */
    @ApiModelProperty("溢洪道进口宽度")
    private BigDecimal spitWidth;

    /**
     * 放水设施形式
     */
    @ApiModelProperty("放水设施形式")
    private String wrfForm;

    /**
     * 溃决影响的乡镇个数
     */
    @ApiModelProperty("溃决影响的乡镇个数")
    private Integer toviNumber;

    /**
     * 溃决影响的乡镇名称
     */
    @ApiModelProperty("溃决影响的乡镇名称")
    private String toviName;

    /**
     * 开工时间
     */
    @ApiModelProperty("开工时间")
    private Date startDate;

    /**
     * 竣工验收时间
     */
    @ApiModelProperty("竣工验收时间")
    private Date compDate;

    /**
     * 归口管理部门
     */
    @ApiModelProperty("归口管理部门")
    private String admaDep;

    /**
     * 是否通过标准化管理创
     */
    @ApiModelProperty("是否通过标准化管理创")
    private String staMan;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String note;

    /**
     * 记录生效时间
     */
    @ApiModelProperty("记录生效时间")
    private Date effDate;

    /**
     * 记录生效时间
     */
    @ApiModelProperty("记录生效时间")
    private Date exprDate;

    /**
     * 行政区划代码
     */
    @ApiModelProperty("行政区划代码")
    private String areaCode;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 政府负责人用户id，关联到用户体系
     */
    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;

    /**
     * 水行政部门负责人用户id，关联到用户体系
     */
    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;

    /**
     * 主管部门负责人id，关联到用户体系
     */
    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;

    /**
     * 管理单位责任人id，关联到用户体系
     */
    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;

    /**
     * 技术负责人
     */
    @ApiModelProperty("技术负责人")
    private String technologyChargeId;

    /**
     * 巡查责任人
     */
    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private boolean delFlag;

    /**
     * 所属流域
     */
    @ApiModelProperty("所属流域")
    private String bas;

    /**
     * 设计标准（年）
     */
    @ApiModelProperty("设计标准（年）")
    private String designStandard;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 安全鉴定时间
     */
    @ApiModelProperty("安全鉴定时间")
    private String safetyTime;

    /**
     * 安全鉴定结论
     */
    @ApiModelProperty("安全鉴定结论")
    private String safetyConclusion;

    /**
     * 是否加固
     */
    @ApiModelProperty("是否加固")
    private String reinforce;
}
