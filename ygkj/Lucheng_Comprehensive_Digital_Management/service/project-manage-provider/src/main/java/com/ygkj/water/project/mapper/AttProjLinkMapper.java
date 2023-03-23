package com.ygkj.water.project.mapper;

import com.ygkj.project.model.AttProjLink;

import java.util.List;

/**
 * @author fml
 * @date 2021-02-24 13:51
 * @description 工程外链信息Mapper
 */
public interface AttProjLinkMapper {

    List<AttProjLink> getProjLinksByProC(String projCode);
}
