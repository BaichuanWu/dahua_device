package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author caowu
 * @since 2023/8/31
 */
@Data
@TableName("attendance_device")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuardDeviceEntity {
    Long id;
    @TableField("id_no")
    String deviceid;
    @TableField("account")
    String gdlogin;
    @TableField("password")
    String gdpaswd;
    @TableField("port")
    Integer gdport;
    @TableField("ip_addr")
    String gdip;
    String location;
}
