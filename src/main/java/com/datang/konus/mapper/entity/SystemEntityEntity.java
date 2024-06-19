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
@TableName("mendixsystem$entity")
public class SystemEntityEntity {

    @TableField("id")
    private String id;

    @TableField("entity_name")
    private String entityName;

    @TableField("table_name")
    private String tableName;

    @TableField("superentity_id")
    private String superentityId;

    @TableField("remote")
    private Short remote;

    @TableField("remote_primary_key")
    private Short remotePrimaryKey;
}
