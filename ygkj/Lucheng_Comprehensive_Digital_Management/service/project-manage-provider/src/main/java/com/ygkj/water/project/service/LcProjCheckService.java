package com.ygkj.water.project.service;

import com.ygkj.project.model.BhLgMpAf;
import com.ygkj.project.model.BhLgMpSp;
import com.ygkj.project.vo.request.BhLgMpAfReqVo;
import com.ygkj.project.vo.request.BhLgMpSpReqVo;
import com.ygkj.project.vo.request.ProjCheckReqVo;
import com.ygkj.vo.PageVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @author lxl
 * @create 2022-04-28 9:27
 * @description 鹿城本地数据 工程检查
 */
public interface LcProjCheckService {

    /**
     * 汛后检查列表
     * @param reqVo
     * @return
     */
    PageVO<BhLgMpAf> mpAfList(ProjCheckReqVo reqVo);

    /**
     * 插入汛后检查
     * @param bhLgMpAf
     * @return
     */
    Integer mpAfInsert(BhLgMpAfReqVo bhLgMpAf);

    /**
     * 更新汛后检查
     * @param bhLgMpAf
     * @return
     */
    Integer mpAfUpdate(BhLgMpAfReqVo bhLgMpAf);

    /**
     * 删除汛后检查
     * @param id
     * @return
     */
    Integer mpAfDelete(String id);

    /**
     * 特别检查列表
     * @param reqVo
     * @return
     */
    PageVO<BhLgMpSp> mpSpList(ProjCheckReqVo reqVo);

    /**
     * 新增特别检查
     * @param bhLgMpSp
     * @return
     */
    Integer mpSpInsert(BhLgMpSpReqVo bhLgMpSp);

    /**
     * 更新特别检查
     * @param bhLgMpSp
     * @return
     */
    Integer mpSpUpdate(BhLgMpSpReqVo bhLgMpSp);

    /**
     * 删除特别检查
     * @param id
     * @return
     */
    Integer mpSpDelete(String id);

    void mpAfExport(ProjCheckReqVo reqVo, HttpServletResponse response);

    void mpSpExport(ProjCheckReqVo reqVo, HttpServletResponse response);
}
