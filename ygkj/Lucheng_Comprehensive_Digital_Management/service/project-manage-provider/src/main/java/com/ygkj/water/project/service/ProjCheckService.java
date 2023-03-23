package com.ygkj.water.project.service;

import com.ygkj.project.model.BhLgMpAc;
import com.ygkj.project.model.BhLgMpFc;
import com.ygkj.project.model.BhLgPsSpecialCheck;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.project.vo.response.BhLgMpFcResVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author lxl
 * @create 2022-04-26 15:23
 * @description
 */
public interface ProjCheckService {


    /**
     * 汛前检查列表
     * @param reqVo
     * @return
     */
    PageVO<BhLgMpFcResVo> bhLgMpFcList(ProjCheckReqVo reqVo);

    /**
     * 导出汛前检查列表
     * @param reqVo
     * @param response
     */
    void exportLgMpFc(ProjCheckReqVo reqVo, HttpServletResponse response);

    /**
     * 日常检查列表
     * @param reqVo
     * @return
     */
    Object dailyCheckList(ProjCheckReqVo reqVo);

    /**
     * 年度检查报告
     * @param reqVo
     * @return
     */
    PageVO<BhLgMpAc> bhLgMpAcList(ProjCheckReqVo reqVo);

    /**
     * 新增汛前检查
     * @param bhLgMpFc
     * @return
     */
    Integer insertBhLgMpFc(BhLgMpFc bhLgMpFc);

    /**
     * 更新汛前检查
     * @param bhLgMpFc
     * @return
     */
    Integer updateBhLgMpFc(BhLgMpFc bhLgMpFc);

    /**
     * 删除汛前检查
     * @param id
     * @return
     */
    Integer deleteBhLgMpFc(String id);

    /**
     * 新增年度检查
     * @param bhLgMpAc
     * @return
     */
    Integer insertBhLgMpAc(BhLgMpAc bhLgMpAc);

    /**
     * 更新年度检查
     * @param bhLgMpAc
     * @return
     */
    Integer updateBhLgMpAc(BhLgMpAc bhLgMpAc);

    /**
     * 删除年度检查
     * @param id
     * @return
     */
    Integer deleteBhLgMpAc(String id);

    /**
     * 获取巡查点位等信息
     * @param id
     * @return
     */
    Map<String,Object> checkTrack(String id);


    Object specialCheckList(ProjCheckReqVo reqVo);

    Integer insertSpecialCheck(BhLgPsSpecialCheck check);

    Integer updateSpecialCheck(BhLgPsSpecialCheck check);

    Integer deleteSpecialCheck(String id);

    void exportDailyCheck(ProjCheckReqVo reqVo, HttpServletResponse response);

    /**
     * 导出年度检查
     * @param reqVo
     * @param response
     */
    void exportBhLgMpAc(ProjCheckReqVo reqVo, HttpServletResponse response);

    /**
     * 导出特别检查
     * @param reqVo
     * @param response
     */
    void exportSpecialCheck(ProjCheckReqVo reqVo, HttpServletResponse response);
}
