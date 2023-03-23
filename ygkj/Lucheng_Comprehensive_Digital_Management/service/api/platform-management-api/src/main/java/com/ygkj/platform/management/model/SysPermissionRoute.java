package com.ygkj.platform.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysPermissionRoute {

    private String id;

    private String name;

    private String pid;

    private String url;

    private SysPermissionRoute parent;
}
