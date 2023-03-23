package com.ygkj.utils;

import com.github.pagehelper.Page;
import com.ygkj.entity.PageEntity;
import com.ygkj.vo.PageVO;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class PageUtils {

    private PageUtils() {
    }

    public static <T> PageVO<T> getPageVO(List<T> list) {
        PageVO<T> result = new PageVO<>();
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            result.setTotalRows(page.getTotal());
            result.setTotalPages(page.getPages());
            result.setPageNum(page.getPageNum());
            result.setPageSize(page.getPageSize());
            result.setCurPageSize(page.size());
            result.setList(page.getResult());
        } else {
            if (!CollectionUtils.isEmpty(list)) {
                result.setList(list);
                result.setTotalRows(Long.valueOf(list.size()));
            } else {
                result.setTotalRows(0L);
                result.setList(Collections.emptyList());
            }
        }
        return result;
    }

    /**
     * 内存分页,把数据查出来后再分页，强烈不推荐使用
     *
     * @return com.ygkj.vo.PageVO
     */
    public static <T> PageVO<T> pageInRam(PageEntity pageEntity, List<T> list) {
        int pageNum = pageEntity.getPageNum() == null ? 0 : pageEntity.getPageNum();
        int pageSize = pageEntity.getPageSize() == null ? 0 : pageEntity.getPageSize();
        return pageInRam(pageNum, pageSize, list);
    }


    /**
     * 内存分页,把数据查出来后再分页，强烈不推荐使用
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return com.ygkj.vo.PageVO
     */
    public static <T> PageVO<T> pageInRam(int pageNum, int pageSize, List<T> list) {
        PageVO<T> result = new PageVO<>();
        list = list == null ? Collections.emptyList() : list;
        int totalPages = 0;
        List<T> pagedList = Collections.emptyList();
        if (pageNum > 0 && pageSize > 0) {
            if (com.ygkj.utils.CollectionUtils.isNotBlank(list)) {
                if (list.size() % pageSize > 0) {
                    totalPages += (list.size() / pageSize + 1);
                } else {
                    totalPages += (list.size() / pageSize);
                }
                if (pageNum > totalPages) {
                    pagedList = Collections.emptyList();
                } else {
                    int end = pageNum * pageSize;
                    if (end > list.size())
                        end = list.size();
                    pagedList = list.subList((pageNum - 1) * pageSize, end);
                }
            }
        } else {
            pagedList = list;
        }
        result.setTotalRows((long) list.size());
        result.setTotalPages(totalPages);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setCurPageSize(pagedList.size());
        result.setList(pagedList);
        return result;
    }
}
