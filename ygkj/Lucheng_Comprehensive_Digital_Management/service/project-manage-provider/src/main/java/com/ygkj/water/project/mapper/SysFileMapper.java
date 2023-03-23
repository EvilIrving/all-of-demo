package com.ygkj.water.project.mapper;

import com.ygkj.project.model.SysFile;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysFileMapper {

    /**
     * 列表查询
     *
     * @param fileIdList
     * @return
     */
    List<SysFile> listFile(List<String> fileIdList);


    /**
     * 查询 根据主键 id 查询
     **/
    SysFile load(String id);

    @Select("SELECT * FROM sys_file WHERE FIND_IN_SET(id , #{id}) AND deleted = 0")
    List<SysFile> loadByIds(@Param("id") String id);
}
