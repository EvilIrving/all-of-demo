package com.ygkj.auth.api.monitor.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: fml
 * @Date: 2020/9/17 17:07
 * @Description: 项目申请材料信息
 */
@Data
public class BusProjectFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 事项id
     */
    private String projectId;

    /**
     * 材料名称
     */
    private String materialName;

    /**
     * 是否必要(1必要，0不必要)
     */
    private Integer isMust;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 文件名称
     */
    private String fileName;
}
