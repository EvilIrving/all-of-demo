package com.dcy.datamanage.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.DataAssessDeptRel;
import com.dcy.datamanage.api.model.UserInfo;
import com.dcy.datamanage.biz.dto.input.DataAssessDeptInputDTO;
import com.dcy.datamanage.biz.dto.input.DataAssessDeptSearchInputDTO;
import com.dcy.datamanage.biz.dto.input.DataEditInputDTO;
import com.dcy.datamanage.biz.dto.output.DataAssessDeptSearchOutputDTO;
import com.dcy.datamanage.biz.feign.UmsFeign;
import com.dcy.datamanage.biz.mapper.DataAssessDeptMapper;
import com.dcy.datamanage.biz.util.SnowFlake;
import com.ygkj.auth.api.department.model.SysDept;
import com.ygkj.auth.api.user.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DataAssessDeptService extends ServiceImpl<DataAssessDeptMapper, DataAssessDeptRel> {

    @Autowired
    private UmsFeign umsFeign;

    public Boolean batchSave(DataAssessDeptInputDTO dto) {
        List<String> tableNameList = dto.getTableNameList();
        List<DataAssessDeptRel> deptRelList = new ArrayList<>();
        if (CollectionUtils.isEmpty(tableNameList)) {
            dto.setId(SnowFlake.zero.nextId());
            //列表为空,保存单条
            deptRelList.add(dto);
        } else {
            //列表不为空，批量保存
            for (String tableName : tableNameList) {
                DataAssessDeptRel dataAssessDeptRel = new DataAssessDeptRel();
                dataAssessDeptRel.setDeptId(dto.getDeptId());
                dataAssessDeptRel.setDataSourceId(dto.getDataSourceId());
                dataAssessDeptRel.setUserId(dto.getUserId());
                dataAssessDeptRel.setId(SnowFlake.zero.nextId());
                dataAssessDeptRel.setTableName(tableName);
                deptRelList.add(dataAssessDeptRel);
            }
        }
        baseMapper.batchReplace(deptRelList);
        return true;
    }

    /**
     * 查询某个表的归属科室和责任人
     *
     * @param dto
     * @return
     */
    public List<DataAssessDeptSearchOutputDTO> list(DataAssessDeptSearchInputDTO dto) {
        List<DataAssessDeptSearchOutputDTO> result = baseMapper.list(dto);
        //筛选
        if (!StringUtils.isEmpty(dto.getTableName())){
            result = result.stream().filter( r -> r.getTableName().contains(dto.getTableName())).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(result)) {
            List<String> deptIdList = new ArrayList<>();
            List<String> userIdList = new ArrayList<>();
            result.forEach(dataAssessDeptSearchOutputDTO -> {
                deptIdList.add(dataAssessDeptSearchOutputDTO.getDeptId());
                userIdList.add(dataAssessDeptSearchOutputDTO.getUserId());
            });
            deptName(deptIdList, result);
            userName(userIdList, result);
        }
        return result;
    }

    /**
     * 补上用户名称
     *
     * @param userIdList
     * @param result
     */
    private void userName(List<String> userIdList, List<DataAssessDeptSearchOutputDTO> result) {
        if (!CollectionUtils.isEmpty(userIdList)) {
            List<SysUser> userInfoList = umsFeign.selectUserByIdList(userIdList);
            if (!CollectionUtils.isEmpty(userInfoList)) {
                Map<String, SysUser> userInfoMap = userInfoList.stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
                result.forEach(dto -> dto.setUserName(userInfoMap.get(dto.getUserId()).getUsername()));
            }
        }
    }

    /**
     * 补上部门名称
     *
     * @param deptIdList
     */
    private void deptName(List<String> deptIdList, List<DataAssessDeptSearchOutputDTO> result) {
        if (!CollectionUtils.isEmpty(deptIdList)) {
            List<SysDept> deptList = umsFeign.selectDeptByIdList(deptIdList);
            if (!CollectionUtils.isEmpty(deptList)) {
                Map<String, SysDept> deptMap = deptList.stream().collect(Collectors.toMap(SysDept::getId, Function.identity()));
                result.forEach(dto -> dto.setDeptName(deptMap.get(dto.getDeptId()).getName()));
            }
        }
    }


    public boolean haveTablePermission(DataEditInputDTO dto) {
        LambdaQueryWrapper<DataAssessDeptRel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataAssessDeptRel::getDataSourceId, dto.getDataSourceId())
                .eq(DataAssessDeptRel::getTableName, dto.getTableName())
                .eq(DataAssessDeptRel::getDeptId, dto.getOperateDeptId());
        final long count = count(queryWrapper);
        return count > 0;
    }
}
