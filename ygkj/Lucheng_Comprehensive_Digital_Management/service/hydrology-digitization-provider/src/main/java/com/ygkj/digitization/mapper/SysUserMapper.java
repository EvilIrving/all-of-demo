package com.ygkj.digitization.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.vo.response.MonitorPersonResVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lenovo
 * 
 * @Date 2021/1/14
 */
@DS("main")
public interface SysUserMapper {
    List<MonitorPersonResVo> selectByUserIdList(@Param("userIdList") List<String> userIdList);
    List<MonitorPersonResVo> selectUserNameByUserIdList(@Param("userIds") String userIds);
    
}
