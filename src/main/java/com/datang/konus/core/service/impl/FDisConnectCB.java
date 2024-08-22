package com.datang.konus.core.service.impl;

// import com.datang.konus.core.service.DisconnectService;
import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设备断线回调: 通过 CLIENT_Init 设置该回调函数，当设备出现断线时，SDK会调用该函数
 */
@Slf4j
@Component
public class FDisConnectCB implements NetSDKLib.fDisConnect {

    // @Autowired
    // DisconnectService disconnectService;

    public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        log.info("Device[%s] Port[%d] Disconnect!\n", pchDVRIP, nDVRPort);
        // disconnectService.doRecord(lLoginID.longValue(),true);
    }
}