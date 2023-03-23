package com.ygkj.gragh.mapper;

import com.ygkj.gragh.model.AttVideoBase;
import com.ygkj.gragh.model.CameraRecord;
import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author Huangzh
 * @description camera_record
 * @date 2020-12-08
 */
public interface CameraRecordMapper {

    /**
     * 新增
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int insert(CameraRecord cameraRecord);

    /**
     * 刪除
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int update(CameraRecord cameraRecord);

    /**
     * 查询 根据主键 id 查询
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    CameraRecord load(String id);

    /**
     * 查询 分页查询
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    List<CameraRecord> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author Huangzh
     * @date 2020/12/08
     **/
    int pageListCount(int offset, int pagesize);

    List<HikvisionCameraRecord> selectByCondition(CameraQueryVo cameraQueryVo);

    @Select("SELECT `year_month`,offline_number FROM att_camera_offline_each_month WHERE `year_month` LIKE CONCAT(#{year},'%')")
    List<Map<String, Object>> selectCameraOfflineEachMonth(String year);

}

