package com.ygkj.river.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.SingletonMap;
import com.ygkj.river.model.AttRivSegBase;
import com.ygkj.river.vo.request.RiverHealthStatReqVo;
import com.ygkj.river.vo.request.RiverQueryReqVo;
import com.ygkj.river.vo.response.RiverBaseInfoResVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @description 另一个河段基础信息表
 * @date 2021-08-07
 */
public interface AttRivSegBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/08/07
     **/
    int insert(AttRivSegBase attRivSegBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/08/07
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/08/07
     **/
    int update(AttRivSegBase attRivSegBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/08/07
     **/
    AttRivSegBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author xq
     * @date 2021/08/07
     **/
    List<AttRivSegBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author xq
     * @date 2021/08/07
     **/
    int pageListCount(int offset, int pagesize);

    List<RiverBaseInfoResVo> selectByCondition(RiverQueryReqVo reqVo);

    @Select("select area_code as `key`,count(1) as `value` from att_riv_seg_base where del_flag=0 group by area_code")
    List<SingletonMap<String, Long>> countByAreaCode();

    @Select("select concat(level,'') as `key`,count(1) as `value` from att_riv_seg_base where del_flag=0 group by level")
    List<SingletonMap<String, Long>> countByLevel();

    @Select("select id ,none_four_chaos,river_manage_well,none_illegal_river_create,shoreline_occupy_balance,river_proj_tallied,public_satisfy,health_state " +
            " from att_riv_seg_base " +
            " where del_flag=0 and find_in_set(id,#{ids})")
    List<RiverHealthStatReqVo> selectHealthStat(String ids);

    /**
     * 河流分析按行政区划统计-数量
     *
     * @return
     */
    List<JSONObject> rivSegStaticByArea();

    /**
     * 河流分析按行政区划统计-长度
     *
     * @return
     */
    List<JSONObject> rivSegStaticLengthByArea();

    /**
     * 河流分析按河段等级统计-数量
     *
     * @return
     */
    List<JSONObject> rivSegStaticByAreaLevel();

    /**
     * 河流分析按河段等级统计-行政区划
     *
     * @return
     */
    List<JSONObject> rivSegStaticLengthByAreaLevel();

    /**
     * 河流分析按健康分析统计
     *
     * @return
     */
    List<JSONObject> rivSegStaticByHealthState();

    /**
     * 河流分布——C位统计
     *
     * @return
     */
    JSONObject rivSegCStatic();
}
