package com.dcy.datamanage.biz.util;

import com.dcy.datamanage.api.model.common.PageResult;
import com.github.pagehelper.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


public class PageUtil {

    /**
     * 转换page
     *
     * @param list
     * @return
     */
    public static <T> PageResult<T> toPage(List<T> list) {
        PageResult<T> result = new PageResult<>();
        if (list instanceof Page) {
            Page<T> page = (Page<T>) list;
            result.setTotal(page.getTotal());
            result.setPages(page.getPages());
            result.setCurrent(page.getPageNum());
            result.setSize(page.getPageSize());
            result.setRecords(page.getResult());
        } else {
            if (!CollectionUtils.isEmpty(list)) {
                result.setRecords(list);
                result.setTotal(Long.valueOf(list.size()));
            } else {
                result.setPages(0L);
                result.setRecords(Collections.emptyList());
            }
        }
        return result;
    }
}
