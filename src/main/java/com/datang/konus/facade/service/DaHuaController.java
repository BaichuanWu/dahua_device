package com.datang.konus.facade.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.modal.DaHuaModule;
import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.service.DaHuaService;
import com.datang.konus.core.service.TimeClockDetailService;
import com.netsdk.lib.NetSDKLib;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("dahua")
public class DaHuaController {
    @Autowired
    DaHuaService daHuaService;
    @Autowired
    TimeClockDetailService timeClockDetailService;

    @GetMapping("demo")
    public List<DaHuaPunchDTO> QueryRecordByTime() {

        LocalDateTime startDateTime = LocalDateTime.of(2023, 8, 1, 14, 8, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 8, 1, 14, 10, 0);

        NetSDKLib.FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX queryCondition = new NetSDKLib.FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX();
        queryCondition.bTimeEnable = 1;
        queryCondition.stStartTime.setTime(startDateTime.getYear(), startDateTime.getMonthValue(), startDateTime.getDayOfMonth(), startDateTime.getHour(), startDateTime.getMinute(), startDateTime.getSecond());
        queryCondition.stEndTime.setTime(endDateTime.getYear(), endDateTime.getMonthValue(), endDateTime.getDayOfMonth(), endDateTime.getHour(), endDateTime.getMinute(), endDateTime.getSecond());

        List<DaHuaPunchDTO> punchDTOList = daHuaService.queryAccessRecords(DaHuaModule.punchDevices.get(0).getLoginHandle(), queryCondition);
        punchDTOList.stream().forEach(daHuaPunchDTO -> {
            log.info("---------------记录打印---------------------");
            log.info("刷卡时间: {}", daHuaPunchDTO.getPunchTime());
            log.info("记录编号: {}", daHuaPunchDTO.getRecordId());
            log.info("  卡编号: {}", daHuaPunchDTO.getCardId());
            log.info("用户编号: {}", daHuaPunchDTO.getUserId());
            log.info("用户姓名: {}", daHuaPunchDTO.getUserName());
            log.info("设备编号: {}", daHuaPunchDTO.getDeviceId());
        });

        return punchDTOList;
    }
}
