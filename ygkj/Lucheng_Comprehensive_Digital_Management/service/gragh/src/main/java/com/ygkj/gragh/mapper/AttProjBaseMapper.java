package com.ygkj.gragh.mapper;


import com.ygkj.gragh.model.AttProjBase;
import com.ygkj.gragh.model.AttResBase;
import com.ygkj.gragh.vo.request.SynWaterRequestVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Huangzh
 * @description 工程建设项目基础信息表
 * @date 2020-12-07
 */
public interface AttProjBaseMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int insert(AttProjBase attProjBase);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int update(AttProjBase attProjBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    AttProjBase load(int id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    List<AttProjBase> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/07
     **/
    int pageListCount(int offset, int pagesize);


    List<AttProjBase> selectByCondition(SynWaterRequestVo waterRequestVo);

    @Select("SELECT * FROM att_res_base WHERE del_flag = 0 AND eng_grad < 4")
    List<AttResBase> selectBigAndMiddleReservoir();

    @Select("SELECT waga_code FROM att_waga_base WHERE wrp_waga = 1 AND del_flag = 0 AND (start_lat IS NOT NULL AND start_long IS NOT NULL)")
    List<String> selectWrpWagaCodes();

    List<AttResBase> selectReservoirs(@Param("engScal") String engScal, @Param("resCodes") String resCodes, @Param("mainStcds") String mainStcds);
}

