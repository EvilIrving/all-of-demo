package com.ygkj.water.project.mapper;

import com.ygkj.project.model.AttProjBuildContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fml
 * @date 2021-02-24 13:54
 * @description 工程建设内容信息 Mapper
 */
public interface AttProjBuildContentMapper {

    List<AttProjBuildContent> getProBuildContentsByProC(@Param("projCode") String projCode, @Param("type") String type);
}
