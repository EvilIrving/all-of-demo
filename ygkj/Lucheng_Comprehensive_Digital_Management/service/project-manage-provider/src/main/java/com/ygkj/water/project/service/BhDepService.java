package com.ygkj.water.project.service;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.project.vo.request.*;
import com.ygkj.project.vo.response.*;
import com.ygkj.vo.PageVO;

import java.util.List;

public interface BhDepService {

    /**
     * 工程简介
     *
     * @param seawallCode
     * @return
     */
    String introduction(String seawallCode);

    /**
     * 组织架构
     *
     * @param seawallCode
     * @return
     */
    List<JobSettingResVo> organization(String seawallCode);

    /**
     * 组织架构详细信息
     *
     * @param reqVo
     * @return
     */
    List<JobSettingDetailResVo> organizationDetail(JobSettingReqVo reqVo);

    /**
     * 人员信息
     *
     * @param reqVo
     * @return
     */
    PageVO<JobPersonResVo> listPeople(JobPersonReqVo reqVo);

    /**
     * 年度培训
     *
     * @param reqVo
     * @return
     */
    PageVO<PersonTrainResVo> personTrain(PersonTrainReqVo reqVo);

    /**
     * 管护经费
     *
     * @param reqVo
     * @return
     */
    List<ManageFundResVo> managefund(ManageFundReqVo reqVo);

    /**
     * 管理制度附件
     *
     * @param seawallCode
     * @return
     */
    List<ManageinstitutionResVo> manageinstitution(String seawallCode);

    /**
     * 项目维护
     *
     * @param reqVo
     * @return
     */
    PageVO<MfpmResVo> mfpm(MfpmReqVo reqVo);

    /**
     * 资金申报
     *
     * @param reqVo
     * @return
     */
    PageVO<MfdResVo> mfd(MfdReqVo reqVo);

    /**
     * 资金使用
     *
     * @param reqVo
     * @return
     */
    PageVO<MfuResVo> mfu(MfuReqVo reqVo);

    /**
     * 标化人员列表
     * @param reqVo
     * @return
     */
    List<BhUserResVo> userList(BhDeptUserReqVo reqVo);

    /**
     * 标化部门列表
     * @param reqVo
     * @return
     */
    List<JSONObject> deptList(BhDeptUserReqVo reqVo);
}
