package com.datang.konus.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.service.EntranceDetailService;
import com.datang.konus.core.service.TimeClockDetailService;
import com.datang.konus.mapper.entity.EntranceDetailEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TimeClockDetailServImpl implements TimeClockDetailService {

    // @Autowired
    // SystemSequenceService systemSequenceService;
    @Autowired
    EntranceDetailService entranceDetailService;

    public Integer doRecord(List<DaHuaPunchDTO> punchDTOList) {

        if (CollectionUtils.isEmpty(punchDTOList)) {
            return 0;
        }
        List<EntranceDetailEntity> detailEntityList = new ArrayList<>();
        for (int i = 0; i < punchDTOList.size(); i++) {
            DaHuaPunchDTO daHuaPunchDTO = punchDTOList.get(i);
            EntranceDetailEntity entity = new EntranceDetailEntity();
            entity.setLocation(daHuaPunchDTO.getLocation());
            entity.setDeviceId(daHuaPunchDTO.getDId());
            entity.setCardNo(daHuaPunchDTO.getUserId());
            entity.setPatchtime(daHuaPunchDTO.getPunchTime());
            detailEntityList.add(entity);
        }
        if (!CollectionUtils.isEmpty(detailEntityList)) {
            entranceDetailService.saveBatch(detailEntityList, detailEntityList.size());
        }

        return detailEntityList.size();
    }

}
