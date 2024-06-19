package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * (Human$staff)表实体类
 *
 * @author caowu
 * @since 2023-08-16 14:59:59
 */
@Data
@TableName("human$staff")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffEntity {

    BigInteger id;

    String personcode;


    String personname;


    String employeeid;


    String departcode;


    String departname;

    String guardcardid;

}

