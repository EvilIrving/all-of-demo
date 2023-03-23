package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.ProjMicroImage;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.ProjMicroImageReqVo;
import com.ygkj.project.vo.response.ProjMicroImageRespVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hucong
 * @description 工程监督-微影像管理表
 * @date 2021-07-09
 */
public interface ProjMicroImageMapper {

    /**
     * 新增
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int insert(ProjMicroImage projMicroImage);

    /**
     * 刪除
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author hucong
     * @date 2021/07/09
     **/
    int update(ProjMicroImage projMicroImage);

    List<ProjMicroImageRespVo> findList(ProjMicroImageReqVo vo);

    List<Integer> yearList(String type);

    @DS("main_db")
    List<SysFile> fileList(@Param("ids") String ids);

    List<ProjMicroImageRespVo> findDataByMonth(@Param("month") String month);
}
