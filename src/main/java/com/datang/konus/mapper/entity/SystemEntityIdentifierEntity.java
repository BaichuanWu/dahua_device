package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigInteger;

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
@TableName("mendixsystem$entityidentifier")
public class SystemEntityIdentifierEntity {

    @TableField("id")
    private String id;

    @TableField("short_id")
    private Short shortId;

    @TableField("object_sequence")
    private BigInteger objectSequence;
}
