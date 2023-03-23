package com.ygkj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ygkj.auth.api.archiveLabel.model.SysArchiveLabel;
import com.ygkj.mapper.SysArchiveLabelMapper;
import com.ygkj.service.SysArchiveLabelService;
import com.ygkj.utils.PageUtils;
import com.ygkj.auth.VO.request.ArchiveLabelPageQueryVO;
import com.ygkj.vo.resp.PageVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description 档案标签表
 * @author hucong
 * @date 2021-01-19
 */
@Service
public class SysArchiveLabelServiceImpl implements SysArchiveLabelService {

    @Resource
    private SysArchiveLabelMapper sysArchiveLabelMapper;


    @Override
    public int insert(SysArchiveLabel sysArchiveLabel) {
        int i = sysArchiveLabelMapper.insert(sysArchiveLabel);
        return i;
    }


    @Override
    public int delete(int id) {
        int ret = sysArchiveLabelMapper.delete(id);
        return ret;
    }


    @Override
    public int update(SysArchiveLabel sysArchiveLabel) {
        int ret = sysArchiveLabelMapper.update(sysArchiveLabel);
        return ret;
    }


    @Override
    public SysArchiveLabel load(int id) {
        return sysArchiveLabelMapper.load(id);
    }


    @Override
    public PageInfo<SysArchiveLabel> pageList(ArchiveLabelPageQueryVO vo) {
        if (!vo.isFetchAll()){
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        List<SysArchiveLabel> selectAll = sysArchiveLabelMapper.selectAll(vo);
        return new PageInfo(selectAll);
    }

}
