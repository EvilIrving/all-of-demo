package com.ygkj.mapper;

import com.ygkj.entity.SysFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 15:43
 * @Description:
 */
@Repository
public interface SysFileMapper {

    /**
     * 新增
     * @author Huangzh
     * @date 2020/09/15
     **/
    int insert(SysFile sysFile);

    /**
     * 刪除
     * @author Huangzh
     * @date 2020/09/15
     **/
    int delete(int id);

    /**
     * 更新
     * @author Huangzh
     * @date 2020/09/15
     **/
    int update(SysFile sysFile);

    int batchLogicDelete(String ids);

    /**
     * 查询 根据主键 id 查询
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysFile> load(String id);

    /**
     * 查询 分页查询
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysFile> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author Huangzh
     * @date 2020/09/15
     **/
    int pageListCount(int offset,int pagesize);

    List<SysFile> list(List<String> list);
}
