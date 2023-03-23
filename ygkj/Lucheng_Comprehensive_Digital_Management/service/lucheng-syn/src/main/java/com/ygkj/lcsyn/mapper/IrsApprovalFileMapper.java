package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.IrsApprovalFile;
import com.ygkj.lcsyn.model.IrsApprovalInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lxl
 * @create 2022-07-08 9:25
 * @description
 */
@DS("basic")
public interface IrsApprovalFileMapper extends BaseMapper<IrsApprovalFile> {

    void replaceIrsApprovalFile(@Param("list") List<IrsApprovalFile> irsApprovalFileList);

}
