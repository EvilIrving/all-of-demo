package com.ygkj.water.project.mapper;

import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.response.AttReservoirArchivesResVo;

import java.util.List;

/**
 * @author lxl
 * @description 大中型水库档案 Mapper
 */
public interface AttReservoirArchivesMapper {


    /**
     * 查询 大中型水库档案应急预案文件 当前年份对应数量
     *
     * @return
     */
    Integer reservoirArchivesFileCount();

    /**
     * 查询 大中型水库 的文件内容 是否已存在以及信息
     *
     * @return
     */
    List<AttReservoirArchivesResVo> reservoirArchivesAndResBase();

    /**
     * 查询 水库对应的文件列表
     *
     * @return
     */
    List<SysFile> reservoirArchivesFileList();
}
