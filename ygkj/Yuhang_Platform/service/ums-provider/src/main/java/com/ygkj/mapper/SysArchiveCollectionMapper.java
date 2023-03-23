package com.ygkj.mapper;

import com.ygkj.auth.api.archivecollection.model.SysArchiveCollection;
import com.ygkj.auth.VO.request.SysArchiveCollectionReqVO;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.vo.req.SysFileReqVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @description 档案收藏表
 * @author hucong
 * @date 2021-01-19
 */
@Mapper
@Repository
public interface SysArchiveCollectionMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/01/19
     **/
    int insert(SysArchiveCollection sysArchiveCollection);

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
    int update(SysArchiveCollection sysArchiveCollection);

    /**
     * 查询 根据主键 id 查询
     * @author hucong
     * @date 2021/01/19
     **/
    SysArchiveCollection load(int id);

    /**
     * 查询 分页查询
     * @author hucong
     * @date 2021/01/19
     **/
    List<SysArchiveCollection> pageList(int offset,int pagesize);

    /**
     * 查询 分页查询 count
     * @author hucong
     * @date 2021/01/19
     **/
    int pageListCount(int offset,int pagesize);

    /**
     * 查找全部
     * @param vo
     * @return
     */
    List<SysFile> selectAll(SysFileReqVo vo);

    /**
     * 取消收藏
     * @param userId 用户id
     * @param achievementId 文件夹id
     * @param fileId 文件id
     * @return
     */
    int cancelCollection(String userId, String achievementId, String fileId);
}

