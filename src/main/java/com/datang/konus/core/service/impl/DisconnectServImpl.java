package com.datang.konus.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.modal.DaHuaModule;
import com.datang.konus.core.service.DisconnectService;
import com.datang.konus.core.service.SystemSequenceService;
import com.datang.konus.mapper.dao.DisconnectionMapper;
import com.datang.konus.mapper.entity.DisconnectionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DisconnectServImpl implements DisconnectService {

    @Autowired
    private DisconnectionMapper disconnectionMapper;
    @Autowired
    private SystemSequenceService systemSequenceService;

    public void doRecord(long lLoginID, Boolean disconnect){
        Optional<PunchDeviceDTO> punchDeviceDTOOptional = DaHuaModule.punchDevices.stream().filter(o->o.getLoginHandle() == lLoginID).findFirst();
        if (punchDeviceDTOOptional.equals(Optional.empty())){
            return;
        }

        List<BigInteger> tableIds = systemSequenceService.initNextMendixIdsByTable("device$disconnection",1);
        if (CollectionUtils.isEmpty(tableIds)){
            log.error("断线后，tableid 组装失败");
            return;
        }

        PunchDeviceDTO punchDeviceDTO = punchDeviceDTOOptional.get();
        DisconnectionEntity disconnectionEntity = new DisconnectionEntity();
        disconnectionEntity.setId(tableIds.get(0));
        disconnectionEntity.setDeviceNo(punchDeviceDTO.getDeviceIndex());
        disconnectionEntity.setConnectStatus(disconnect? 0 : 1);
        disconnectionEntity.setEventDate(LocalDateTime.now());
        disconnectionEntity.setHasLoad(0);
        disconnectionMapper.insert(disconnectionEntity);
    }
}
