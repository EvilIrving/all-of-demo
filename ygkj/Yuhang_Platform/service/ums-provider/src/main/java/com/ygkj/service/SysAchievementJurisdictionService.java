package com.ygkj.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.ygkj.entity.SysAchievementJurisdiction;
import com.ygkj.vo.req.SysAchievementJurisdictionReqVo;

import java.util.List;
import java.util.Map;

/**
 * @description 文档权限表
 * @author hucong
 * @date 2021-01-20
 */
public interface SysAchievementJurisdictionService {

    /**
     * 新增
     */
    public int insert(SysAchievementJurisdiction sysAchievementJurisdiction);

    /**
     * 删除
     */
    public int delete(int id);

    /**
     * 更新
     */
    public int update(SysAchievementJurisdiction sysAchievementJurisdiction);

    /**
     * 根据主键 id 查询
     */
    public SysAchievementJurisdiction load(int id);

    /**
     * 分页查询
     */
    public PageInfo<SysAchievementJurisdiction> pageList(SysAchievementJurisdictionReqVo vo);

    /**
     * 根据文件夹id和操作类型查找数据
     * @param achievementId
     * @param type 权限类型 1:查看权限 2:上传权限 3:删除权限 4:下载权限 5:更新权限
     * @return
     */
    List<com.ygkj.auth.api.achievement.model.SysAchievementJurisdiction> findDataByAchievementIdAndType(String achievementId, String type);

    /**
     * 根据文档id检查用户权限
     * @param achievementId
     * @return
     */
    JSONObject checkPermissions(String achievementId);
}

