package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.service.RelationshipService;
import com.ygkj.digitization.mapper.RelationshipMapper;
import com.ygkj.digitization.model.RelRelPs;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.*;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("relationshipService")
public class RelationshipServiceImpl implements RelationshipService {

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RelationshipMapper relationshipMapper;

    /**
     * 编辑关联关系
     *
     * @param reqVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(RelBaseReqVo reqVo) {
        String id = reqVo.getId();
        if (StringUtils.isEmpty(id)) {
            id = snowFlake.nextId() + "";
            //新增基本信息
            reqVo.setId(id);
            relationshipMapper.save(reqVo);
        } else {
            //修改基本信息
            relationshipMapper.update(reqVo);
            //删除旧的关联关系
            relationshipMapper.delRel(reqVo.getId());
        }
        //新增新的关联关系
        List<String> idList = reqVo.getList();
        if(!CollectionUtils.isEmpty(idList)){
            List<RelRelPs> list = new ArrayList<>();
            for (String s : reqVo.getList()) {
                RelRelPs relRelPs = new RelRelPs();
                relRelPs.setId(snowFlake.nextId() + "");
                relRelPs.setRelId(id);
                relRelPs.setPsCode(s);
                list.add(relRelPs);
            }
            relationshipMapper.saveRel(list);
        }
    }

    /**
     * 删除关联关系
     *
     * @param idList
     */
    @Override
    public void del(List<String> idList) {
        relationshipMapper.del(idList);
    }

    /**
     * 关联关系列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<RelBaseResVo> list(RelListReqVo reqVo) {
        if (!reqVo.isFetch()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        List<RelBaseResVo> list = relationshipMapper.list(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            //查询名称
            List<String> relIdList = list.stream().map(RelBaseResVo::getId).collect(Collectors.toList());
            Integer type = reqVo.getType();
            List<RelBaseResVo> nameList;
            if (type == 1) {
                nameList = relationshipMapper.selectResName(relIdList);
            } else {
                nameList = relationshipMapper.selectStName(relIdList);
            }
            //拼上名称
            if (!CollectionUtils.isEmpty(nameList)) {
                Map<String, String> map = nameList.stream().filter(relBaseResVo -> !StringUtils.isEmpty(relBaseResVo.getPsName()))
                        .collect(Collectors.toMap(RelBaseResVo::getId, RelBaseResVo::getPsName));
                list.forEach(relBaseResVo -> relBaseResVo.setPsName(map.get(relBaseResVo.getId())));
            }
        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 关联关系详细信息
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<RelDetailResVo> select(RelDetailReqVo reqVo) {
        if (reqVo.getType() == 1) {
            return relationshipMapper.selectRes(reqVo.getId());
        } else {
            return relationshipMapper.selectSt(reqVo.getId());
        }
    }

    /**
     * 水库或海塘列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<? extends ProjectResVo> listProject(ProjectListReqVo reqVo) {
        if ("水库".equals(reqVo.getProjectType())) {
            return relationshipMapper.listRes(reqVo);
        } else {
            return relationshipMapper.listSeawall(reqVo);
        }
    }

    /**
     * 测站列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public List<StResVo> listSt(StListReqVo reqVo) {
        return relationshipMapper.listSt(reqVo);
    }

    /**
     * 编辑潮位关联关系
     *
     * @param reqVo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editTide(RelSeawallReqVo reqVo) {
        //清除该测站旧的关联海塘
        relationshipMapper.updateSeawallByStCode(reqVo.getStCode());
        //将新的海塘列表和该测站进行关联
        relationshipMapper.saveSeawallRel(reqVo);
        //更新该测站的备注
        relationshipMapper.updateStRemark(reqVo.getStCode(), reqVo.getRemark());
    }

    /**
     * 删除潮位关联关系
     *
     * @param stCodes
     */
    @Override
    public void delTide(String stCodes) {
        //清除该测站旧的关联海塘
        relationshipMapper.updateSeawallByStCode(stCodes);
        //更新该测站的备注
        relationshipMapper.updateStRemark(stCodes, null);
    }

    /**
     * 关联潮位列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<RelSeawallListResVo> listTide(RelListReqVo reqVo) {
        if (!reqVo.isFetch()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        //有关联关系的潮位站
        List<RelSeawallListResVo> list = relationshipMapper.listTide(reqVo);
        if (!CollectionUtils.isEmpty(list)) {
            List<String> stCodeList = list.stream().map(RelSeawallListResVo::getStCode).collect(Collectors.toList());
            //查询名字
            List<RelSeawallListResVo> nameList = relationshipMapper.selectSeawallName(stCodeList);
            Map<String, String> map = nameList.stream().filter(relSeawallListResVo -> !StringUtils.isEmpty(relSeawallListResVo.getSeawallNames()))
                    .collect(Collectors.toMap(RelSeawallListResVo::getStCode, RelSeawallListResVo::getSeawallNames));
            list.forEach(relSeawallListResVo -> relSeawallListResVo.setSeawallNames(map.get(relSeawallListResVo.getStCode())));
        }
        return PageUtils.getPageVO(list);
    }

    /**
     * 关联潮位详细信息列表
     *
     * @param stCode
     * @return
     */
    @Override
    public List<RelSeawallDetailResVo> selectTide(String stCode) {
        return relationshipMapper.selectTide(stCode);
    }
}
