package com.ygkj.water.project.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BhSysAttachment;
import com.ygkj.project.model.SysFile;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-27 17:46
 * @description
 */
@DS("exchange")
public interface BhSysAttachmentMapper extends BaseMapper<BhSysAttachment> {

    @Select("select aid id, concat('https://slbzh.lucheng.gov.cn:9016/',FILEPATH) fileUrl, FILETILE fileName from bh_sys_attachment where find_in_set(aid,#{aids}) ")
    List<SysFile> findByAids(String aids);


}
