package com.ygkj.gragh.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 在建工程信息表
 * @date 2020-12-09
 */
@Data
public class BuildingProject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * project_name
     */
    private String projectName;

    /**
     * project_code
     */
    private String projectCode;

    /**
     * longitude
     */
    private String longitude;

    /**
     * latitude
     */
    private String latitude;

    /**
     * image_ids
     */
    private String imageIds;

    /**
     * construct_company
     */
    private String constructCompany;

    /**
     * building_company
     */
    private String buildingCompany;

    /**
     * construct_phone
     */
    private String constructPhone;

    /**
     * building_phone
     */
    private String buildingPhone;

    /**
     * construct_person
     */
    private String constructPerson;

    /**
     * building_person
     */
    private String buildingPerson;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * creator_id
     */
    private Integer creatorId;

    /**
     * update_time
     */
    private Date updateTime;

    /**
     * updator_id
     */
    private Integer updatorId;

    /**
     * del_flag
     */
    private boolean delFlag;

    public BuildingProject() {
    }

}

