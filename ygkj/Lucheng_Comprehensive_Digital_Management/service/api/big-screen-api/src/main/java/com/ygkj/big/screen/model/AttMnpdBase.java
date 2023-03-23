package com.ygkj.big.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-29 15:08
 * @description 山塘基础信息表
 */
@Data
@ApiModel("山塘基础信息表 ")
public class AttMnpdBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("山塘代码")
    private String mnpdCode;

    @ApiModelProperty("山塘名称")
    private String mnpdName;

    @ApiModelProperty("坝顶中点经度")
    private Double dtMidLong;

    @ApiModelProperty("坝顶中点维度")
    private Double dtMidLat;

    @ApiModelProperty("所在乡镇（街道）名称")
    private String strName;

    @ApiModelProperty("地区")
    private String dscd;

    @ApiModelProperty("所在村名称")
    private String vilName;

    @ApiModelProperty("主要功能")
    private String zygn;

    @ApiModelProperty("所有权人（管理责任主体）")
    private String syqr;

    @ApiModelProperty("所有权人电话")
    private String syqrdh;

    @ApiModelProperty("产权证发证机关、编号")
    private String cqzfzjgbh;

    @ApiModelProperty("巡查员姓名")
    private String xcyxm;

    @ApiModelProperty("巡查员电话")
    private String xcydh;

    @ApiModelProperty("主管部门")
    private String zgbm;

    @ApiModelProperty("是否进行山塘整治")
    private String sfjxstzz;

    @ApiModelProperty("设计单位")
    private String sjdw;

    @ApiModelProperty("校核洪水（p）")
    private Double zlcd;

    @ApiModelProperty("校核洪水（水位）")
    private Double jhhsP;

    @ApiModelProperty("校核洪水（容积）")
    private Double jhhsRj;

    @ApiModelProperty("设计洪水（p）")
    private Double sjhsP;

    @ApiModelProperty("设计洪水（水位）")
    private Double sjhsSw;

    @ApiModelProperty("设计洪水（容积）")
    private Double sjhsRj;

    @ApiModelProperty("正常蓄水位（水位）")
    private Double zcsxswSw;

    @ApiModelProperty("正常蓄水位（容积）")
    private Double zcxswRj;

    @ApiModelProperty("影响区域（km2）")
    private Double yxqy;

    @ApiModelProperty("影响区域内村庄个数（个）")
    private Integer yxqynczgs;

    @ApiModelProperty("影响人口（人）")
    private Integer yxrk;

    @ApiModelProperty("日供水能力（t）")
    private Double rgsnl;

    @ApiModelProperty("灌溉面积（亩）")
    private Double ggmj;

    @ApiModelProperty("坝型")
    private String bx;

    @ApiModelProperty("背水坡脚高程（m）")
    private Double bspgc;

    @ApiModelProperty("迎水坡坡比")
    private String ysppg;

    @ApiModelProperty("背水坡坡比")
    private String bsppg;

    @ApiModelProperty("是否兼做坝顶公路")
    private String sfjzbdgl;

    @ApiModelProperty("泄洪建筑物-溢流宽度（m）")
    private Double xhjzwYlkd;

    @ApiModelProperty("输水建筑物-型式")
    private String ssjzwXs;

    @ApiModelProperty("输水建筑物-长度（m）")
    private Double ssjzwCd;

    @ApiModelProperty("输水建筑物-断面尺寸（m）")
    private Double ssjzwDmcc;

    @ApiModelProperty("是否有金属结构")
    private String sfyjsjg;

    @ApiModelProperty("是否存在渗漏")
    private String sfczsl;

    @ApiModelProperty("渗漏位置")
    private String slwz;

    @ApiModelProperty("管理房")
    private String glf;

    @ApiModelProperty("管理房面积（m2）")
    private Double glfmj;

    @ApiModelProperty("管理房位置")
    private String glfwz;

    @ApiModelProperty("是否完成标准化管理")
    private String sfwcbzhgl;

    @ApiModelProperty("观测设施")
    private String gcss;

    @ApiModelProperty("文件名称")
    private String filenam;

    @ApiModelProperty("照片-整体面貌")
    private String picZtmm;

    @ApiModelProperty("照片-坝顶面貌")
    private String picBdmm;

    @ApiModelProperty("照片-上游概况")
    private String picSygk;

    @ApiModelProperty("照片-下游面貌")
    private String picXymm;

    @ApiModelProperty("照片-溢洪道")
    private String picYhd;

    @ApiModelProperty("照片-放水设施")
    private String picFsss;

    @ApiModelProperty("照片-排水棱体")
    private String picPslt;

    @ApiModelProperty("工程任务")
    private String projectTask;

    @ApiModelProperty("大坝类型按材料分")
    private String damTypeByMaterials;

    @ApiModelProperty("建成时间(年/月)")
    private String completeTime;

    @ApiModelProperty("整治开始时间")
    private String remstTime;

    @ApiModelProperty("整治完成时间")
    private String remfiTime;

    @ApiModelProperty("坝址以上集雨面积")
    private Double rcdsArea;

    @ApiModelProperty("总容积")
    private Double totalVol;

    @ApiModelProperty("山塘类别")
    private String mnpdCate;

    @ApiModelProperty("山塘功能")
    private String mnpdFunc;

    @ApiModelProperty("坝顶高程")
    private Double dmtpEle;

    @ApiModelProperty("最大坝高")
    private Double madmHeight;

    @ApiModelProperty("坝顶宽度")
    private Double madmWidth;

    @ApiModelProperty("坝长")
    private Double damLength;

    @ApiModelProperty("主要泄洪建筑物型式")
    private String dscncltp;

    @ApiModelProperty("溢洪道堰顶高程")
    private Double spdmEle;

    @ApiModelProperty("溢洪道进口宽度")
    private Double spitWidth;

    @ApiModelProperty("放水设施形式")
    private String wrfForm;

    @ApiModelProperty("溃决影响的乡镇个数")
    private Integer toviNumber;

    @ApiModelProperty("溃决影响的乡镇名称")
    private String toviName;

    @ApiModelProperty("开工时间")
    private String startDate;

    @ApiModelProperty("竣工验收时间")
    private String compDate;

    @ApiModelProperty("归口管理部门")
    private String admaDep;

    @ApiModelProperty("是否通过标准化管理创")
    private String staMan;

    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("记录生效时间")
    private String effDate;

    @ApiModelProperty("记录生效时间")
    private String exprDate;

    @ApiModelProperty("行政区划代码")
    private String areaCode;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("政府负责人用户id，关联到用户体系")
    private String govermentChargeId;

    @ApiModelProperty("水行政部门负责人用户id，关联到用户体系")
    private String waterDepartmentChargeId;

    @ApiModelProperty("主管部门负责人id，关联到用户体系")
    private String competentDepartmentChargeId;

    @ApiModelProperty("管理单位责任人id，关联到用户体系")
    private String manageDepartmentChargeId;

    @ApiModelProperty("技术负责人")
    private String technologyChargeId;

    @ApiModelProperty("巡查责任人")
    private String patrolChargeId;

    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("设计标准（年）")
    private String designStandard;
}
