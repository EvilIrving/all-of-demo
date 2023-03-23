package com.ygkj.digitization.service;

import com.ygkj.digitization.vo.request.WarnUserReqVo;
import com.ygkj.digitization.vo.response.WarnUserRespVo;
import com.ygkj.vo.PageVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @description 预警人员表
 * @author hucong
 * @date 2021-07-26
 */
public interface WarnUserService {

    /**
     * 新增
     */
    int insert(WarnUserReqVo vo);

    /**
     * 删除
     */
    int delete(String id);

    /**
     * 更新
     */
    int update(WarnUserReqVo vo);

    PageVO<WarnUserRespVo> loadPage(WarnUserReqVo vo);

    /**
     * 批量导入数据
     * @param file
     * @param deptId
     */
    void importUserExcel(MultipartFile file, String deptId) throws IOException;
}
