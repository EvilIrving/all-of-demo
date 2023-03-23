package com.ygkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.auth.VO.request.SingleSignOnRequestVo;
import com.ygkj.entity.SysOpenUserBind;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SysOpenUserBindMapper extends BaseMapper<SysOpenUserBind> {
    @Select("select * from sys_open_user_bind where openId = #{openid} and platType = #{plattype}")
    SysOpenUserBind selectBind(SingleSignOnRequestVo vo);
}
