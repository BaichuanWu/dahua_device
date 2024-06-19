package com.datang.konus.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.datang.konus.core.modal.DaHuaModule;
import com.datang.konus.core.dto.DaHuaHistoryDTO;
import com.datang.konus.core.dto.DaHuaMissDTO;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.service.DaHuaService;
import com.datang.konus.core.service.MissHistoryService;
import com.datang.konus.mapper.dao.DisconnectionMapper;
import com.datang.konus.mapper.entity.DisconnectionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MissHistoryServImpl implements MissHistoryService {

    @Autowired
    DaHuaService daHuaService;

    @Autowired
    private DisconnectionMapper disconnectionMapper;

    public List<DaHuaHistoryDTO> MissPunchTimeRecords() {

        //直接捞取没有处理过的信息
        LambdaQueryWrapper<DisconnectionEntity> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(DisconnectionEntity::getHasLoad, 0);
        List<DisconnectionEntity> disconnectionEntityList = disconnectionMapper.selectList(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(disconnectionEntityList)) {
            return new ArrayList<>();
        }

        List<DaHuaHistoryDTO> daHuaHistoryDTOList = new ArrayList<>();
        Map<Integer, List<DisconnectionEntity>> disconnectionMap = disconnectionEntityList.stream().collect(Collectors.groupingBy(DisconnectionEntity::getDeviceNo));
        disconnectionMap.forEach(
                (deviceNo, disconnectionEntities) -> {

                    //判断是否存在以登录的设备
                    Optional<PunchDeviceDTO> punchDeviceDTOOptional = DaHuaModule.punchDevices.stream().filter(o -> o.getDeviceIndex().equals(deviceNo)).findFirst();
                    if (ObjectUtils.isEmpty(punchDeviceDTOOptional) || punchDeviceDTOOptional.equals(Optional.empty())) {
                        return;
                    }

                    PunchDeviceDTO punchDeviceDTO = punchDeviceDTOOptional.get();

                    if (punchDeviceDTO.getLoginHandle() <= 0) {
                        daHuaService.doLogin(punchDeviceDTO);
                    }

                    if (punchDeviceDTO.getLoginHandle() <= 0) {
                        return;
                    }

                    List<DaHuaMissDTO> daHuaMissList = buildDaHuaMissDTOFromDisconnectionEntity(disconnectionEntityList);
                    if (CollectionUtils.isEmpty(daHuaMissList)) {
                    } else {
                        daHuaMissList.forEach(
                                daHuaMissDTO -> {
                                    DaHuaHistoryDTO daHuaHistoryDTO = new DaHuaHistoryDTO();
                                    daHuaHistoryDTO.setDeviceDTO(punchDeviceDTO);
                                    daHuaHistoryDTO.setMissDTO(daHuaMissDTO);
                                    daHuaHistoryDTOList.add(daHuaHistoryDTO);
                                }
                        );
                    }
                }
        );

        return daHuaHistoryDTOList;
    }

    public List<DaHuaMissDTO> buildDaHuaMissDTOFromDisconnectionEntity(List<DisconnectionEntity> disconnectionEntityList) {

        if (CollectionUtils.isEmpty(disconnectionEntityList)) {
            return new ArrayList<>();
        }

        //处理捞取的时间对象
        List<DaHuaMissDTO> daHuaMissDTOS = new ArrayList<>();
        Boolean allowedAddOne = true;
        DaHuaMissDTO lastDaHuaMissDTO = null;
        for (int i = 0; i < disconnectionEntityList.size(); i++) {
            DisconnectionEntity disconnectionEntity = disconnectionEntityList.get(i);
            if (disconnectionEntity.getConnectStatus().equals(0)) {

                DaHuaMissDTO daHuaMissDTO;
                if (allowedAddOne) {
                    daHuaMissDTO = new DaHuaMissDTO();
                    daHuaMissDTO.setStartTime(disconnectionEntity.getEventDate());
                    daHuaMissDTO.getDisconnectIDs().add(disconnectionEntity.getId());
                    allowedAddOne = false;
                    lastDaHuaMissDTO = daHuaMissDTO;
                    daHuaMissDTOS.add(daHuaMissDTO);
                } else {
                    lastDaHuaMissDTO.getDisconnectIDs().add(disconnectionEntity.getId());
                }
            } else if (disconnectionEntity.getConnectStatus().equals(1)) {
                lastDaHuaMissDTO.setEndTime(disconnectionEntity.getEventDate());
                lastDaHuaMissDTO.getDisconnectIDs().add(disconnectionEntity.getId());
                allowedAddOne = true;
            } else {
                //什么也不做;
            }
        }

        return daHuaMissDTOS.stream().filter(o -> !ObjectUtils.isEmpty(o.getStartTime()) && !ObjectUtils.isEmpty(o.getEndTime()) && !CollectionUtils.isEmpty(o.getDisconnectIDs())).collect(Collectors.toList());
    }

    public Integer MarkHistoryDealed(List<BigInteger> disconnectIDs){
            if (CollectionUtils.isEmpty(disconnectIDs)){
                return 0;
            }

          return  disconnectionMapper.markLoadedWithUpdateBatchByIds(disconnectIDs);
    }
}
