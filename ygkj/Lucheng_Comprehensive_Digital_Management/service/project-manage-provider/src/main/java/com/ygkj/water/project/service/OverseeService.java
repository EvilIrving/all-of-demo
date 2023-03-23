package com.ygkj.water.project.service;

import com.ygkj.auth.VO.response.LoginRespVO;
import com.ygkj.project.model.ProjectOversee;
import com.ygkj.project.vo.request.OverseeCodeReqVo;
import com.ygkj.project.vo.request.OverseeReqVo;
import com.ygkj.project.vo.request.ProjectOverseeReqVo;
import com.ygkj.project.vo.response.ProjectOverseeResVo;
import com.ygkj.vo.PageVO;

public interface OverseeService {

    /**
     * 删除督办
     *
     * @param id
     */
    void del(String id);

    /**
     * 查询督办
     *
     * @param reqVo
     * @return
     */
    PageVO<ProjectOverseeResVo> page(ProjectOverseeReqVo reqVo);

    /**
     * 新增
     *
     * @param reqVo
     */
    void save(OverseeReqVo reqVo);


    /**
     * 整改
     *
     * @param oversee
     */
    void update(ProjectOversee oversee) throws Exception;

    /**
     * 发送验证码
     *
     * @param reqVo
     * @return
     */
    void sendVerificationCode(OverseeCodeReqVo reqVo) throws Exception;

    /**
     * 督办登陆
     *
     * @param workId
     * @return
     */
    LoginRespVO loginByOverseeWorkId(String workId) throws Exception;
}
