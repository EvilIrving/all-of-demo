package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/10
 */
@Data
@ApiModel("河段健康评价")
public class RiverHealthStatReqVo {

    /**
     * 河段编号
     */
    @ApiModelProperty("河段编号")
    private Integer id;

    /**
     * 河湖“四乱”清零：是/否
     */
    @ApiModelProperty("河湖“四乱”清零：1是/0否")
    Integer noneFourChaos;

    /**
     * 河湖管护到位：是/否
     */
    @ApiModelProperty("河湖管护到位：是/否")
    Integer riverManageWell;

    /**
     * 无乱点河道创建：是/否
     */
    @ApiModelProperty("无乱点河道创建：1是/0否")
    Integer noneIllegalRiverCreate;

    /**
     * 水域岸线占补平衡：是/否
     */
    @ApiModelProperty("水域岸线占补平衡：1是/0否")
    Integer shorelineOccupyBalance;

    /**
     * 涉河建设项目批建相符：是/否
     */
    @ApiModelProperty("涉河建设项目批建相符：1是/0否")
    Integer riverProjTallied;

    /**
     * 公众满意：是/否
     */
    @ApiModelProperty("公众满意：1是/0否")
    Integer publicSatisfy;

    /**
     * 1绿码、2黄码、3红码；5个“是”为绿码，3~4个“是”为黄码，0~2个“是”为红码
     */
    @ApiModelProperty("1绿码、2黄码、3红码；5个“是”为绿码，3~4个“是”为黄码，0~2个“是”为红码")
    Integer healthState;


    public void buildHealthState() {
        if (noneFourChaos == null) {
            noneFourChaos = 1;
        }
        if (noneIllegalRiverCreate == null) {
            noneIllegalRiverCreate = 1;
        }
        if (shorelineOccupyBalance == null) {
            shorelineOccupyBalance = 1;
        }
        if (riverProjTallied == null) {
            riverProjTallied = 1;
        }
        if (publicSatisfy == null) {
            publicSatisfy = 1;
        }
        int num = 0;
        if (noneFourChaos == 1) {
            num++;
        }
        if (noneIllegalRiverCreate == 1) {
            num++;
        }
        if (shorelineOccupyBalance == 1) {
            num++;
        }
        if (riverProjTallied == 1) {
            num++;
        }
        if (publicSatisfy == 1) {
            num++;
        }
        if (num <= 2) {
            healthState = 3;
        } else if (num > 2 && num <= 4) {
            healthState = 2;
        } else if (num >= 5) {
            healthState = 1;
        }
    }


}
