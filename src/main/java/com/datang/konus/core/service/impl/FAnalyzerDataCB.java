package com.datang.konus.core.service.impl;

import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.service.PunchDeviceService;
import com.datang.konus.core.service.TimeClockDetailService;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;


/**
 * 智能报警事件回调
 */
@Slf4j
@Component
public class FAnalyzerDataCB implements NetSDKLib.fAnalyzerDataCallBack {
    @Autowired
    private TimeClockDetailService clockDetailService;
    @Autowired
    private PunchDeviceService punchDeviceService;
    
    String zone = "Asia/Shanghai";

    @Override
    public int invoke(NetSDKLib.LLong lAnalyzerHandle,
                      int dwAlarmType,
                      Pointer pAlarmInfo,
                      Pointer pBuffer,
                      int dwBufSize,
                      Pointer dwUser,
                      int nSequence,
                      Pointer reserved) throws UnsupportedEncodingException {

        if (dwAlarmType == NetSDKLib.EVENT_IVS_ACCESS_CTL) { // /< 门禁事件
            NetSDKLib.DEV_EVENT_ACCESS_CTL_INFO msg = new NetSDKLib.DEV_EVENT_ACCESS_CTL_INFO();
            ToolKits.GetPointerData(pAlarmInfo, msg);

            System.out.println("事件名称 :" + new String(msg.szName).trim());
            if (msg.emEventType == 1) {
                log.info("门禁事件类型 : 进门！");
            } else if (msg.emEventType == 2) {
                log.info("门禁事件类型 : 出门！");
            }

            if (msg.bStatus == 1) {
                log.info("刷卡结果 : 成功！");
            } else if (msg.bStatus == 0) {
                log.info("刷卡结果 : 失败！");
            }

            //进门且刷卡成功
            if (msg.bStatus == 1 && msg.emEventType == 1) {
                DaHuaPunchDTO daHuaPunchDTO = new DaHuaPunchDTO();

                //2023/09/11 09:03:17
                NetSDKLib.NET_TIME_EX date = msg.RealUTC;
                Instant instant = Instant.ofEpochSecond(date.dwUTC);
                // 将Instant对象转换为LocalDateTime对象
                LocalDateTime datetime = LocalDateTime.ofInstant(instant, ZoneId.of(zone));
                log.info("时间秒数：" + msg.RealUTC.dwUTC);
                log.info("时间格式：" + msg.RealUTC.toStringTime());
                log.info("datetime：" + datetime);

                log.info("szName：" + new String(msg.szName));
                log.info("szReaderID：" + new String(msg.szReaderID));
                log.info("szDeviceID：" + new String(msg.szDeviceID));
                log.info("szUserID：" + new String(msg.szUserID));
                log.info("lAnalyzerHandle：" +lAnalyzerHandle);
                //daHuaPunchDTO.setPunchTime(LocalDateTime.parse(msg.RealUTC.toStringTime(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

                if ("00/00/00 00:00:00".equals(msg.RealUTC.toStringTime())) {
                    log.info("szCardName:" + Arrays.toString(msg.szCardName));
                    log.info("msg:" + new String(msg.szCardName, StandardCharsets.UTF_8));
                    log.error("读取时间异常取当前时间：用户人：{}", new String(msg.szCardName, StandardCharsets.UTF_8).trim());
                    datetime = LocalDateTime.now(ZoneId.of(zone));
                }
                daHuaPunchDTO.setPunchTime(datetime);
                daHuaPunchDTO.setRecordId(null);

                daHuaPunchDTO.setCardId(new String(msg.szCardNo).trim());
                daHuaPunchDTO.setUserId(new String(msg.szUserID).trim());
                daHuaPunchDTO.setUserName(new String(msg.szCardName, StandardCharsets.UTF_8).trim());
                //daHuaPunchDTO.setDeviceId(Integer.valueOf(new String(msg.szDeviceID).trim()));
                daHuaPunchDTO.setDeviceId(Integer.valueOf(new String(msg.szReaderID).trim()));
                log.info("msg:" + new String(msg.szDeviceID));
                PunchDeviceDTO punchDeviceDTO=punchDeviceService.getByRealLoadHandle(lAnalyzerHandle.longValue());
                if (punchDeviceDTO != null) {
                    daHuaPunchDTO.setLocation(punchDeviceDTO.getLocation());
                    daHuaPunchDTO.setDId(punchDeviceDTO.getDId());
                }
                log.info("进门且刷卡成功事件->daHuaPunchDTO:{}", daHuaPunchDTO);
                clockDetailService.doRecord(List.of(daHuaPunchDTO));
                log.info("进门且刷卡成功事件处理完毕");
            }
        } else {
            // 其他事件，什么也不需要做
        }
        return 0;
    }


}