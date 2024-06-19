package com.datang.konus.facade.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.datang.konus.core.dto.DaHuaHistoryDTO;
import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.dto.TimeClockDetailDTO;
import com.datang.konus.core.service.DaHuaService;
import com.datang.konus.core.service.MissHistoryService;
import com.datang.konus.core.service.SystemUserService;
import com.datang.konus.core.service.TimeClockDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("punch")
public class PunchController {

    @Autowired
    DaHuaService daHuaService;
    @Autowired
    MissHistoryService missHistoryService;
    @Autowired
    private TimeClockDetailService timeClockDetailService;

    @GetMapping("in")
    public Integer doPunchIn(String username) {


        TimeClockDetailDTO timeClockDetailDTO = new TimeClockDetailDTO();
        timeClockDetailDTO.setLocation("16");
        timeClockDetailDTO.setEmployeeId("68888");
        timeClockDetailDTO.setPersonName("demo_administrator");
        timeClockDetailDTO.setClockingTime(LocalDateTime.now());
        timeClockDetailDTO.setOwner(BigInteger.valueOf(23643898043695260l));
        timeClockDetailDTO.setChangedBy(BigInteger.valueOf(23643898043695260l));
        timeClockDetailDTO.setCreatedDate(LocalDateTime.now());
        timeClockDetailDTO.setChangedDate(LocalDateTime.now());
        Integer insertInt = timeClockDetailService.insertTimeClockDetailWithCustomerId(timeClockDetailDTO);

        return insertInt;
    }


    @GetMapping("history")
    public Boolean LoadHistory(){

        //获取需要load的时间段
        List<DaHuaHistoryDTO> daHuaHistoryDTOList = missHistoryService.MissPunchTimeRecords();
        if (CollectionUtils.isEmpty(daHuaHistoryDTOList)){
            return false;
        }

        //捞取断线中间的记录
        daHuaHistoryDTOList.forEach(
                daHuaHistoryDTO -> {
                    List<DaHuaPunchDTO> punchDTOList = daHuaService.queryAccessRecords(daHuaHistoryDTO.getDeviceDTO(),daHuaHistoryDTO.getMissDTO());
                    if (!CollectionUtils.isEmpty(punchDTOList)){
                        //写入数据库
                        Integer recordInt = timeClockDetailService.doRecord(punchDTOList);
                        if (recordInt >0){
                            missHistoryService.MarkHistoryDealed(daHuaHistoryDTO.getMissDTO().getDisconnectIDs());
                        }
                    }
                }
        );
        return true;
    }

}
