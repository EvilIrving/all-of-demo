package com.ygkj.river.mapper;

import com.ygkj.river.model.HikvisionCameraRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/8
 */
public interface HikvisionCameraRecordMapper {
    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/04/08
     **/
    HikvisionCameraRecord load(String cameraIndexCode);

    List<HikvisionCameraRecord> loadMany(String cameraCodes);


    List<HikvisionCameraRecord> cameraOfRiver(String riverCode);

    @Select("select camera_name from hikvision_camera_record where camera_index_code=#{cameraIndexCode}")
    String cameraNameByCode(String cameraIndexCode);
}
