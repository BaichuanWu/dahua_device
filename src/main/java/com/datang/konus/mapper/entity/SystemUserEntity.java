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
@TableName("system$user")
public class SystemUserEntity {

    @TableField("id")
    private BigInteger id;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("lastlogin")
    private LocalDateTime lastLogin;

    @TableField("blocked")
    private short blocked;

    @TableField("blockedsince")
    private LocalDateTime blockedSince;

    @TableField("active")
    private short active;

    @TableField("failedlogins")
    private Integer failedLogins;

    @TableField("webserviceuser")
    private short webServiceUser;

    @TableField("isanonymous")
    private short isAnonymous;

    @TableField("submetaobjectname")
    private String submetaObjectName;

    @TableField("system$changedby")
    private BigInteger changedBy;

    @TableField("system$owner")
    private BigInteger owner;

    @TableField("createddate")
    private LocalDateTime createdDate;

    @TableField("changeddate")
    private LocalDateTime changedDate;
}
