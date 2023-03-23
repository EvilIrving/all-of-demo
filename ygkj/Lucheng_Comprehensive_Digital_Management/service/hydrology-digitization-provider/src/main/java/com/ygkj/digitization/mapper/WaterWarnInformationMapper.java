package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.WaterWarnInformation;
import com.ygkj.digitization.vo.request.WaterWarnInformationReqVo;
import com.ygkj.digitization.vo.response.WaterWarnInformationRespVo;
import com.ygkj.project.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description 水位预警信息表
 * @author hucong
 * @date 2021-07-13
 */
public interface WaterWarnInformationMapper {

    /**
     * 新增
     * @author hucong
     * @date 2021/07/13
     **/
    int insert(WaterWarnInformation entity);

    /**
     * 刪除
     * @author hucong
     * @date 2021/07/13
     **/
    int delete(String id);

    /**
     * 更新
     * @author hucong
     * @date 2021/07/13
     **/
    int update(WaterWarnInformation entity);

    List<WaterWarnInformationRespVo> loadPage(WaterWarnInformationReqVo vo);

    @DS("main")
    List<SysUser> findUserList(@Param("ids") String peopleIds);
}
