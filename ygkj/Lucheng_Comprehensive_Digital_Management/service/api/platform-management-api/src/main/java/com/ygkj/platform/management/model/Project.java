package com.ygkj.platform.management.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "project", type = "docs", shards = 1, replicas = 0)
public class Project {

    @Id
    private String projectCode;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String projectName;

    @Field(type = FieldType.Keyword)
    private String longitude;

    @Field(type = FieldType.Keyword)
    private String latitude;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String projectType;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String bas;

}
