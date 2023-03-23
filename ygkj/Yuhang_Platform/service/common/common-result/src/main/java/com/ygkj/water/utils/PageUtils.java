package com.ygkj.water.utils;

import com.github.pagehelper.Page;
import com.ygkj.water.utils.vo.PageVO;

import java.util.List;

/**
* @ClassName:       PageUtils
*                   分页工具类
* @Author:          Huang.zh
* @CreateDate:      2019/9/19 13:26
* @UpdateUser:      Huang.zh
* @UpdateDate:      2019/9/19 13:26
* @Version:         0.0.1
*/
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
        } else {
            //全表记录查询封装
            result.setCurPageSize(list.size());
            result.setList(list);
        }
        return result;
    }
}
