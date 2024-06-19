package com.datang.konus.core.dto;

import lombok.Data;

@Data
public class PunchDeviceDTO {

    // 设备信息
    private Integer DeviceIndex;
    private String DeviceIP ;
    private int Port = 37777;   //37777
    private String LogInUser ;
    private String LogInPwd ;

    // 登录后信息
    private long LoginHandle = 0l;
    // 订阅句柄
    private long realLoadHandle = 0l;

    String location;
}
