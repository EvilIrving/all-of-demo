package com.ygkj.auth.VO.request;

import lombok.Data;

@Data
public class AppAchievementReqVo {
    //档案文件夹父文件夹id
    private String parentId;

    //档案类型
    private String achievementType;

    //工程类型
    private String projectType;

    //年度
    private String year;

    //关键词
    private String keyWord;

    private String flag;
}
