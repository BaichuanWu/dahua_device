package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

/**
 * @author caowu
 * @since 2023/8/31
 */
@Data
@TableName("basedata$guarddevice")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuardDeviceEntity {
    BigInteger id;
    Integer deviceid;
    String gdlogin;
    String gdpaswd;
    Integer gdport;
    String gdip;
    String location;
}
