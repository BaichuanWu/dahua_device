package com.datang.konus.core.service.impl;

import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.service.PunchDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author caowu
 * @since 2023/9/12
 */
@Slf4j
@Service
public class PunchDeviceServiceImpl implements PunchDeviceService {
    /**
     * key:订阅句柄
     */
    private ConcurrentHashMap<Long, PunchDeviceDTO> punchDeviceMap = new ConcurrentHashMap<>();
    @Override

    public void syncPunchDeviceMap(PunchDeviceDTO dto) {
        punchDeviceMap.put(dto.getRealLoadHandle(), dto);

    }

    @Override
    public PunchDeviceDTO getByRealLoadHandle(long realLoadHandle) {
        return punchDeviceMap.get(realLoadHandle);
    }
}
