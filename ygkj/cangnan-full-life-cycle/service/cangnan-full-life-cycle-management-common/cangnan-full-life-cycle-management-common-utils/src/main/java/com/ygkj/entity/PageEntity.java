package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageEntity implements Serializable {

    private Integer pageSize;

    private Integer pageNum;

}
