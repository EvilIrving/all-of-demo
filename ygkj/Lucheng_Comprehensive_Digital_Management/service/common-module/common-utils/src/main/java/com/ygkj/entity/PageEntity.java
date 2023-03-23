package com.ygkj.entity;

import com.github.pagehelper.PageHelper;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class PageEntity implements Serializable {

    private static final long serialVersionUID = -1792474383774710851L;

    private Integer pageSize;

    private Integer pageNum;

    /**
     * 判断是否能够分页
     *
     * @return true 入参能够分页，false 不满足分页入参
     */
    public boolean isPaged() {
        return pageSize != null && pageNum != null && pageSize > 0 && pageNum > 0;
    }

    public void page() {
        if (isPaged()) {
            PageHelper.startPage(pageNum, pageSize);
        }
    }

    public void noPage() {
        this.pageNum = null;
        this.pageSize = null;
    }

    public List limitPage(List list){
        if (list.size()-1 >= pageSize * pageNum){
            list = list.subList(pageSize * ( pageNum - 1 ) , pageSize * pageNum );
        }else if (list.size() > pageSize * ( pageNum - 1 )){
            list = list.subList(pageSize * ( pageNum - 1 ) , list.size());
        }else {
            list = new ArrayList<>();
        }
        return list;
    }


}
