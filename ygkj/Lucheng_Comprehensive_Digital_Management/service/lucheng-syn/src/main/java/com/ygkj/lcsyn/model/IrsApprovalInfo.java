package com.ygkj.lcsyn.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 投资项目办件信息与批文  - 数据来源 irs
 * @author Huangzh
 * @date 2022-06-21
 */
@Data
public class IrsApprovalInfo {

    /**
     * 办件编号 办件信息唯一标识。
     */
    @TableId
    private String approvalItemid;

    /**
     * 批复文号
     */
    private String approvalNumber;

    /**
     * 批复文件标题
     */
    private String approvalTitle;

    /**
     * 当前办理状态（待受理、 退办、 受理、 不予受理、许可办结、不予许可办结、rn暂停、注销、废止）
     */
    private String currentState;

    /**
     * 变更为当前状态的时间
     */
    private Date dealTime;

    /**
     * 审批部门代码
     */
    private String deptCode;

    /**
     * 审批部门名称
     */
    private String deptName;

    /**
     * 审批部门所属行政区划代码
     */
    private String divisionCode;

    /**
     * 返回值为带参数的一个接口地址，调用这个接口才返回真实批文rn地址，真实批文地址为有效期1天的oss文件授权访问地址，超期rn需重新调用接口地址获取oss批文临时访问地址
     */
    private String filePath;

    /**
     * 审批事项编码
     */
    private String itemCode;

    /**
     * 审批事项名称
     */
    private String itemName;

    /**
     * 项目代码
     */
    private String projectCode;

    /**
     * 批复文件有效期限
     */
    private Date validityDate;

    /**
     * 数据有效标识：0无效；1有效。
     */
    private String validityFlag;

    public IrsApprovalInfo() {
    }

}
