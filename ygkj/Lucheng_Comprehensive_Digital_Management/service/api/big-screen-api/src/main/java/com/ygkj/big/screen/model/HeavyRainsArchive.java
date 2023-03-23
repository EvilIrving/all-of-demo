package com.ygkj.big.screen.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ygkj.gragh.model.PageEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wgf
 * @date 2022/5/6 13:39
 * @Description: 暴雨归档（鹿城区降雨）
 */
@Data
public class HeavyRainsArchive  extends PageEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_UUID)
    /**
     * id
     */
    private String id;

    /**
     * 暴雨名称
     */
    private String name;

    /**
     * 开始时间(yyyy-mm-dd hh)
     */
    private String startTime;

    /**
     * 结束时间(yyyy-mm-dd hh)
     */
    private String endTime;

    /**
     * 持续时间(h)
     */
    private String duration;

    /**
     * 累计降雨
     */
    private String rainfall;

    @TableField(exist = false)
    private List<String> codes;

    @TableField(exist = false)
    private Integer hours;

    public HeavyRainsArchive() {}
}
