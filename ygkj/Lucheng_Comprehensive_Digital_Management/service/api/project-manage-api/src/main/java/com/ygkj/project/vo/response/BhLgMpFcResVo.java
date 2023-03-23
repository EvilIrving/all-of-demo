package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.project.model.SysFile;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-26 15:34
 * @description 汛前检查报告返回类
 */
@Data
public class BhLgMpFcResVo {

    /**
     * 检查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 参加人员
     */
    private String joinUid;

    /**
     * 参加人员名称
     */
    private String joinUser;

    /**
     * 隐患数量
     */
    private Integer hidCount;

    /**
     * 问题类型
     */
    private String problemType;


    /**
     * 汛前检查报告
     */
    private String aid;


    /**
     * 检查照片
     */
    private String checkPic;

    /**
     * 数据来源  1-标化 2-本地新增
     */
    private Integer sourceType;

    private String id;

    private String runActionRecord;

    private String projCheckRecord;

    private List<SysFile> checkPicList;

    private List<SysFile> aidFileList;
    private List<SysFile> runActionRecordFiles;
    private List<SysFile> projCheckRecordFiles;


}
