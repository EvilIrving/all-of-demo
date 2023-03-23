package com.ygkj.mapper;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.auth.api.file.model.SysFile;
import com.ygkj.vo.req.SysFileReqVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Huang.zh
 * @date 2020/9/15 15:43
 * @Description:
 */
@Repository
public interface SysFileMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int insert(SysFile sysFile);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int update(SysFile sysFile);

    int batchLogicDelete(String ids);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysFile> load(String id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    List<SysFile> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/09/15
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 档案数据统计
     *
     * @return
     */
    List<JSONObject> archivalDataStatistics();


    /**
     * 数字档案上传统计
     *
     * @param year 年份
     * @return
     */
    List<JSONObject> statUploadFile(Integer year);

    /**
     * 文件所有年份
     *
     * @return
     */
    List<String> findFileGroupTimes();

    /**
     * 文件所有文件类型
     *
     * @return
     */
    List<String> findFileGroupTypes();

    /**
     * 查询全部数据
     *
     * @param vo
     * @return
     */
    List<SysFile> selectAll(SysFileReqVo vo);

    /**
     * 逻辑删除文件
     *
     * @param id
     * @return
     */
    int del(String id);

    List<SysFile> selectAppAchievement(SysFileReqVo vo);
}
