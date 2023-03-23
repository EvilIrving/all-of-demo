package com.ygkj.digitization.mapper;

import com.ygkj.digitization.model.AttTideFcstBase;
import com.ygkj.digitization.model.AttTideFcstValBase;
import com.ygkj.digitization.vo.request.FcstValReqVo;
import com.ygkj.digitization.vo.request.TideFcstQueryVo;
import com.ygkj.digitization.vo.response.*;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * @author xq
 * @description 风暴潮预报表
 * @date 2021-09-22
 */
public interface AttTideFcstBaseMapper {

    /**
     * 新增
     *
     * @author xq
     * @date 2021/09/22
     **/
    int insert(AttTideFcstBase attTideFcstBase);

    /**
     * 刪除
     *
     * @author xq
     * @date 2021/09/22
     **/
    int delete(String id);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/09/22
     **/
    int update(AttTideFcstBase attTideFcstBase);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/09/22
     **/
    AttTideFcstBase load(String id);

    int replaceVal(AttTideFcstValBase tideFcstValBase);

    List<TideFcstResVo> selectByCondition(TideFcstQueryVo queryVo);

    TideFcstResVo selectNewestFcst();

    List<TideFcstValResVo> selectManualValByFcstId(String fcstId);

    @Delete("delete from att_tide_fcst_val_base where fcst_id=#{fcstId}")
    int deleteValByByFcstId(String fcstId);


    /**
     * 根据id查询风暴潮列表
     *
     * @param fcstId
     * @return
     */
    List<AttTideFcstValResVo> listFcstVal(String fcstId);

    /**
     * 风暴潮站点列表
     *
     * @param fcstId
     * @return
     */
    List<FcstStResVo> listTileFcstSt(String fcstId);

    /**
     * 风暴潮站点
     *
     * @param reqVo
     * @return
     */
    List<FcstValResVo> listTileFcstVal(FcstValReqVo reqVo);

    /**
     * 批量新增值
     * @param manualValList
     */
    void batchInsertVal(List<TideFcstValResVo> manualValList);
}
