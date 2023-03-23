package com.ygkj.water.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.project.model.AttServiceItem;
import com.ygkj.project.vo.request.AttServiceItemReqVo;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.HttpClientUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.StringUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.project.mapper.AttServiceItemMapper;
import com.ygkj.water.project.service.AttServiceItemService;
import com.ygkj.water.project.utils.DESUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-12 13:51
 * @description
 */
@Service
public class AttServiceItemServiceImpl implements AttServiceItemService {
    @Value("${access.key}")
    private String key;

    @Value("${access.secret}")
    private String secret;

    @Resource
    private AttServiceItemMapper attServiceItemMapper;
    @Resource
    private GenerateDistributionIDService idService;

    @Override
    public JSONObject loadSkPage(AttServiceItemReqVo vo) {
        String url = "";
        if (StringUtils.isNotBlank(vo.getProjectType())){
            if ("水库".equals(vo.getProjectType())){
                url = "https://sk.zjwater.com/mgt/ml/waterdirectory/listJson?adcd=330302000000";
            }else if ("海塘".equals(vo.getProjectType())){
                url = "https://sk.zjwater.com/mgt/ml/seawalldirectory/listJson?adcd=330302000000";
            }else if ("水闸".equals(vo.getProjectType())){
                url = "https://sk.zjwater.com/mgt/ml/sluicedirectory/listJson?adcd=330302000000";
            } else if ("堤防".equals(vo.getProjectType())){
                url = "https://sk.zjwater.com/mgt/ml/dikedirectory/listJson?adcd=330302000000";
            }
            url = url + "&type=" + vo.getProjectType();
        }
        if (vo.isPaged()){
            url = url + "&page="+ vo.getPageNum();
            url = url + "&rows="+ vo.getPageSize();
        }
        if (StringUtils.isNotBlank(vo.getProjectName())){
            url = url + "&queryStr=" + vo.getProjectName();
        }

        url = url + "&outsideToken=" + getToken();
        String get = HttpClientUtil.httpGet(url);
        JSONObject object = JSONObject.parseObject(get);
        object.put("pageNum",ObjectUtils.isNotEmpty(vo.getPageNum())?vo.getPageNum():1);
        return object;
    }

    @Override
    public int insert(AttServiceItemReqVo vo) {

        AttServiceItem attServiceItem = new AttServiceItem();
        BeanUtils.copyProperties(vo, attServiceItem);
        attServiceItem.setId(idService.nextID());
        attServiceItem.setCreateTime(new Date());
        return attServiceItemMapper.insert(attServiceItem);
    }

    @Override
    public int update(AttServiceItemReqVo vo) {
        AttServiceItem attServiceItem = new AttServiceItem();
        BeanUtils.copyProperties(vo, attServiceItem);
        attServiceItem.setUpdateId(JwtTokenUtil.getCurrentUserId().orElse(""));
        attServiceItem.setUpdateTime(new Date());
        return attServiceItemMapper.updateById(attServiceItem);
    }

    @Override
    public int delete(String id) {

        return attServiceItemMapper.deleteItemById(id);
    }

    @Override
    public PageVO<AttServiceItem> loadPage(AttServiceItemReqVo vo) {

        if (vo.isPaged()){
            PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        }
        List<AttServiceItem> attServiceItems = attServiceItemMapper.selectList(Wrappers.lambdaQuery(AttServiceItem.class)
                .eq(ObjectUtils.isNotEmpty(vo.getProjectType()), AttServiceItem::getProjectType, vo.getProjectType())
                .eq(StringUtils.isNotBlank(vo.getId()),AttServiceItem::getId,vo.getId())
                .eq(StringUtils.isNotBlank(vo.getDeptId()),AttServiceItem::getDeptId,vo.getDeptId())
                .like(ObjectUtils.isNotEmpty(vo.getProjectName()), AttServiceItem::getProjectName, vo.getProjectName())
                .eq(AttServiceItem::isDelFlag, 0));

        return PageUtils.getPageVO(attServiceItems);
    }

    private String getToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", "13858860396");
        jsonObject.put("prcd", "");

        jsonObject.put("accesskey", key);
        jsonObject.put("accesssecret", secret);
        String data = JSON.toJSONString(jsonObject);
        return DESUtil.encrypt(DESUtil.KEY, data);
    }
}
