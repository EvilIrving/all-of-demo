package com.ygkj.mapper;

import com.ygkj.auth.VO.request.ArchiveLabelPageQueryVO;
import com.ygkj.auth.api.archiveLabel.model.SysArchiveLabel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description 档案标签表
 * @author hucong
 * @date 2021-01-19
 */
@Repository
public interface SysArchiveLabelMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/01/19
     **/
    int insert(SysArchiveLabel sysArchiveLabel);

    /**
     * 刪除
     * @author hucong
     * @date 2021/01/19
     **/
    int delete(int id);

    /**
     * 更新
     * @author hucong
     * @date 2021/01/19
     **/
    int update(SysArchiveLabel sysArchiveLabel);

    /**
     * 查询 根据主键 id 查询
     * @author hucong
     * @date 2021/01/19
     **/
    SysArchiveLabel load(int id);

    /**
     * 查询 分页查询
     * @author hucong
     * @date 2021/01/19
     **/
    List<SysArchiveLabel> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author hucong
     * @date 2021/01/19
     **/
    int pageListCount(int offset,int pagesize);

    /**
     * 分页查询全部
     * @param vo
     * @return
     */
    List<SysArchiveLabel> selectAll(ArchiveLabelPageQueryVO vo);
}
