package com.ygkj.utils;

import com.github.pagehelper.Page;
import com.ygkj.vo.PageVO;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class PageUtils {

    private PageUtils() {}

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
        }else {
            if(!CollectionUtils.isEmpty(list)){
                result.setList(list);
                result.setTotalRows(Long.valueOf(list.size()));
            }else{
                result.setTotalRows(0L);
                result.setList(Collections.emptyList());
            }
        }
        return result;
    }
}
