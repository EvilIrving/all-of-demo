package com.ygkj.service;

import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.archivecollection.model.SysArchiveCollection;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.vo.req.SysFileReqVo;

/**
 * @description 档案收藏表
 * @author hucong
 * @date 2021-01-19
 */
public interface SysArchiveCollectionService {

    /**
     * 新增
     */
    public int insert(SysArchiveCollection sysArchiveCollection);

    /**
     * 删除
     */
    public int delete(int id);

    /**
     * 更新
     */
    public int update(SysArchiveCollection sysArchiveCollection);

    /**
     * 根据主键 id 查询
     */
    public SysArchiveCollection load(int id);

    /**
     * 分页查询
     */
    public PageInfo<SysFile> pageList(SysFileReqVo vo);

    /**
     * 取消收藏
     * @param userId 用户id
     * @param achievementId 文件夹id
     * @param fileId 文件id
     * @return
     */
    int cancelCollection(String userId, String achievementId, String fileId);
}
