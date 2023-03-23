package com.ygkj.water.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ygkj.project.model.IrsInvestProjectBase;
import com.ygkj.project.model.IrsInvestProjectConstruction;
import com.ygkj.project.model.IrsInvestProjectFinance;
import com.ygkj.project.model.IrsInvestProjectPlan;
import com.ygkj.project.vo.request.InvestProjectReqVo;

import java.util.List;

/**
 * @author lxl
 * @create 2022-06-21 16:08
 * @description
 */
public interface IrsInvestProjectBaseService extends IService<IrsInvestProjectBase> {


    boolean editInvestProject(InvestProjectReqVo vo);

    List<IrsInvestProjectBase> loadPage(InvestProjectReqVo vo);

    List<IrsInvestProjectPlan> queryInvestProjectPlan(IrsInvestProjectPlan plan);

    int editInvestProjectPlan(IrsInvestProjectPlan plan);

    List<IrsInvestProjectFinance> queryInvestProjectFinance(IrsInvestProjectFinance finance);

    int editInvestProjectFinance(IrsInvestProjectFinance finance);

    List<IrsInvestProjectConstruction> queryInvestProjectConstruction(IrsInvestProjectConstruction construction);

    int editInvestProjectConstruction(IrsInvestProjectConstruction construction);
}

