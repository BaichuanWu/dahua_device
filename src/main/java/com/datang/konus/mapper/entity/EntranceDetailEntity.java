package com.datang.konus.mapper.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

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

  BigInteger id;

  
  String personcode;

  
  String personname;

  
  String employeeid;

  
  String guarddevicecode;

  
  String guarddevicealias;

  
  String location;

  
  LocalDateTime patchdate;

  
  String datemark;

  
  LocalDateTime createddate;

  
  LocalDateTime changeddate;


  BigInteger system$owner;


  BigInteger system$changedby;

}
