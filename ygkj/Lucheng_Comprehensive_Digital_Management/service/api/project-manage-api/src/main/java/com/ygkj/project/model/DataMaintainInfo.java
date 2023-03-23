package com.ygkj.project.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentLoopMerge;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DataMaintainInfo extends PageEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId("id")
    @ExcelIgnore
    private String id;

    /**
     * 目录id
     */
    @ExcelIgnore
    private String directoryId;

    /**
     * 目录名称
     */
//    @Excel(name = "目录名称",orderNum = "1",width = 80,needMerge = true)
    private String directoryName;

    /**
     * 数据项
     */
    @ExcelIgnore
    private String dataItem;

    /**
     * 详细描述
     */
    @ExcelIgnore
    private String description;

    /**
     * 数据来源
     */
    @ExcelIgnore
    private String source;

    /**
     * 责任单位名称
     */
    @ExcelIgnore
    private String unitName;

    /**
     * 责任单位负责人
     */
    @ExcelIgnore
    private String unitDirector;

    /**
     * 责任单位负责人联系方式
     */
    @ExcelIgnore
    private String unitDirectorPhone;

    /**
     * 第三方单位json
     */
    @ExcelIgnore
    private String thirdPartyUnit;

    @TableField(exist = false)
//    @ExcelCollection(name = "数据维护负责人")
//    @ExcelProperty(value = "数据维护负责人")
    private List<ThirdPartyUnitInfo> thirdPartyUnitInfos;

    /**
        数据维护负责人名称
    */
    @ExcelIgnore
    private String dataMaintainName;
    /**
        数据维护负责人电话
    */
    @ExcelIgnore
    private String dataMaintainPhone;

    @ExcelIgnore
    private Date tm;

    /**
     * 责任人json
     */
    @ExcelIgnore
    private String sixPeople;

    /**
     * 主管部门责任人
     */
    @TableField(exist = false)
    @ExcelIgnore
    private SixPeopleInfo competentPeople;

    /**
     * 管理部门责任人
     */
    @TableField(exist = false)
    @ExcelIgnore
    private SixPeopleInfo managePeople;

    /**
     * 物业单位责任人
     */
    @TableField(exist = false)
    @ExcelIgnore
    private SixPeopleInfo propertyPeople;

    /**
     * 技术责任人
     */
    @TableField(exist = false)
    @ExcelIgnore
    private SixPeopleInfo technologyPeople;

    /**
     * 巡查责任人
     */
    @TableField(exist = false)
    @ExcelIgnore
    private SixPeopleInfo patrolPeople;

    /**
     * 0表示没有同步工程运管数据
     * 1表示同步了工程运管责任人数据
     */
    @TableField(exist = false)
    @ExcelIgnore
    private String transManageDataType;
}
