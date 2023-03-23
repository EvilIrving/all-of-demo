package com.ygkj.water.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ygkj.project.model.IrsInvestProjectBase;
import com.ygkj.project.model.IrsInvestProjectConstruction;
import com.ygkj.project.model.IrsInvestProjectFinance;
import com.ygkj.project.model.IrsInvestProjectPlan;
import com.ygkj.project.vo.request.InvestProjectReqVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-21 16:11
 * @description
 */
public interface IrsInvestProjectBaseMapper extends BaseMapper<IrsInvestProjectBase> {


    List<IrsInvestProjectBase> loadPage(InvestProjectReqVo vo);

    @Select("select * from irs_invest_project_plan where PROJECT_BASE_CODE = #{projectBaseCode}")
    List<IrsInvestProjectPlan> queryInvestProjectPlan(IrsInvestProjectPlan plan);

    int saveInvestProjectPlan(IrsInvestProjectPlan plan);

    int editInvestProjectPlan(IrsInvestProjectPlan plan);

    @Select("select * from irs_invest_project_finance where PROJECT_BASE_CODE = #{projectBaseCode}")
    List<IrsInvestProjectFinance> queryInvestProjectFinance(IrsInvestProjectFinance finance);

    int saveInvestProjectFinance(IrsInvestProjectFinance finance);

    int editInvestProjectFinance(IrsInvestProjectFinance finance);

    @Select("select * from irs_invest_project_construction where PROJECT_BASE_CODE = #{projectBaseCode}")
    List<IrsInvestProjectConstruction> queryInvestProjectConstruction(IrsInvestProjectConstruction construction);

    int saveInvestProjectConstruction(IrsInvestProjectConstruction construction);

    int editInvestProjectConstruction(IrsInvestProjectConstruction construction);
}
