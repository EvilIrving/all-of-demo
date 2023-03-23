package com.ygkj.lcsyn.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.lcsyn.model.IrsApprovalInfo;
import com.ygkj.lcsyn.model.RealTyphoonData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-21 9:33
 * @description
 */
@DS("basic")
public interface IrsApprovalInfoMapper extends BaseMapper<IrsApprovalInfo> {


    void replaceIrsApprovalInfo(@Param("list") List<IrsApprovalInfo> irsApprovalInfos);

}
