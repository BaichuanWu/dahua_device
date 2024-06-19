package com.datang.konus.core.dto;

import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 * 科纳兹 -
 * </p>
 *
 * @author Jane Shi
 * @since 2022-01-06
 */
@Data
public class TimeClockDetailDTO {

    private Integer id;

    private LocalDateTime clockingTime;


    private String personName;


    private String employeeId;


    private String location;


    private BigInteger changedBy;


    private BigInteger owner;


    private LocalDateTime createdDate;


    private LocalDateTime changedDate;
}
