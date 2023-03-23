package com.ygkj.digitization.service.impl;

import com.github.pagehelper.PageHelper;
import com.ygkj.digitization.enums.CollectionType;
import com.ygkj.digitization.mapper.RecordMapper;
import com.ygkj.digitization.mapper.SysUserCollectionMapper;
import com.ygkj.digitization.model.SysUserCollection;
import com.ygkj.digitization.service.RecordService;
import com.ygkj.digitization.vo.request.*;
import com.ygkj.digitization.vo.response.CatalogResVo;
import com.ygkj.digitization.vo.response.RecordListResVo;
import com.ygkj.digitization.vo.response.RecordResVo;
import com.ygkj.jwt.JwtTokenUtil;
import com.ygkj.utils.PageUtils;
import com.ygkj.utils.SnowFlake;
import com.ygkj.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("recordService")
public class RecordServiceImpl implements RecordService {

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private SysUserCollectionMapper collectionMapper;


    @Override
    public PageVO catalogs(CatalogPageQueryVo queryVo) {
        if (null != queryVo.getPageNum() && null != queryVo.getPageSize()) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        List<CatalogResVo> list = recordMapper.selectCatalogsByCondition(queryVo);
        return PageUtils.getPageVO(list);
    }

    /**
     * 获取档案目录树
     *
     * @return
     */
    @Override
    public CatalogResVo listCatalog() {
        CatalogResVo root = null;
        //查询所有档案目录
        List<CatalogResVo> list = recordMapper.listCatalog();

        Map<String, CatalogResVo> map = new HashMap<>(list.size());
        //第一次遍历找到根节点并转成map
        for (CatalogResVo catalogResVo : list) {
            if ("0".equals(catalogResVo.getParentId())) {
                root = catalogResVo;
            }
            map.put(catalogResVo.getId(), catalogResVo);
        }

        //第二次遍历组装树
        buildTree(list, map);

//        //查询每个目录下的文件数量
//        List<CatalogResVo> countFileByCatalog = recordMapper.countFileByCatalog();
//        Map<String, Integer> countMap = countFileByCatalog.stream().collect(Collectors.toMap(CatalogResVo::getId, CatalogResVo::getCount));
//        //第三次遍历给目录加上文件数量
//        for (Map.Entry<String, CatalogResVo> entry : map.entrySet()) {
//            String id = entry.getKey();
//            if (countMap.containsKey(id)) {
//                CatalogResVo catalogResVo = entry.getValue();
//                int count = countMap.get(id);
//                catalogResVo.setCount(catalogResVo.getCount() + count);
//                jointCount(map, catalogResVo.getParentId(), count);
//            }
//        }

        //拼接成树结构
        return root;
    }

    /**
     * 构建树档案目录树
     *
     * @param list
     * @param map
     */
    private void buildTree(List<CatalogResVo> list, Map<String, CatalogResVo> map) {
        list.forEach(catalogResVo -> {
            String parentId = catalogResVo.getParentId();
            if (map.containsKey(parentId)) {
                CatalogResVo parent = map.get(parentId);
                List<CatalogResVo> subCatalogs = parent.getSubCatalogs();
                if (subCatalogs == null) {
                    subCatalogs = new ArrayList<>();
                    parent.setSubCatalogs(subCatalogs);
                }
                subCatalogs.add(catalogResVo);
            }
        });
    }

    /**
     * 递归往上给上级目录加上文件数量
     *
     * @param map
     * @param parentId
     * @param count
     */
    private void jointCount(Map<String, CatalogResVo> map, String parentId, int count) {
        if (map.containsKey(parentId)) {
            CatalogResVo catalogResVo = map.get(parentId);
            catalogResVo.setCount(catalogResVo.getCount() + count);
            jointCount(map, catalogResVo.getParentId(), count);
        }
    }


    /**
     * 编辑档案目录
     *
     * @param catalogReqVo
     */
    @Override
    public void editCatalog(CatalogReqVo catalogReqVo) {
        if (StringUtils.isEmpty(catalogReqVo.getId())) {
            catalogReqVo.setId(snowFlake.nextId() + "");
            catalogReqVo.setSort(recordMapper.countCatalogByPid(catalogReqVo.getParentId()));
            catalogReqVo.setCreateBy(JwtTokenUtil.getCurrentUserName().orElse(org.apache.commons.lang3.StringUtils.EMPTY));
            recordMapper.saveCatalog(catalogReqVo);
        } else {
            recordMapper.updateCatalog(catalogReqVo);
        }
    }

    /**
     * 逻辑删除档案目录
     *
     * @param id
     */
    @Override
    public void delCatalog(String id) {
        recordMapper.delCatalog(id);
    }

    /**
     * 编辑档案
     *
     * @param reqVo
     */
    @Override
    public void editRecord(RecordReqVo reqVo) {
        if (StringUtils.isEmpty(reqVo.getId())) {
            reqVo.setId(snowFlake.nextId() + "");
            recordMapper.saveRecord(reqVo);
        } else {
            recordMapper.updateRecord(reqVo);
        }
    }

    /**
     * 删除档案
     *
     * @param id
     */
    @Override
    public void delRecord(String id) {
        recordMapper.delRecord(id);
    }

    /**
     * 档案列表
     *
     * @param reqVo
     * @return
     */
    @Override
    public PageVO<RecordListResVo> listRecord(RecordListReqVo reqVo) {
        //查询所有档案目录
        List<CatalogResVo> list = recordMapper.listCatalog();
        Map<String, CatalogResVo> map = list.stream().collect(Collectors.toMap(CatalogResVo::getId, Function.identity()));

        //组装成树形结构
        buildTree(list, map);

        //获取当前目录id和该目录下所有目录的id
        List<String> catalogIdList = new ArrayList<>();
        reqVo.setCatalogList(catalogIdList);
        String rootId = reqVo.getCatalogId();
        catalogIdList.add(rootId);
        CatalogResVo catalogResVo = map.get(rootId);
        getSubCatalog(catalogResVo, catalogIdList);
        if (!reqVo.isFetch()) {
            PageHelper.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        }
        String date = reqVo.getDate();
        if (!StringUtils.isEmpty(date)) {
            reqVo.setStartTime(date + "00:00:00");
            reqVo.setEndTime(date + "23:59:59");
        }
        List<RecordListResVo> recordListResVos = recordMapper.listRecord(reqVo);

        //判断文件是否被当前用户收藏
        CollectionPageQueryVo queryVo = new CollectionPageQueryVo();
        queryVo.setUserId(JwtTokenUtil.getCurrentUserId().orElse(org.apache.commons.lang3.StringUtils.EMPTY));
        queryVo.setCollectionType(CollectionType.ARCHIVES.type());
        List<SysUserCollection> collects = collectionMapper.selectByCondition(queryVo);
        Set<String> set = collects.stream().map(SysUserCollection::getObjectId).collect(Collectors.toSet());
        recordListResVos.stream().filter(recordListResVo -> set.contains(recordListResVo.getId()))
                .forEach(recordListResVo -> recordListResVo.setCollected(Boolean.TRUE));
        return PageUtils.getPageVO(recordListResVos);
    }

    /**
     * 递归获取子目录
     *
     * @param parentNode
     * @param list
     */
    private void getSubCatalog(CatalogResVo parentNode, List<String> list) {
        List<CatalogResVo> subCatalogs = parentNode.getSubCatalogs();
        if (!CollectionUtils.isEmpty(subCatalogs)) {
            for (CatalogResVo catalogResVo : subCatalogs) {
                list.add(catalogResVo.getId());
                getSubCatalog(catalogResVo, list);
            }
        }
    }

    /**
     * 查询档案详情
     *
     * @param id
     * @return
     */
    @Override
    public RecordResVo selectRecord(String id) {
        return recordMapper.selectRecord(id);
    }

    @Override
    public Boolean collect(String id) {
        RecordResVo record = recordMapper.selectRecord(id);
        SysUserCollection collection = new SysUserCollection(String.valueOf(snowFlake.nextId()), record.getId(),
                record.getFileName(), record.getCategory(), record.getFileUrl(), new Date(),
                JwtTokenUtil.getCurrentUserId().orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                JwtTokenUtil.getCurrentUserName().orElse(org.apache.commons.lang3.StringUtils.EMPTY),
                CollectionType.ARCHIVES.type(), record.getUploaderName());
        return collectionMapper.insert(collection) > 0;
    }

    @Override
    public Boolean unCollect(String objectId) {
        CollectionPageQueryVo queryVo = new CollectionPageQueryVo();
        queryVo.setUserId(JwtTokenUtil.getCurrentUserId().orElse(org.apache.commons.lang3.StringUtils.EMPTY));
        queryVo.setObjectId(objectId);
        List<SysUserCollection> list = collectionMapper.selectByCondition(queryVo);
        if (!list.isEmpty()) {
            return collectionMapper.delete(list.get(0).getId()) > 0;
        }
        return true;
    }

    @Override
    public PageVO myCollection(CollectionPageQueryVo queryVo) {
        if (queryVo.getPageNum() != null && queryVo.getPageSize() != null) {
            PageHelper.startPage(queryVo.getPageNum(), queryVo.getPageSize());
        }
        queryVo.setUserId(JwtTokenUtil.getCurrentUserId().orElse(org.apache.commons.lang3.StringUtils.EMPTY));
        List<SysUserCollection> list = collectionMapper.selectByCondition(queryVo);
        return PageUtils.getPageVO(list);
    }
}
