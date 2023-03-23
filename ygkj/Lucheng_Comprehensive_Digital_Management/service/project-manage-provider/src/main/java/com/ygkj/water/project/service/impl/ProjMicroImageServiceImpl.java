package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.ProjMicroImage;
import com.ygkj.project.model.SysFile;
import com.ygkj.project.vo.request.ProjMicroImageInsVo;
import com.ygkj.project.vo.request.ProjMicroImageReqVo;
import com.ygkj.project.vo.response.ProjMicroImageRespVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.DateUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.feign.UserFeignClient;
import com.ygkj.water.project.mapper.ProjMicroImageMapper;
import com.ygkj.water.project.mapper.UserMapper;
import com.ygkj.water.project.service.ProjMicroImageService;
import com.ygkj.water.project.utils.CommonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hucong
 * @description 工程监督-微影像管理表
 * @date 2021-07-09
 */
@Service
public class ProjMicroImageServiceImpl implements ProjMicroImageService {

    @Resource
    private ProjMicroImageMapper projMicroImageMapper;
    @Resource
    private GenerateDistributionIDService idService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserFeignClient userFeignClient;

    @Override
    public int insert(ProjMicroImageInsVo vo) {
        ProjMicroImage entity = new ProjMicroImage();
        BeanUtils.copyProperties(vo, entity);
        entity.setId(idService.nextID());
        entity.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        entity.setUploadName(JwtTokenUtil.getCurrentUserName().orElse(""));
        entity.setUploadTime(new Date());
        return projMicroImageMapper.insert(entity);
    }

    @Override
    public int delete(String id) {
        return projMicroImageMapper.delete(id);
    }

    @Override
    public int update(ProjMicroImageInsVo vo) {
        ProjMicroImage entity = new ProjMicroImage();
        BeanUtils.copyProperties(vo, entity);
        return projMicroImageMapper.update(entity);
    }

    @Override
    public List<JSONObject> loadPage(ProjMicroImageReqVo vo) {
        //判断当前用户是否是建管中心的人员:如果是,查询所有的信息；不是，查询当前用户上传的
        if (!CommonUtil.isConstructionManagementPerson(userMapper)) {
            vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        }
        List<ProjMicroImageRespVo> list = projMicroImageMapper.findList(vo);

        List<JSONObject> joList = new LinkedList<>();
        JSONObject jo = null;
        Set<String> monthList = list.stream().map(e -> DateUtils.format(e.getUploadTime(), "yyyy-MM")).collect(Collectors.toSet());
        Iterator<String> iterator = monthList.iterator();
        while (iterator.hasNext()) {
            String month = iterator.next();

            List<ProjMicroImageRespVo> list2 = projMicroImageMapper.findDataByMonth(month);
            jo = new JSONObject();
            for (ProjMicroImageRespVo projMicroImageRespVo : list2) {
                String imgIds = projMicroImageRespVo.getImgIds();
                if (!StringUtils.isEmpty(imgIds)) {
                    List<SysFile> fileList = projMicroImageMapper.fileList(imgIds);
                    projMicroImageRespVo.setImgList(fileList);
                }
            }
            jo.put("month", month);
            jo.put("data", list2);
            joList.add(jo);
        }

        return joList;
    }

    @Override
    public List<Integer> yearList(String type) {
        return projMicroImageMapper.yearList(type);
    }

    @Override
    public PageVO<ProjMicroImageRespVo> listData(ProjMicroImageReqVo vo) {
        if (vo.getPageNum() != null && vo.getPageNum() > 0
                && vo.getPageSize() != null && vo.getPageSize() > 0)
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        if (!StringUtils.isEmpty(JwtTokenUtil.getCurrentUserId().orElse(""))) {
            vo.setUploadId(JwtTokenUtil.getCurrentUserId().orElse(""));
        }
        List<ProjMicroImageRespVo> list = projMicroImageMapper.findList(vo);
        for (ProjMicroImageRespVo projMicroImageRespVo : list) {
            String imgIds = projMicroImageRespVo.getImgIds();
            if (!StringUtils.isEmpty(imgIds)) {
                List<SysFile> fileList = projMicroImageMapper.fileList(imgIds);
                if (!fileList.isEmpty()) {
                    projMicroImageRespVo.setImgFile(fileList.get(0));
                }

            }
        }
        return PageUtils.getPageVO(list);
    }

    @Override
    public void saveUserChoosepData(String data) {
        userFeignClient.putUserChooseProject("ProjMicro_" + JwtTokenUtil.getCurrentUserId().orElse(""), data);
    }

    @Override
    public JSONObject getUserChoosepData() {
        String data = userFeignClient.getUserChooseProject("ProjMicro_" + JwtTokenUtil.getCurrentUserId().orElse(""));
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        try {
            return JSONObject.parseObject(data);
        } catch (Exception e) {
            return null;
        }
    }
}
