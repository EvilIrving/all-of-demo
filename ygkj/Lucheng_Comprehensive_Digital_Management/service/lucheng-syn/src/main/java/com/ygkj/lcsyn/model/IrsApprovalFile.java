package com.ygkj.lcsyn.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description 投资项目批文信息
 * @author lxl
 * @date 2022-07-08
 */
@Data
public class IrsApprovalFile {

    /**
     * 批复日期
     */
    private String approvalDate;

    /**
     * 批复文件信息id
     */
    private String approvalDocId;

    /**
     * 办件编号
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
     * 批复文件地址
     */
    private String filePath;

    /**
     * 批复文件有效日期
     */
    private Date validityDate;

    /**
     * 有效状态
     */
    private String validityFlag;

    public IrsApprovalFile() {
    }

}
