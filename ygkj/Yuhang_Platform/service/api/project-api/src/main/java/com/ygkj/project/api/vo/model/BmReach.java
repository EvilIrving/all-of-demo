package com.ygkj.project.api.vo.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 河道基本信息表
 * @author hucong
 * @date 2022-04-27
 */
@Data
public class BmReach implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * dts_cmf_id
    */
    private String dtsCmfId;

    /**
    * 河道（段）编码
    */
    private String code;

    /**
    * 河道（段）名称
    */
    private String name;

    /**
    * 行政区划编码
    */
    private String adcd;

    /**
    * 等级
    */
    private String grade;

    /**
    * 干支流：（0、干流；1、一级支流；2、二级支流；3、三级支流；4、四级支流；5、五级支流；6、六级支流等；rn-1、平原河道）
    */
    private String mntrb;

    /**
    * 所属流域
    */
    private String bas;

    /**
    * 创建人
    */
    private String dtsCmfCreatorId;

    /**
    * 创建时间
    */
    private Date dtsCmfCreateTime;

    /**
    * 更新人
    */
    private String dtsCmfUpdatorId;

    /**
    * 更新时间
    */
    private Date dtsCmfUpdateTime;

    /**
    * 归属科室
    */
    private String dtsCmfDept;

    /**
    * 删除标记
    */
    private Integer dtsCmfDeleted;

    /**
    * 数据版本
    */
    private Integer dtsCmfVersion;

    /**
    * 水域类型 1:河道 2:湖泊 3:水库 4:塘坝 5:其他
    */
    private Integer type;

    public BmReach() {}
}