package com.datang.konus.core.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 * 科纳兹 - mendix 系统用户
 * </p>
 *
 * @author Jane Shi
 * @since 2022-01-06
 */
@Data
public class SystemUserDTO {

    /**
     * 用户mendix的userid
     */
    private BigInteger id;

    /**
     * 用户mendix的登录账号
     */
    private String name;
}
