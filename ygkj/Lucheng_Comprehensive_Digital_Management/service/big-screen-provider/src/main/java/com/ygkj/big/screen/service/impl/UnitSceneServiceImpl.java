package com.ygkj.big.screen.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.big.screen.enums.BsNameEnum;
import com.ygkj.big.screen.mapper.BsSceneMapper;
import com.ygkj.big.screen.mapper.BsUnitMapper;
import com.ygkj.big.screen.mapper.BsUnitSceneMapper;
import com.ygkj.big.screen.mapper.MainDbMapper;
import com.ygkj.big.screen.service.UnitSceneService;
import com.ygkj.big.screen.model.BsScene;
import com.ygkj.big.screen.model.BsUnit;
import com.ygkj.big.screen.model.BsUnitScene;
import com.ygkj.big.screen.vo.request.BsSceneQueryReqVo;
import com.ygkj.big.screen.vo.request.BsUnitQueryReqVo;
import com.ygkj.big.screen.vo.request.SceneUnitReqVo;
import com.ygkj.big.screen.vo.response.ChartResVo;
import com.ygkj.big.screen.vo.response.Unit4SceneResVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.model.SysPermission;
import com.ygkj.model.SysRolePermission;
import com.ygkj.service.GenerateDistributionIDService;
import com.ygkj.utils.Builder;
import com.ygkj.utils.CollectionUtils;
import com.ygkj.utils.PageUtils;
import com.ygkj.vo.PageVO;
import com.ygkj.water.model.CommonResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xq
 * @Description
 * @Date 2021/6/28
 */
@Service("unitSceneServiceImpl")
public class UnitSceneServiceImpl implements UnitSceneService {


    private BsUnitMapper unitMapper;

    private BsUnitSceneMapper relMapper;

    private BsSceneMapper sceneMapper;

    private MainDbMapper mainDbMapper;

    private GenerateDistributionIDService idService;

    public UnitSceneServiceImpl(@Autowired BsUnitMapper unitMapper,
                                @Autowired BsUnitSceneMapper relMapper,
                                @Autowired BsSceneMapper sceneMapper,
                                @Autowired GenerateDistributionIDService idService,
                                @Autowired MainDbMapper mainDbMapper
    ) {
        this.unitMapper = unitMapper;
        this.relMapper = relMapper;
        this.sceneMapper = sceneMapper;
        this.idService = idService;
        this.mainDbMapper = mainDbMapper;
    }

    /**
     * 各个大屏组件
     *
     * @return
     */
    @Override
    public List<ChartResVo<String, Long>> unitNumStatistic() {
        List<ChartResVo<String, Long>> res = unitMapper.countUnitNumByBsName();
        Map<String, Long> tempMap = res.stream().collect(Collectors.toMap(ChartResVo::getKey, ChartResVo::getValue));
        BsNameEnum[] nameArray = BsNameEnum.values();
        List<ChartResVo<String, Long>> result = new ArrayList<>(nameArray.length);
        long total = 0;
        for (BsNameEnum temp : nameArray) {
            long tempNum = tempMap.getOrDefault(temp.getBsName(), 0L);
            total += tempNum;
            result.add(Builder.of(ChartResVo<String, Long>::new)
                    .with(ChartResVo::setKey, temp.getBsName())
                    .with(ChartResVo::setValue, tempNum)
                    .build());
        }
        result.add(0, Builder.of(ChartResVo<String, Long>::new)
                .with(ChartResVo::setKey, "全部")
                .with(ChartResVo::setValue, total)
                .build());
        return result;
    }

    /**
     * 查询大屏组件信息
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<BsUnit> listUnit(BsUnitQueryReqVo reqVo) {
        if (reqVo.getPageNum() != null
                && reqVo.getPageSize() != null
                && reqVo.getPageNum() > 0
                && reqVo.getPageSize() > 0)
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        return PageUtils.getPageVO(unitMapper.selectByCondition(reqVo));
    }

    /**
     * 添加或更新一项大屏组件信息
     *
     * @param record
     * @return
     */
    @Override
    public CommonResult addOrUpdateUnit(BsUnit record) {
        if (StringUtils.isNotBlank(record.getId())) {
            unitMapper.update(record);
        } else {
            record.setId(idService.nextID());
            unitMapper.insert(record);
        }
        return CommonResult.success();
    }

    /**
     * 删除一项大屏组件信息
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public CommonResult deleteUnit(String id) {
        unitMapper.delete(id);
        relMapper.delete(null, id);
        return CommonResult.success();
    }

    /**
     * 查询大屏场景信息
     *
     * @param bsName
     * @return
     */
    @Override
    public List<BsScene> listScene(String bsName, Integer mode) {
        mode = mode == null ? 1 : mode;
        if (mode == 1) {
            // 大屏模式下，查询大屏对应的固定场景＋自定义场景
            List<BsScene> result = new ArrayList<>();
            List<BsScene> temp1 = this.sceneMapper.selectByCondition(Builder.of(BsSceneQueryReqVo::new)
                    .with(BsSceneQueryReqVo::setBsName, bsName)
                    .with(BsSceneQueryReqVo::setDefaulted, 1)
                    .with(BsSceneQueryReqVo::setMode, 1).build());
            List<BsScene> temp2 = this.sceneMapper.selectByCondition(Builder.of(BsSceneQueryReqVo::new)
                    .with(BsSceneQueryReqVo::setBsName, bsName)
                    .with(BsSceneQueryReqVo::setDefaulted, 0)
                    .with(BsSceneQueryReqVo::setMode, 1).build());
            result.addAll(temp1);
            result.addAll(temp2);
            return result.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BsScene::getId))), ArrayList::new))
                    .stream()
                    .sorted(Comparator.comparing(o -> Long.valueOf(o.getId())))
                    .collect(Collectors.toList());
        } else {
//            //查全部自定义场景，不和用户做关联
//            return sceneMapper.customScene();
////            String userId = JwtTokenUtil.getCurrentUserId().orElse(StringUtils.EMPTY);
////            List<String> list = mainDbMapper.userUnits(userId);
////            if (list.isEmpty()){
////                return Collections.emptyList();
////            } else {
////                return sceneMapper.load(list.stream().collect(Collectors.joining(",")));
////            }

            // PC模式下，该用户的大屏对应的固定场景＋该用户自定义场景
            List<BsScene> result = new ArrayList<>();
            String userId = JwtTokenUtil.getCurrentUserId().orElse(StringUtils.EMPTY);
            List<BsScene> temp1 = this.sceneMapper.selectByCondition(Builder.of(BsSceneQueryReqVo::new)
                    .with(BsSceneQueryReqVo::setBsName, bsName)
                    // PC模式下固定场景不和用户绑定
//                    .with(BsSceneQueryReqVo::setUserId,userId)
                    .with(BsSceneQueryReqVo::setDefaulted, 1)
                    .with(BsSceneQueryReqVo::setMode, 2).build());
            List<BsScene> temp2 = this.sceneMapper.selectByCondition(Builder.of(BsSceneQueryReqVo::new)
                    .with(BsSceneQueryReqVo::setBsName, bsName)
                    .with(BsSceneQueryReqVo::setUserId, userId)
                    .with(BsSceneQueryReqVo::setDefaulted, 0)
                    .with(BsSceneQueryReqVo::setMode, 2).build());
            result.addAll(temp1);
            result.addAll(temp2);
            String sceneId = this.sceneMapper.loadDefaultSenceId(userId, bsName);
            return result.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BsScene::getId))), ArrayList::new))
                    .stream()
                    .peek(o -> o.setUserDefaultScene(StringUtils.isNotBlank(sceneId) && sceneId.equals(o.getId())))
                    .sorted(Comparator.comparing(o -> Long.valueOf(o.getId())))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 添加或更新一项大屏场景信息
     *
     * @param record
     * @return
     */
    @Transactional
    @Override
    public CommonResult addOrUpdateScene(SceneUnitReqVo record) {
        // 跟前端约定，传组件名字，所以先要把名字换成id
        List<BsUnitScene> unitList = record.getUnitList();
        if (CollectionUtils.isNotBlank(unitList)) {
            String unitNames = unitList.stream()
                    .map(BsUnitScene::getUnitId)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER));
            List<BsUnit> records = null;
            records = this.unitMapper.listIdByName(unitNames);
            records = records == null ? Collections.emptyList() : records;
            Map<String, String> map = records.stream().collect(Collectors.toMap(BsUnit::getUnitName, BsUnit::getId));
            unitList = unitList.stream()
                    .peek(unitScene -> unitScene.setUnitId(map.get(unitScene.getUnitId())))
                    .filter(unitScene -> StringUtils.isNotBlank(unitScene.getUnitId()))
                    .collect(Collectors.toList());
            record.setUnitList(unitList);
        }
        if (record.getMode() != null && record.getMode().equals(2)) {
            JwtTokenUtil.getCurrentUserId().ifPresent(record::setUserId);
        }
        if (StringUtils.isNotBlank(record.getId())) {
            return updateScene(record);
        } else {
            String id = idService.nextID();
            record.setId(id);
            record.setDefaulted(0);
//            SysPermission sysPermission = new SysPermission();
//            String permissionId = idService.nextID();
//            sysPermission.setId(permissionId);
//            sysPermission.setName(record.getSceneName());
//            sysPermission.setPid("e26d8d2b-4db4-450f-a004-0dbf7123dfc5");
//            sysPermission.setSceneId(id);
//            sysPermission.setType(1);
            sceneMapper.insert(record);
//            mainDbMapper.insertScenePermission(sysPermission);
//            List<String> roleIds = mainDbMapper.userRoleIds(JwtTokenUtil.getCurrentUserId().orElse(StringUtils.EMPTY));
//            List<SysRolePermission> list = new ArrayList<>();
//            for (String roleId : roleIds) {
//                list.add(new SysRolePermission(idService.nextID(),roleId,permissionId));
//            }
//            mainDbMapper.batchInsertPermissionAndRole(list);
            insertUnit4Scene(record.getUnitList(), record.getId());
            return CommonResult.success();
        }
    }

    private CommonResult updateScene(SceneUnitReqVo record) {
        if (sceneMapper.checkSceneEditable(record.getId()) == null) {
            return CommonResult.failed("场景不可编辑");
        }
        sceneMapper.update(record);
        List<BsUnitScene> paramList = record.getUnitList();
        for (BsUnitScene unitScene : paramList) {
            unitScene.setSceneId(record.getId());
        }
        if (CollectionUtils.isNotBlank(paramList)) {
            List<BsUnitScene> existList = relMapper.selectByCondition(record.getId(), null);
            if (CollectionUtils.isNotBlank(existList)) {
                Map<String, BsUnitScene> paramMap = paramList.stream().collect(Collectors.toMap(this::combineId, Function.identity()));
                Map<String, BsUnitScene> existMap = existList.stream().collect(Collectors.toMap(this::combineId, Function.identity()));

                List<String> insertIdList = new ArrayList<>(paramMap.keySet());
                List<String> deleteIdList = new ArrayList<>(existMap.keySet());

                List<String> updateIdList = new ArrayList<>(insertIdList);
                updateIdList.retainAll(deleteIdList);

                insertIdList.removeAll(updateIdList);
                deleteIdList.removeAll(updateIdList);
                if (CollectionUtils.isNotBlank(deleteIdList)) {
                    for (String s : deleteIdList) {
                        String[] strArray = splitId(s);
                        relMapper.delete(strArray[0], strArray[1]);
                    }
                }
                if (CollectionUtils.isNotBlank(updateIdList)) {
                    for (String s : updateIdList) {
                        BsUnitScene temp = paramMap.get(s);
                        relMapper.update(temp);
                    }
                }
                if (CollectionUtils.isNotBlank(insertIdList)) {
                    for (String s : insertIdList) {
                        BsUnitScene temp = paramMap.get(s);
                        relMapper.insert(temp);
                    }
                }
            } else {
                insertUnit4Scene(record.getUnitList(), record.getId());
            }
        } else {
            relMapper.delete(record.getId(), null);
        }
        return CommonResult.success();
    }

    private void insertUnit4Scene(List<BsUnitScene> unitList, String sceneId) {
        if (CollectionUtils.isNotBlank(unitList) && StringUtils.isNotBlank(sceneId)) {
            for (BsUnitScene temp : unitList) {
                temp.setSceneId(sceneId);
                relMapper.insert(temp);
            }
        }
    }

    private String combineId(BsUnitScene o) {
        return o.getSceneId().concat(com.ygkj.utils.StringUtils.JOIN_DELIMITER).concat(o.getUnitId());
    }

    private String[] splitId(String str) {
        return str.split(com.ygkj.utils.StringUtils.JOIN_DELIMITER);
    }

    /**
     * 删除一项大屏场景信息
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public CommonResult deleteScene(String id) {
        sceneMapper.delete(id);
        relMapper.delete(id, null);
        return CommonResult.success();
    }

    /**
     * 设置场景为默认场景
     *
     * @return
     */
    @Transactional
    @Override
    public CommonResult setSceneAsDefault(String sceneId, String bsName) {
        String userId = JwtTokenUtil.getCurrentUserId().orElse(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(sceneId) && StringUtils.isNotBlank(bsName) && StringUtils.isNotBlank(userId)) {
            this.sceneMapper.replaceSceneDefault(userId, bsName, sceneId);
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    /**
     * 查询一项完整的大屏
     *
     * @param bsName    大屏名称
     * @param sceneName 场景名称
     * @return
     */
    @Override
    public List<Unit4SceneResVo> listFullBigScreen(String sceneId, String bsName, String sceneName, Integer mode) {
        BsScene scene = null;
        if (StringUtils.isNotBlank(sceneId)) {
            scene = sceneMapper.load(sceneId).get(0);
        } else if (StringUtils.isNotBlank(bsName) && StringUtils.isNotBlank(sceneName)) {
            List<BsScene> scenes = sceneMapper.selectByCondition(Builder.of(BsSceneQueryReqVo::new)
                    .with(BsSceneQueryReqVo::setBsName, bsName)
                    .with(BsSceneQueryReqVo::setSceneName, sceneName)
                    .with(BsSceneQueryReqVo::setMode, mode).build());
            if (CollectionUtils.isNotBlank(scenes)) {
                scene = scenes.get(0);
            }
        }
        if (scene == null) {
            return Collections.emptyList();
        }
        List<BsUnitScene> relList = relMapper.selectByCondition(scene.getId(), null);
        List<BsUnit> unitList = unitMapper.selectByCondition(Builder.of(BsUnitQueryReqVo::new)
                .with(BsUnitQueryReqVo::setId, relList.stream()
                        .map(BsUnitScene::getUnitId)
                        .collect(Collectors.joining(com.ygkj.utils.StringUtils.JOIN_DELIMITER)))
                .build());
        Map<String, BsUnit> unitMap = unitList.stream().collect(Collectors.toMap(BsUnit::getId, Function.identity()));
        List<Unit4SceneResVo> result = new ArrayList<>(relList.size());
        for (BsUnitScene unitScene : relList) {
            BsUnit unit = unitMap.get(unitScene.getUnitId());
            if (unit == null) {
                continue;
            }
            Unit4SceneResVo target = new Unit4SceneResVo();
            BeanUtils.copyProperties(unit, target);
            target.setUnitSceneProperty(unitScene.getUnitSceneProperty());
            target.setSceneName(scene.getSceneName());
            result.add(target);
        }
        return result;
    }
}
