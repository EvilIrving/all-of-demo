package com.ygkj.service;

import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.archiveLabel.model.SysArchiveLabel;
import com.ygkj.auth.VO.request.ArchiveLabelPageQueryVO;
import com.ygkj.vo.resp.PageVO;

/**
 * @description 档案标签表
 * @author hucong
 * @date 2021-01-19
 */
public interface SysArchiveLabelService {

    /**
     * 新增
     */
    public int insert(SysArchiveLabel sysArchiveLabel);

    /**
     * 删除
     */
    public int delete(int id);

    /**
     * 更新
     */
    public int update(SysArchiveLabel sysArchiveLabel);

    /**
     * 根据主键 id 查询
     */
    public SysArchiveLabel load(int id);

    /**
     * 分页查询
     */
    public PageInfo<SysArchiveLabel> pageList(ArchiveLabelPageQueryVO vo);

}
