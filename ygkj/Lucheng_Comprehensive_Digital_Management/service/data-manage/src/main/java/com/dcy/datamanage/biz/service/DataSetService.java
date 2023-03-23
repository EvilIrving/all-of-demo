package com.dcy.datamanage.biz.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dcy.datamanage.api.model.*;
import com.dcy.datamanage.api.model.common.PageModel;
import com.dcy.datamanage.api.model.common.PageResult;
import com.dcy.datamanage.api.model.common.R;
import com.dcy.datamanage.biz.dto.input.BusDataSetPreviewInputDTO;
import com.dcy.datamanage.biz.dto.input.BusDataSetSaveDTO;
import com.dcy.datamanage.biz.dto.input.BusDataSetSearchInputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetPreviewOutputDTO;
import com.dcy.datamanage.biz.dto.output.BusDataSetSelectOutputDTO;
import com.dcy.datamanage.biz.mapper.DataSetMapper;
import com.dcy.datamanage.biz.util.PageUtil;
import com.dcy.datamanage.biz.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("dataSetService")
@Slf4j
public class DataSetService extends ServiceImpl<DataSetMapper, BusDataSet> {

    @Autowired
    private DataSetMapper dataSetMapper;

    @Autowired
    private DataSetSqlService dataSetSqlService;

    @Autowired
    private DataSetHttpService dataSetHttpService;

    @Autowired
    private DataSetParamService dataSetParamService;

    @Autowired
    private DataSetResFieldService dataSetResFieldService;

    /**
     * 列表查询
     *
     * @param dto
     * @param pageModel
     * @return
     */
    public PageResult<BusDataSetOutputDTO> list(BusDataSetSearchInputDTO dto, PageModel pageModel) {
        if (pageModel.isPage()) {
            PageHelper.startPage(pageModel.getCurrent().intValue(), pageModel.getSize().intValue());
        }
        List<BusDataSetOutputDTO> list = dataSetMapper.list(dto);
        //添加筛选
        if (!StringUtils.isEmpty(dto.getDataSetName())){
            list = list.stream().filter( l -> l.getDataSetName().contains(dto.getDataSetName())).collect(Collectors.toList());
        }
        return PageUtil.toPage(list);
    }

    /**
     * 新增数据集
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(BusDataSetSaveDTO dto) {
        Integer dataSetType = dto.getDataSetType();
        String id = SnowFlake.zero.nextId();
        dto.setId(id);
        JSONObject setDetail = dto.getSetDetail();
        //保存详情
        switch (dataSetType) {
            case 1:
                BusDataSetSql setSql = setDetail.toJavaObject(BusDataSetSql.class);
                setSql.setDataSetId(id);
                setSql.setId(SnowFlake.zero.nextId());
                dataSetSqlService.save(setSql);
                break;
            case 2:
                BusDataSetHttp setHttp = setDetail.toJavaObject(BusDataSetHttp.class);
                setHttp.setDataSetId(id);
                setHttp.setId(SnowFlake.zero.nextId());
                dataSetHttpService.save(setHttp);
                break;
            default:
        }
        //保存参数
        List<BusDataSetParam> paramList = dto.getParamList();
        if (!CollectionUtils.isEmpty(paramList)) {
            paramList.forEach(busDataSetParam -> {
                busDataSetParam.setId(SnowFlake.zero.nextId());
                busDataSetParam.setDataSetId(id);
            });
            dataSetParamService.saveBatch(paramList);
        }

        dto.setDelFlag(0);
        //保存返回字段
        batchSaveField(dto, id);

        return save(dto);
    }

    /**
     * 预览
     *
     * @param dto
     * @return
     */
    public R<BusDataSetPreviewOutputDTO> preview(BusDataSetPreviewInputDTO dto) {
        Integer dataSetType = dto.getDataSetType();
        String msg = checkParam(dto.getParam(), dto.getParamList());
        if (!StringUtils.isEmpty(msg)) {
            return R.error(msg);
        }
        //传进来的是空，就重新初始化
        if(CollectionUtils.isEmpty(dto.getFieldList())){
            dto.setFieldUpdate(true);
        }
        String errorMsg = "";
        BusDataSetPreviewOutputDTO result = null;
        try {
            switch (dataSetType) {
                case 1:
                    result = dataSetSqlService.preview(dto);
                    break;
                case 2:
                    result = dataSetHttpService.preview(dto);
                    break;
                default:
            }
        } catch (Exception e) {
            errorMsg = e.getMessage();
            log.error("[preview data set error]", e);
        }
        if(result == null){
            return R.error(errorMsg);
        }else {
            //这次查询是空，不覆盖之前的返回列
//            if(CollectionUtils.isEmpty(result.getFieldList()) && !dto.isFieldUpdate()){
//                result.setFieldList(dto.getFieldList());
//            }
            return R.success(result);
        }
    }

    /**
     * 必填验证
     *
     * @param param
     * @param paramList
     */
    private String checkParam(Map<String, Object> param, List<BusDataSetParam> paramList) {
        //验证是否所有必填参数都填了
        if (!CollectionUtils.isEmpty(paramList)) {
            for (BusDataSetParam busDataSetParam : paramList) {
                boolean requiredIsNull = busDataSetParam.getRequired() == 1 && (param == null || !param.containsKey(busDataSetParam.getParamName()));
                if (requiredIsNull) {
                    return busDataSetParam.getParamName() + "不可为空";
                }
            }
        }
        return "";
    }


    /**
     * 更新数据集信息
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean update(BusDataSetSaveDTO dto) {
        Integer dataSetType = dto.getDataSetType();
        String id = dto.getId();
        //更新详情
        JSONObject setDetail = dto.getSetDetail();
        switch (dataSetType) {
            case 1:
                BusDataSetSql setSql = setDetail.toJavaObject(BusDataSetSql.class);
                dataSetSqlService.updateById(setSql);
                break;
            case 2:
                BusDataSetHttp setHttp = setDetail.toJavaObject(BusDataSetHttp.class);
                dataSetHttpService.updateById(setHttp);
                break;
            default:
        }

        //更新参数信息
        LambdaQueryWrapper<BusDataSetParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusDataSetParam::getDataSetId, id);
        dataSetParamService.remove(queryWrapper);

        List<BusDataSetParam> paramList = dto.getParamList();
        if (!CollectionUtils.isEmpty(paramList)) {
            dataSetParamService.saveBatch(paramList);
        }

        //更新返回字段信息
        LambdaQueryWrapper<BusDataSetResField> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BusDataSetResField::getDataSetId, id);
        dataSetResFieldService.remove(wrapper);

        //更新返回字段
        batchSaveField(dto, id);

        //更新基本信息
        updateById(dto);

        return true;
    }

    private void batchSaveField(BusDataSetSaveDTO dto, String dataSetId) {
        List<BusDataSetResField> fieldList = dto.getFieldList();
        if (!CollectionUtils.isEmpty(fieldList)) {
            fieldList.forEach(busDataSetResField -> {
                busDataSetResField.setId(SnowFlake.zero.nextId());
                busDataSetResField.setDataSetId(dataSetId);
            });
            dataSetResFieldService.saveBatch(fieldList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean del(String id) {
        BusDataSet busDataSet = getById(id);

        //删除参数
        dataSetParamService.remove(new LambdaQueryWrapper<BusDataSetParam>().eq(BusDataSetParam::getDataSetId, id));

        //删除详情
        Integer dataSetType = busDataSet.getDataSetType();
        switch (dataSetType) {
            case 1:
                dataSetSqlService.remove(new LambdaQueryWrapper<BusDataSetSql>().eq(BusDataSetSql::getDataSetId, id));
                break;
            case 2:
                dataSetHttpService.remove(new LambdaQueryWrapper<BusDataSetHttp>().eq(BusDataSetHttp::getDataSetId, id));
                break;
            default:
        }

        //删除返回字段
        dataSetResFieldService.remove(new LambdaQueryWrapper<BusDataSetResField>().eq(BusDataSetResField::getDataSetId, id));

        //删除基本信息
        return removeById(id);
    }

    public BusDataSetSelectOutputDTO select(String id) {
        BusDataSetSelectOutputDTO result = new BusDataSetSelectOutputDTO();

        //基本信息
        BusDataSet busDataSet = getById(id);
        BeanUtils.copyProperties(busDataSet, result);

        //详情
        Integer dataSetType = busDataSet.getDataSetType();
        switch (dataSetType) {
            case 1:
                result.setSetDetail(dataSetSqlService.selectByDataSetId(id));
                break;
            case 2:
                result.setSetDetail(dataSetHttpService.getOne(new LambdaQueryWrapper<BusDataSetHttp>().eq(BusDataSetHttp::getDataSetId, id)));
                break;
            default:
        }

        //参数信息
        result.setParamList(dataSetParamService.list(new LambdaQueryWrapper<BusDataSetParam>().eq(BusDataSetParam::getDataSetId, id)));

        //返回字段
        result.setFieldList(dataSetResFieldService.list(new LambdaQueryWrapper<BusDataSetResField>().eq(BusDataSetResField::getDataSetId, id)));

        return result;
    }

    public Object result(String id, Map<String, Object> param) {
        //查询详情
        BusDataSetSelectOutputDTO detail = select(id);
        BusDataSetPreviewInputDTO dto = new BusDataSetPreviewInputDTO(detail);
        dto.setParam(param);

        R<BusDataSetPreviewOutputDTO> preview = preview(dto);
        Object res = preview.getData().getRes();
        return res;
    }
}
