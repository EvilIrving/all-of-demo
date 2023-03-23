package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.BmPropertyContract;
import com.ygkj.project.vo.request.BmPropertyContractReqVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BmPropertyContractMapper extends BaseMapper<BmPropertyContract> {
    //xml里的第一个和第二的查询语句的字段顺序要保持一致
    List<BmPropertyContractReqVo> loadPage(BmPropertyContractReqVo vo);
}
