package com.datang.konus.core.service;

import com.datang.konus.core.dto.DaHuaMissDTO;
import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.netsdk.lib.NetSDKLib;

import java.util.List;

public interface DaHuaService {

    PunchDeviceDTO doLogin(PunchDeviceDTO punchDeviceDTO);

    void doLogOut(PunchDeviceDTO punchDeviceDTO);



    List<DaHuaPunchDTO> queryAccessRecords(PunchDeviceDTO deviceDTO, DaHuaMissDTO missDTO);

    List<DaHuaPunchDTO> queryAccessRecords(long loginHandle, NetSDKLib.FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX queryCondition);

    List<PunchDeviceDTO> getPunchDevices();

    void realLoadPicture(PunchDeviceDTO punchDeviceDTO);

}
