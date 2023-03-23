package com.ygkj.project.model;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class TokenEntity {

    private String token;

    private LocalDateTime localDateTime;

}
