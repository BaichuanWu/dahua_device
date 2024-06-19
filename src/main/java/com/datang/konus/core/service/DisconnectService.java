package com.datang.konus.core.service;


import com.netsdk.lib.NetSDKLib;
import com.sun.jna.Pointer;

public interface DisconnectService {

    void doRecord(long lLoginID, Boolean disconnect);
}
