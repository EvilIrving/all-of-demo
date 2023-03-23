package com.ygkj.water.project.mapper;

import com.ygkj.project.model.WarningRecord;
import com.ygkj.project.vo.request.WarningRecordPageQueryVo;
import com.ygkj.project.vo.response.WarningRecordResVo;

import java.util.List;

/**
 * 报警记录表
 *
 * @author huangzh
 * @date 2020/12/30
 */
public interface WarningRecordMapper {

    /**
     * [新增]
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int insert(WarningRecord warningRecord);

    /**
     * [刪除]
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int delete(int id);

    /**
     * [更新]
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int update(WarningRecord warningRecord);

    /**
     * [查询] 根据主键 id 查询
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    WarningRecord load(int id);

    /**
     * [查询] 分页查询
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    List<WarningRecord> pageList(int offset, int pagesize);

    /**
     * [查询] 分页查询 count
     *
     * @author huangzh
     * @date 2020/12/30
     **/
    int pageListCount(int offset, int pagesize);

    List<WarningRecordResVo> selectByCondition(WarningRecordPageQueryVo queryVo);

}

