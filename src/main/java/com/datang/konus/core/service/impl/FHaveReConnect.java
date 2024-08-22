package com.datang.konus.core.service.impl;

// import com.datang.konus.core.service.DisconnectService;
// import com.datang.konus.core.service.SystemSequenceService;
// import com.datang.konus.mapper.dao.DisconnectionMapper;
import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 网络连接恢复，设备重连成功回调
 * 通过 CLIENT_SetAutoReconnect 设置该回调函数，当已断线的设备重连成功时，SDK会调用该函数
 */
@Slf4j
@Component
public class FHaveReConnect implements NetSDKLib.fHaveReConnect {
    // @Autowired
    // private DisconnectService disconnectService;

    public void invoke(NetSDKLib.LLong loginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        log.info("ReConnect Device[%s] Port[%d]\n", pchDVRIP,nDVRPort);
        // disconnectService.doRecord(loginHandle.longValue(),false);
    }
}
