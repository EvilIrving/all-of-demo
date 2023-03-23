package com.ygkj.flood.vo.request;

import com.alibaba.fastjson.JSONObject;
import com.ygkj.entity.PageEntity;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/6/8
 */
@Data
public class PrevTfInfoReqVo extends PageEntity {
    String recType;
    String id;
    String baseId;

    String key;

    /**
     * 自然村作为落点信息查询
     */
    String naturalAdnm;


    /**
     * 短信发送需求字段
     */
    String content;
    String phones;
    List<JSONObject> recInfo;

    /**
     * 影响人自有参数
     */
    String chargerName;
    String chargerPhone;

}
