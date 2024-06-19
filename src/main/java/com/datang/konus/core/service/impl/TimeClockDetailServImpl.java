package com.datang.konus.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.dto.TimeClockDetailDTO;
import com.datang.konus.core.service.EntranceDetailService;
import com.datang.konus.core.service.SystemSequenceService;
import com.datang.konus.core.service.TimeClockDetailService;
import com.datang.konus.mapper.dao.StaffMapper;
import com.datang.konus.mapper.dao.TimeClockDetailMapper;
import com.datang.konus.mapper.entity.EntranceDetailEntity;
import com.datang.konus.mapper.entity.StaffEntity;
import com.datang.konus.mapper.entity.TimeClockDetailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimeClockDetailServImpl implements TimeClockDetailService {

    @Autowired
    TimeClockDetailMapper timeClockDetailMapper;
    @Autowired
    SystemSequenceService systemSequenceService;
    @Autowired
    EntranceDetailService entranceDetailService;
    @Autowired
    StaffMapper staffMapper;
    String TimeClockTableName = "attendance$timeclockdetail";

    String entrancedetailTableName = "attendance$entrancedetail";

    public Integer insertTimeClockDetailWithCustomerId(TimeClockDetailDTO detailDTO) {

        if (ObjectUtils.isEmpty(detailDTO)) {
            return 0;
        }

        TimeClockDetailEntity timeClockDetailEntity = new TimeClockDetailEntity();
        BeanUtils.copyProperties(detailDTO, timeClockDetailEntity);
        if (ObjectUtils.isEmpty(detailDTO.getId())) {

            List<BigInteger> tableId = systemSequenceService.initNextMendixIdsByTable(TimeClockTableName, 1);
            timeClockDetailEntity.setId(tableId.get(0));
        }

        return timeClockDetailMapper.insert(timeClockDetailEntity);
    }


    public Integer doRecord(List<DaHuaPunchDTO> punchDTOList) {

        if (CollectionUtils.isEmpty(punchDTOList)) {
            return 0;
        }
        /*
        List<TimeClockDetailEntity> timeClockDetailEntityList = new ArrayList<>();
        List<BigInteger> tableIds = systemSequenceService.initNextMendixIdsByTable(TimeClockTableName, punchDTOList.size());
        for (int i = 0; i < punchDTOList.size(); i++) {
            TimeClockDetailEntity timeClockDetailEntity = new TimeClockDetailEntity();
            timeClockDetailEntity.setId(tableIds.get(i));
            timeClockDetailEntity.setLocation(String.valueOf(punchDTOList.get(i).getDeviceId()));
            timeClockDetailEntity.setEmployeeId(punchDTOList.get(i).getUserId());// staff guardcardid
            timeClockDetailEntity.setPersonName(punchDTOList.get(i).getUserName());
            timeClockDetailEntity.setClockingTime(punchDTOList.get(i).getPunchTime());
            timeClockDetailEntity.setOwner(BigInteger.valueOf(23643898043695260L));
            timeClockDetailEntity.setChangedBy(BigInteger.valueOf(23643898043695260L));
            timeClockDetailEntity.setCreatedDate(LocalDateTime.now());
            timeClockDetailEntity.setChangedDate(LocalDateTime.now());
            timeClockDetailEntityList.add(timeClockDetailEntity);
        }

        return timeClockDetailMapper.InsertBatchSomeColumn(timeClockDetailEntityList);*/
        List<BigInteger> tableIds = systemSequenceService.initNextMendixIdsByTable(entrancedetailTableName, punchDTOList.size());

        Set<String> userIdSet = punchDTOList.stream().map(DaHuaPunchDTO::getUserId).collect(Collectors.toSet());
        LambdaQueryWrapper<StaffEntity> staffLambda = new LambdaQueryWrapper<>();
        staffLambda.in(StaffEntity::getGuardcardid, userIdSet);
        Map<String, StaffEntity> staffMap = staffMapper.selectList(staffLambda).stream()
                .collect(Collectors.toMap(StaffEntity::getGuardcardid, item -> item));
        LocalDateTime now = LocalDateTime.now();
        BigInteger userId = BigInteger.valueOf(23643898043695260L);

        List<EntranceDetailEntity> detailEntityList = new ArrayList<>();
        for (int i = 0; i < punchDTOList.size(); i++) {
            DaHuaPunchDTO daHuaPunchDTO = punchDTOList.get(i);
            StaffEntity staffEntity = staffMap.get(daHuaPunchDTO.getUserId());
            if (staffEntity == null) {
                log.info("未找到对应员工实体对象：{}", daHuaPunchDTO);
                continue;
            }
            EntranceDetailEntity entity = new EntranceDetailEntity();
            entity.setId(tableIds.get(i));
            entity.setLocation(daHuaPunchDTO.getLocation());

            entity.setPersoncode(staffEntity.getPersoncode());
            entity.setPersonname(staffEntity.getPersonname());
            entity.setEmployeeid(staffEntity.getEmployeeid());
            entity.setSystem$owner(userId);
            entity.setSystem$changedby(userId);
            entity.setCreateddate(now);
            entity.setChangeddate(now);
            entity.setPatchdate(daHuaPunchDTO.getPunchTime());
            detailEntityList.add(entity);
        }
        if (!CollectionUtils.isEmpty(detailEntityList)) {
            entranceDetailService.saveBatch(detailEntityList, detailEntityList.size());
        }

        return detailEntityList.size();
    }

}
