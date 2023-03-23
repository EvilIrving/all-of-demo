package com.ygkj.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Date 2021/1/13
 */
@Data
public class OrganSchema implements Serializable {

    private String id;

    private String name;

    private String pid;

    private boolean county;

    List<OrganSchema> children;

    private int num;
}
