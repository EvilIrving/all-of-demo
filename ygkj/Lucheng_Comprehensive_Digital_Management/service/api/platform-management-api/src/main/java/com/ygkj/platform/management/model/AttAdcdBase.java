package com.ygkj.platform.management.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Document(indexName = "adcd", type = "docs", shards = 1, replicas = 0)
public class AttAdcdBase {
    /**
     * 行政区划编码
     */
    @Id
    private String adcd;

    /**
     * 行政区划名称
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String adnm;

    /**
     * 父级行政区划
     */
    @Field(type = FieldType.Keyword)
    private String parentAdcd;

    /**
     * 行政级别
     */
    @Field(type = FieldType.Long)
    private Integer level;

    /**
     * 城市的行政编码
     */

    @Field(type = FieldType.Keyword)
    private String cityAdcd;

    /**
     * 城市的名称
     */
    @Field(type = FieldType.Keyword)
    private String cityAdnm;

    /**
     * 县（区）行政编码
     */
    @Field(type = FieldType.Keyword)
    private String countryAdcd;

    /**
     * 县（区）名称
     */
    @Field(type = FieldType.Keyword)
    private String countryAdnm;


}
