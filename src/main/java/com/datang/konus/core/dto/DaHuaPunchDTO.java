package com.datang.konus.core.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DaHuaPunchDTO {

    private LocalDateTime punchTime;

    private Integer recordId;

    private String cardId;

    private String userId;

    private String userName;

    private Integer deviceId;

    String location;
}
