package com.ygkj.platform.management.vo.request;

import com.ygkj.enums.Type;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;

@Data
@ApiModel
public class SysThresholdAddOrUpdateVo {

    @ApiModelProperty("id,更新的时候带上")
    private Long id;

    @ApiModelProperty("阈值类型：登录/菜单")
    @NotBlank(message = "请指定设定阈值的类型！")
    private String thresholdType;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @NotBlank(message = "请指定一个用户！")
    private String userId;

    /**
     * 部门id
     */
    @ApiModelProperty("部门id")
    @NotBlank(message = "请指定一个部门！")
    private String deptId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    @NotBlank(message = "请指定用户名称！")
    private String username;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @NotBlank(message = "请指定部门名称！")
    private String department;

    /**
     * 菜单id
     */
    @ApiModelProperty("菜单id，当类型为菜单的时候需要传参")
    private String menuId;

    /**
     * 频率
     */
    @ApiModelProperty("频率")
    @NotBlank(message = "请输入频率！")
    private Integer frequency;

    /**
     * 频率单位：每月/每日
     */
    @ApiModelProperty("频率单位：每月/每日")
    @NotBlank(message = "请输入频率单位！")
    private String unit;

    /**
     * 提醒方式：浙政钉/短信/全部
     */
    @ApiModelProperty("提醒方式：浙政钉/短信/全部")
    @NotBlank(message = "必须指定一种提醒方式！")
    private String notifyMode;

    public boolean checkParameters() {
        if (thresholdType.equals(ThresholdType.MENU.type()) && StringUtils.isBlank(this.menuId)) {
            return false;
        }
        return Arrays.stream(NotifyMode.values()).anyMatch(mode -> mode.type().equals(this.notifyMode))
                && Arrays.stream(Unit.values()).anyMatch(unit -> unit.type().equals(this.unit))
                && Arrays.stream(ThresholdType.values()).anyMatch(type -> type.type().equals(this.thresholdType));
    }

    public enum ThresholdType implements Type {
        LOGIN("登录"), MENU("菜单");

        private String type;

        ThresholdType(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }
    }

    public enum NotifyMode implements Type {

        GOVERNMENT_DING_DING("浙政钉"),
        SMS("短信"),
        ALL("全部");

        private String type;

        NotifyMode(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }
    }

    public enum Unit implements Type {
        EACH_MONTH("每月"),
        EACH_DAY("每日");

        private String type;

        Unit(String type) {
            this.type = type;
        }

        @Override
        public String type() {
            return type;
        }
    }
}
