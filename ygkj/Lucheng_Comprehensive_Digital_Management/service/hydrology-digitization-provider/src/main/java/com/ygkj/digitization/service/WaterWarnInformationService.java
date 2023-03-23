package com.ygkj.digitization.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.digitization.model.WaterWarnInformation;
import com.ygkj.digitization.vo.request.WaterWarnInformationReqVo;
import com.ygkj.digitization.vo.response.WaterWarnInformationRespVo;
import com.ygkj.project.model.SysUser;
import com.ygkj.vo.PageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaterWarnInformationService {

    /**
     * 新增
     */
    int insert(WaterWarnInformationReqVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(WaterWarnInformationReqVo vo);

    /**
     * 分页查找
     * @param vo
     * @return
     */
    PageVO<WaterWarnInformationRespVo> loadPage(WaterWarnInformationReqVo vo);


    List<SysUser> findUserList(String peopleIds);

    /**
     * 发送预警信息（短信 or 传真）
     * @param vo
     */
    void sendMessage(WaterWarnInformationReqVo vo) throws Exception;
}
