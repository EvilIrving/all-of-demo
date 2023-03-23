package com.ygkj.mapper;

import com.ygkj.auth.VO.request.SysThirdPartySystemQueryVO;
import com.ygkj.auth.api.thirdPartSystem.model.SysThirdPartySystem;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/10/27 10:34
 * @Description:
 */
public interface SysThirdPartySystemMapper {
    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/10/27
     **/
    int insert(SysThirdPartySystem sysThirdPartySystem);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/10/27
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/10/27
     **/
    int update(SysThirdPartySystem sysThirdPartySystem);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/10/27
     **/
    SysThirdPartySystem load(String id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/10/27
     **/
    List<SysThirdPartySystem> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/10/27
     **/
    int pageListCount(int offset, int pagesize);

    List<SysThirdPartySystem> selectAll(SysThirdPartySystemQueryVO queryVO);

}
