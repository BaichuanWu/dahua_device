package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import com.baomidou.mybatisplus.annotation.TableField;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author caowu
 * @since 2023/8/16
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("attendance_device_record")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EntranceDetailEntity {

  Long id;
  @TableField("device_id")
  Long deviceId;
  String location;
  @TableField("punch_time")
  LocalDateTime patchtime;

  @TableField("employee_card_no")
  String cardNo;
}
