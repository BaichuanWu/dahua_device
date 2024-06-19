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
@TableName("mendixsystem$sequence")
public class SystemSequenceEntity {

    @TableField("name")
    private String name;

    @TableField("attribute_id")
    private String attributeId;

    @TableField("start_value")
    private BigInteger startValue;

    @TableField("current_value")
    private BigInteger currentValue;
}
