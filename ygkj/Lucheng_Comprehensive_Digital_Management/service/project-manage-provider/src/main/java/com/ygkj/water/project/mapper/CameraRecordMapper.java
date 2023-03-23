package com.ygkj.water.project.mapper;

import com.ygkj.gragh.model.CameraRecord;
import com.ygkj.gragh.model.HikvisionCameraRecord;
import com.ygkj.gragh.vo.request.CameraQueryVo;
import com.ygkj.project.vo.response.CameraResVo;
import com.ygkj.project.vo.response.OutLineCameraResVo;
import org.apache.ibatis.annotations.Param;

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
    int insert(HikvisionCameraRecord cameraRecord);

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
    int update(HikvisionCameraRecord cameraRecord);

    int batchDelete(@Param("list") List<String> list);

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
    List<Map<String, Object>> countByStatus();

    List<HikvisionCameraRecord> selectByCondition(CameraQueryVo cameraQueryVo);

    List<CameraResVo> selectByProjectCodes(@Param("list") List<String> list, @Param("cameraName") String cameraName
            , @Param("cameraIndexCode") String cameraIndexCode);

    List<CameraResVo> selectByRiverCodes(@Param("cameraName") String cameraName
            , @Param("cameraIndexCode") String cameraIndexCode);

    List<Map<String, Object>> countCamerasByProjectType();

    List<Map<String, Object>> countCamerasByAdcd();

    List<Map<String, Object>> countByProjectType();

    List<Map<String, Object>> selectCameras();

    List<Map<String, Object>> selectOutLineCameras();

    List<OutLineCameraResVo> outLineCameras();

    List<OutLineCameraResVo> outLineCameraOrderList();
}

