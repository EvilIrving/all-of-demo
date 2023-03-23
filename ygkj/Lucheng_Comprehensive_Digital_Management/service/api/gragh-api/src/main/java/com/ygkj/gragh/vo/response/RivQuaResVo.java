package com.ygkj.gragh.vo.response;

import com.ygkj.gragh.model.AttRivQuaStBase;
import com.ygkj.utils.OptionalString;
import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/8/5
 */
@Data
public class RivQuaResVo extends AttRivQuaStBase {
    private static final long serialVersionUID = -6313071136538331325L;

    /**
     * 经度
     */
    @ApiModelProperty("经度")
    String longitude;

    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    String latitude;

    /**
     * 上传时刻
     */
    @ApiModelProperty("上传时刻")
    private Date tm;

    /**
     * 透明度
     */
    @ApiModelProperty("透明度")
    private String transparent;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadDate;

    /**
     * 锌
     */
    @ApiModelProperty("锌")
    private String zn;

    /**
     * ph
     */
    @ApiModelProperty("ph")
    private String ph;

    /**
     * 五日生化需氧量
     */
    @ApiModelProperty("五日生化需氧量")
    private String fiveDayCod;

    /**
     * 硒
     */
    @ApiModelProperty("硒")
    private String se;

    /**
     * 镉
     */
    @ApiModelProperty("镉")
    private String cd;

    /**
     * 挥发酚
     */
    @ApiModelProperty("挥发酚")
    private String cr;

    /**
     * 氰化物
     */
    @ApiModelProperty("氰化物")
    private String cyanide;

    /**
     * 化学需氧量
     */
    @ApiModelProperty("化学需氧量")
    private String cod;

    /**
     * 汞
     */
    @ApiModelProperty("汞")
    private String hg;

    /**
     * 石油类
     */
    @ApiModelProperty("石油类")
    private String petro;

    /**
     * 阴离子表面活性剂
     */
    @ApiModelProperty("阴离子表面活性剂")
    private String surfactant;

    /**
     * 硫化物
     */
    @ApiModelProperty("硫化物")
    private String sulfide;

    /**
     * 铅
     */
    @ApiModelProperty("铅")
    private String pb;

    /**
     * 氟化物
     */
    @ApiModelProperty("氟化物")
    private String fluoride;

    /**
     * 铜
     */
    @ApiModelProperty("铜")
    private String cu;

    /**
     * 挥发酚
     */
    @ApiModelProperty("挥发酚")
    private String volatilePhenol;

    /**
     * 溶解氧
     */
    @ApiModelProperty("溶解氧")
    private String dO;

    /**
     * 高锰酸盐
     */
    @ApiModelProperty("高锰酸盐")
    private String codmn;

    /**
     * 砷
     */
    @ApiModelProperty("砷")
    private String arsenic;

    /**
     * 总磷
     */
    @ApiModelProperty("总磷")
    private String tp;

    /**
     * 氨氮
     */
    @ApiModelProperty("氨氮")
    private String nh3N;

    @ApiModelProperty("水质等级分为：Ⅰ类(1)、Ⅱ类(2)、Ⅲ类(3)、Ⅳ类(4)、Ⅴ类(5)、劣Ⅴ类(6)")
    Integer qualityLevel;

    @ApiModelProperty("行政区划")
    String areaName;

    @ApiModelProperty("是否恶化,true为恶化")
    boolean qualityWorse;

    public void buildCoordinate() {
        OptionalString.ofBlank(super.getLocation()).ifBlankPresent(o -> {
            String tempStr = null;
            if (o instanceof String && (tempStr = (String) o).contains(StringUtils.JOIN_DELIMITER)) {
                String[] tempStrArray = tempStr.split(StringUtils.JOIN_DELIMITER);
                if (tempStrArray.length >= 2) {
                    longitude = tempStrArray[0];
                    latitude = tempStrArray[1];
                }
            }
        });
    }

    /**
     * 根据上个时刻的值判断水质好转或恶化
     * 高锰酸盐指数,氨氮,总磷,值增加，溶解氧，降低 就为恶化，否则就是好转
     *
     * @param lastRecord
     */
    public void computeQualityWorse(RivQuaResVo lastRecord) {
        if (lastRecord == null) {
            return;
        }
        boolean codmnWorse = false;
        try {
            codmnWorse = Double.compare(Double.parseDouble(getCodmn()), Double.parseDouble(lastRecord.getCodmn())) > 0;
        } catch (Exception ignored) {

        }
        boolean nh3NWorse = false;
        try {
            nh3NWorse = Double.compare(Double.parseDouble(getNh3N()), Double.parseDouble(lastRecord.getNh3N())) > 0;
        } catch (Exception ignored) {

        }
        boolean tpWorse = false;
        try {
            tpWorse = Double.compare(Double.parseDouble(getTp()), Double.parseDouble(lastRecord.getTp())) > 0;
        } catch (Exception ignored) {

        }
        boolean dOWorse = false;
        try {
            dOWorse = Double.compare(Double.parseDouble(getDO()), Double.parseDouble(lastRecord.getDO())) < 0;
        } catch (Exception ignored) {

        }
        qualityWorse = !codmnWorse && !nh3NWorse && !tpWorse && !dOWorse;
    }

}
