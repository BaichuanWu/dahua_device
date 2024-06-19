package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Getter
@Setter
@Accessors(chain = true)
@TableName("attendance$timeclockdetail")
public class TimeClockDetailEntity {

    //@TableId(value = "id", type = IdType.AUTO)
    @TableField("id")
    private BigInteger id;

    @TableField("clockingtime")
    private LocalDateTime clockingTime;

    @TableField("personname")
    private String personName;

    @TableField("employeeid")
    private String employeeId;

    @TableField("location")
    private String location;

    @TableField("system$changedby")
    private BigInteger changedBy;

    @TableField("system$owner")
    private BigInteger owner;

    @TableField("createddate")
    private LocalDateTime createdDate;

    @TableField("changeddate")
    private LocalDateTime changedDate;
}
