package com.ygkj.big.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.big.screen.model.SeawallRange;
import com.ygkj.big.screen.model.SocialResources;
import com.ygkj.big.screen.vo.request.SocialResourcesReqVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialResourcesMapper extends BaseMapper<SocialResources> {
    @Select("select * from seawall_range where prcd = #{prcd} and type = '3'")
    SeawallRange querySeawallRange(SocialResourcesReqVo reqVo);
}
