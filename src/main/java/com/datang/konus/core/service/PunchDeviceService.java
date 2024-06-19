package com.datang.konus.core.service;

import com.datang.konus.core.dto.PunchDeviceDTO;

/**
 * @author caowu
 * @since 2023/9/12
 */
public interface PunchDeviceService {
   void syncPunchDeviceMap(PunchDeviceDTO dto);
    PunchDeviceDTO getByRealLoadHandle(long realLoadHandle);
}
