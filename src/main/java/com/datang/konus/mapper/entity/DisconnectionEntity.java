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
@TableName("device$disconnection")
public class DisconnectionEntity {

    @TableField("id")
    private BigInteger id;

    @TableField("connectstatus")
    private Integer connectStatus;

    @TableField("eeventdt")
    private LocalDateTime eventDate;

    @TableField("deviceno")
    private Integer deviceNo;

    @TableField("hasload")
    private Integer hasLoad;
}
