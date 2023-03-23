package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.entity.SingletonMap;
import com.ygkj.project.model.AttCwsBaseWcp;
import com.ygkj.project.model.CwsWaterQuality;
import com.ygkj.project.model.CwsWaterVolume;
import com.ygkj.project.vo.request.WaterWorksReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @description 农村供水工程基础信息表
 * @date 2021-08-21
 */

public interface AttCwsBaseWcpMapper extends BaseMapper<AttCwsBaseWcp> {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/21
     **/
    int insert(AttCwsBaseWcp attCwsBaseWcp);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/21
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/21
     **/
    int update(AttCwsBaseWcp attCwsBaseWcp);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/21
     **/
    AttCwsBaseWcp load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/21
     **/
    List<AttCwsBaseWcp> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/21
     **/
    int pageListCount(int offset, int pagesize);

    List<AttCwsBaseWcp> selectByCondition(WaterWorksReqVo reqVo);

    @DS("business")
    List<CwsWaterVolume> selectNewestVolume(@Param("codeList") List<String> codeList);

    @DS("business")
    List<CwsWaterQuality> selectNewestQuality(@Param("codeList") List<String> codeList);

    @DS("business")
    List<CwsWaterVolume> selectVolumeByTimeInterval(String cwsCode, Date start, Date end);

    @DS("business")
    List<CwsWaterQuality> selectQualityByTimeInterval(String cwsCode, Date start, Date end);


}
