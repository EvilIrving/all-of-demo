package com.ygkj.water.project.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ygkj.project.model.AttCameraOnline;
import com.ygkj.project.model.CameraWarnRecord;
import com.ygkj.project.model.HikvisionCameraRecord;
import com.ygkj.project.vo.request.CamBindObjReqVo;
import com.ygkj.project.vo.request.HikCameraQueryReqVo;
import com.ygkj.project.vo.response.CamBindObjResVo;
import com.ygkj.project.vo.response.CountCameraStatus;
import com.ygkj.project.vo.response.HikCameraResVo;
import com.ygkj.project.vo.response.HikOpsQuaResVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/4/14
 */
public interface HikCameraMapper {
    /**
     * 新增
     *
     * @author xq
     * @date 2021/04/14
     **/
    int insert(HikvisionCameraRecord hikvisionCameraRecord);

    /**
     * 更新
     *
     * @author xq
     * @date 2021/04/14
     **/
    int update(HikvisionCameraRecord hikvisionCameraRecord);

    /**
     * 查询 根据主键 id 查询
     *
     * @author xq
     * @date 2021/04/14
     **/
    HikvisionCameraRecord load(String cameraIndexCode);

    List<HikCameraResVo> selectByCondition(HikCameraQueryReqVo reqVo);

    List<HikCameraResVo> selectCameraInfoByCondition(HikCameraQueryReqVo reqVo);

    List<HikCameraResVo> selectCamStatus(HikCameraQueryReqVo reqVo);

    List<HikvisionCameraRecord> loadByProjCode(String projCode);

    @Update("update hikvision_camera_record SET project_code=NULL WHERE FIND_IN_SET(project_code,#{projectCodes})")
    int unbindProjects(String projectCodes);

    @Select("select distinct project_code from hikvision_camera_record where del_flag=0")
    List<String> selectAllBindProjCode();

    @Select("select camera_index_code, CONCAT(SUBSTR(area_code,1,6),'000000') as area_code,`status`,basin from hikvision_camera_record where del_flag=0")
    List<HikCameraResVo> selectAllStatusAdcdBasin();

    @Select("select if(b.project_type is null,'其他',b.project_type) project_type,a.`status` from hikvision_camera_record as a\n" +
            "LEFT JOIN att_real_all_project_base as b  on a.project_code=b.project_code\n" +
            "where a.del_flag=0")
    List<HikCameraResVo> selectAllProjTypeStatus();

    List<HikOpsQuaResVo> selectCamsProjOpsQua(HikCameraQueryReqVo reqVo);

    @Update("update hikvision_camera_record SET ops_id=NULL WHERE FIND_IN_SET(ops_id,#{opsIds})")
    int unbindOps(String opsIds);

    @Select("select index_code,online,create_time from att_camera_online where FIND_IN_SET(index_code,#{cameraIndexCodes}) and sync_date=#{syncDate}")
    @ResultType(AttCameraOnline.class)
    List<AttCameraOnline> selectCameraOnline(String cameraIndexCodes, Date syncDate);

    @Update("update hikvision_camera_record SET project_code=NULL WHERE camera_index_code = #{cameraIndexCode}")
    int unbindProjectCamera(String cameraIndexCode);

    @Update("update hikvision_camera_record SET ops_id=NULL WHERE ops_id=#{opsId} and camera_index_code = #{cameraIndexCode}")
    int unbindOpsCamera(String opsId, String cameraIndexCode);

    List<HikCameraResVo> candidateCamera(@Param("codes") List<String> codes);

    List<CountCameraStatus> countCameraStatus();

    List<String> selectCameraBindObjType(@Param("projCodeList") List<String> projCodeList);

    List<CamBindObjResVo> selectBindObjByCondition(CamBindObjReqVo reqVo);

    List<AttCameraOnline> selectCameraOnlineByCameraHashList(@Param("cameraHashList") List<Integer> cameraHashList, @Param("start") Date start, @Param("end") Date end);

    @DS("business")
    int insertCameraWarnRecord(CameraWarnRecord cameraWarnRecord);

    @DS("business")
    @Select("select * from ${tableName} where camera_index_code=#{cameraIndexCode} order by warn_time desc")
    @ResultType(CameraWarnRecord.class)
    List<CameraWarnRecord> selectCameraWarnRecord(String tableName, String cameraIndexCode);

    @DS("business")
    List<CameraWarnRecord> selectNewestWarnTime(String tableName, List<String> cameraIndexCodeList);

}
