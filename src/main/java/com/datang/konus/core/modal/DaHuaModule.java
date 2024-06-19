package com.datang.konus.core.modal;

import com.datang.konus.core.dto.PunchDeviceDTO;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.module.BaseModule;

import java.util.List;

public class DaHuaModule {

    public static List<PunchDeviceDTO> punchDevices;

    public static NetSDKLib netsdkApi = NetSDKLib.NETSDK_INSTANCE;
}
