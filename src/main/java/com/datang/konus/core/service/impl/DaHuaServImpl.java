package com.datang.konus.core.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.datang.konus.core.dto.DaHuaMissDTO;
import com.datang.konus.core.dto.DaHuaPunchDTO;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.modal.DaHuaModule;
import com.datang.konus.core.service.DaHuaService;
import com.datang.konus.core.service.PunchDeviceService;
import com.datang.konus.mapper.dao.GuardDeviceMapper;
import com.datang.konus.mapper.entity.GuardDeviceEntity;
import com.netsdk.lib.NetSDKLib;
import com.netsdk.lib.ToolKits;
import com.netsdk.lib.enumeration.EM_EVENT_IVS_TYPE;
import com.netsdk.lib.enumeration.ENUMERROR;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.netsdk.lib.ToolKits.getErrorCode;

@Slf4j
@Service
public class DaHuaServImpl implements DaHuaService {

    final String encode = "UTF-8";  // 编码格式

    @Autowired
    private FDisConnectCB m_DisConnectCB;
    @Autowired
    private FHaveReConnect haveReConnect;

    @Autowired
    private FAnalyzerDataCB fAnalyzerDataCB;
    @Autowired
    private GuardDeviceMapper guardDeviceMapper;

    @Autowired
    private   PunchDeviceService punchDeviceService;

    // 捞取所有的设备，或者配置所有的设备，后期改为从mendix里捞取配置。
    public List<PunchDeviceDTO> getPunchDevices() {

        List<GuardDeviceEntity> deviceList = guardDeviceMapper.selectList(null);
        if (CollectionUtils.isEmpty(deviceList)) {
            return Collections.emptyList();
        }
        return deviceList.stream().map(item -> {
            PunchDeviceDTO dto = new PunchDeviceDTO();
            dto.setDeviceIndex(item.getDeviceid());
            dto.setLogInUser(item.getGdlogin());
            dto.setLogInPwd(item.getGdpaswd());
            dto.setDeviceIP(item.getGdip());
            dto.setPort(item.getGdport());
            dto.setLocation(item.getLocation());
            return dto;
        }).collect(Collectors.toList());

        /*List<String> ipList = List.of("172.168.20.10", "172.16.1.250");


        List<PunchDeviceDTO> punchDevices = new ArrayList<>();
        int index = 1;
        for (String ip : ipList) {
            PunchDeviceDTO punchDevice = new PunchDeviceDTO();
            punchDevice.setDeviceIndex(index++);
            punchDevice.setDeviceIP(ip);
            punchDevice.setPort(37777);
            punchDevice.setLogInUser("admin");
            punchDevice.setLogInPwd("konus123");
            punchDevices.add(punchDevice);
        }
        //TDO: 后期该从mendix里读取

        return punchDevices;*/
    }

    public PunchDeviceDTO doLogin(PunchDeviceDTO punchDeviceDTO) {
        if (punchDeviceDTO == null || punchDeviceDTO.getDeviceIP() == null) {
            return punchDeviceDTO;
        }

        //初始化SDK库
        DaHuaModule.netsdkApi.CLIENT_Init(m_DisConnectCB, null);
        /*NetSDKLib.LOG_SET_PRINT_INFO setLog = new NetSDKLib.LOG_SET_PRINT_INFO();
        File path = new File("D:\\data\\sdklog\\");
        if (!path.exists()) {
            path.mkdir();
        }
        String logPath = path.getAbsoluteFile().getParent() + "\\sdklog\\"
                + ".log";
        setLog.nPrintStrategy = 0;
        setLog.bSetFilePath = 1;
        System.arraycopy(logPath.getBytes(), 0, setLog.szLogFilePath, 0,
                logPath.getBytes().length);
        System.out.println(logPath);
        setLog.bSetPrintStrategy = 1;
        Boolean bLogopen = DaHuaModule.netsdkApi.CLIENT_LogOpen(setLog);
        if (!bLogopen) {
            System.err.println("Failed to open NetSDK log");
        }*/

        // 设置断线重连回调接口，设置过断线重连成功回调函数后，当设备出现断线情况，SDK内部会自动进行重连操作
        // 此操作为可选操作，但建议用户进行设置
        DaHuaModule.netsdkApi.CLIENT_SetAutoReconnect(haveReConnect, null);

        //设置登录超时时间和尝试次数，可选
        int waitTime = 5000; //登录请求响应超时时间设置为5S
        int tryTimes = 3;    //登录时尝试建立链接3次
        DaHuaModule.netsdkApi.CLIENT_SetConnectTime(waitTime, tryTimes);

        // 设置更多网络参数，NET_PARAM的nWaittime，nConnectTryNum成员与CLIENT_SetConnectTime
        // 接口设置的登录设备超时时间和尝试次数意义相同,可选
        NetSDKLib.NET_PARAM netParam = new NetSDKLib.NET_PARAM();
        netParam.nConnectTime = 10000; //登录时尝试建立链接的超时时间
        DaHuaModule.netsdkApi.CLIENT_SetNetworkParam(netParam);

        // 向设备登入
        int nSpecCap = 0;
        Pointer pCapParam = null;
        IntByReference nError = new IntByReference(0);
        NetSDKLib.NET_DEVICEINFO_Ex deviceinfo = new NetSDKLib.NET_DEVICEINFO_Ex();

        try {

            //NetSDKLib.LLong loginHandle = DaHuaModule.netsdkApi.CLIENT_LoginEx2(punchDeviceDTO.getDeviceIP(), punchDeviceDTO.getPort(), punchDeviceDTO.getLogInUser(), punchDeviceDTO.getLogInPwd(), nSpecCap, pCapParam, deviceinfo, nError);
            NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY pstInParam =
                    new NetSDKLib.NET_IN_LOGIN_WITH_HIGHLEVEL_SECURITY();
            pstInParam.szIP = punchDeviceDTO.getDeviceIP().getBytes(StandardCharsets.UTF_8);
            pstInParam.nPort = punchDeviceDTO.getPort();
            pstInParam.szUserName = punchDeviceDTO.getLogInUser().getBytes(StandardCharsets.UTF_8);
            pstInParam.szPassword = punchDeviceDTO.getLogInPwd().getBytes(StandardCharsets.UTF_8);
            NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY pstOutParam =
                    new NetSDKLib.NET_OUT_LOGIN_WITH_HIGHLEVEL_SECURITY();
            NetSDKLib.LLong loginHandle = DaHuaModule.netsdkApi.CLIENT_LoginWithHighLevelSecurity(pstInParam, pstOutParam);
            if (loginHandle.longValue() != 0L) {
                log.info("Login Device {} Port {} Success!", punchDeviceDTO.getDeviceIP(), punchDeviceDTO.getPort());
                punchDeviceDTO.setLoginHandle(loginHandle.longValue());
            } else {
                log.info("Login Device {} Port {} errCode:{}!", punchDeviceDTO.getDeviceIP(), punchDeviceDTO.getPort(), getErrorCode());
                punchDeviceDTO.setLoginHandle(0);
            }


        } catch (Exception exception) {
            DaHuaModule.netsdkApi.CLIENT_Cleanup();
        }
        return punchDeviceDTO;
    }



    public void doLogOut(PunchDeviceDTO punchDeviceDTO) {
        if (punchDeviceDTO.getLoginHandle() == 0L) {
            return;
        }

        if (DaHuaModule.netsdkApi.CLIENT_Logout(new NetSDKLib.LLong(punchDeviceDTO.getLoginHandle()))) {
            log.info("Logout Device[%s] Port[%d]Success!", punchDeviceDTO.getDeviceIP(), punchDeviceDTO.getPort());
        } else {
            log.error("logout failed.error is " + ENUMERROR.getErrorMessage());
        }
        DaHuaModule.netsdkApi.CLIENT_Cleanup();
    }


    /************************************************************************************************
     * 获取或推送比对成功及失败记录（包括比对照片, 这个是通过触发事件，接收信息
     ************************************************************************************************/
    /**
     * 订阅
     */
    public void realLoadPicture(PunchDeviceDTO punchDeviceDTO) {
        boolean bNeedPicture = false; // 是否需要图片
        int ChannelId = 0; // -1代表全通道

        NetSDKLib.LLong realLoadHandler = DaHuaModule.netsdkApi.CLIENT_RealLoadPictureEx(new NetSDKLib.LLong(punchDeviceDTO.getLoginHandle()), ChannelId, EM_EVENT_IVS_TYPE.EVENT_IVS_ALL.getType(), bNeedPicture ? 1 : 0, fAnalyzerDataCB, null, null);
        if (realLoadHandler.longValue() != 0) {
            log.info("智能订阅成功. 登录Handle = {} , IP = {},订阅handler{}", punchDeviceDTO.getLoginHandle(), punchDeviceDTO.getDeviceIP(), realLoadHandler.longValue());
            punchDeviceDTO.setRealLoadHandle(realLoadHandler.longValue());
            punchDeviceService.syncPunchDeviceMap(punchDeviceDTO);
        } else {

            log.error("智能订阅失败登录Handle = {} , IP = {}." + getErrorCode(), punchDeviceDTO.getLoginHandle(), punchDeviceDTO.getDeviceIP());
        }
    }

    /**
     * 取消订阅
     */
    public void stopRealLoadPicture(PunchDeviceDTO punchDeviceDTO) {
        if (0 != punchDeviceDTO.getRealLoadHandle()) {
            DaHuaModule.netsdkApi.CLIENT_StopLoadPic(new NetSDKLib.LLong(punchDeviceDTO.getRealLoadHandle()));
            log.info("停止智能订阅.");
        }
    }

    public List<DaHuaPunchDTO> queryAccessRecords(PunchDeviceDTO deviceDTO, DaHuaMissDTO missDTO) {
        if (ObjectUtils.isEmpty(deviceDTO) || ObjectUtils.isEmpty(missDTO) || ObjectUtils.isEmpty(missDTO.getStartTime()) || ObjectUtils.isEmpty(missDTO.getEndTime())) {
            return new ArrayList<>();
        }

        NetSDKLib.FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX queryCondition = new NetSDKLib.FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX();
        queryCondition.bTimeEnable = 1;
        queryCondition.stStartTime.setTime(missDTO.getStartTime().getYear(), missDTO.getStartTime().getMonthValue(), missDTO.getStartTime().getDayOfMonth(), missDTO.getStartTime().getHour(), missDTO.getStartTime().getMinute(), missDTO.getStartTime().getSecond());
        queryCondition.stEndTime.setTime(missDTO.getEndTime().getYear(), missDTO.getEndTime().getMonthValue(), missDTO.getEndTime().getDayOfMonth(), missDTO.getEndTime().getHour(), missDTO.getEndTime().getMinute(), missDTO.getEndTime().getSecond());

        List<DaHuaPunchDTO> punchDTOList = queryAccessRecords(deviceDTO.getLoginHandle(), queryCondition);
        for (DaHuaPunchDTO daHuaPunchDTO : punchDTOList) {
            log.info("---------------记录打印---------------------\n" + "刷卡时间: {%s} \n" + "记录编号: {%s} \n" + "  卡编号: {%s} \n" + "用户编号: {%s} \n" + "用户姓名: {%s} \n" + "设备编号: {%s} \n", daHuaPunchDTO.getPunchTime(), daHuaPunchDTO.getRecordId(), daHuaPunchDTO.getCardId(), daHuaPunchDTO.getUserId(), daHuaPunchDTO.getUserName(), daHuaPunchDTO.getDeviceId());

        }

        return punchDTOList;

    }

    /**
     * 查询门禁刷卡记录
     *
     * @param queryCondition 查询条件
     */
    public List<DaHuaPunchDTO> queryAccessRecords(long loginHandle, NetSDKLib.FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX queryCondition) {

        List<DaHuaPunchDTO> daHuaPunchDTOList = new ArrayList<>();

        if (loginHandle <= 0 || queryCondition == null) {
            return daHuaPunchDTOList;
        }

        NetSDKLib.NET_IN_FIND_RECORD_PARAM findRecordIn = new NetSDKLib.NET_IN_FIND_RECORD_PARAM();
        findRecordIn.emType = NetSDKLib.EM_NET_RECORD_TYPE.NET_RECORD_ACCESSCTLCARDREC_EX;
        findRecordIn.pQueryCondition = queryCondition.getPointer();

        NetSDKLib.NET_OUT_FIND_RECORD_PARAM findRecordOut = new NetSDKLib.NET_OUT_FIND_RECORD_PARAM();

        queryCondition.write();
        findRecordIn.write();
        findRecordOut.write();
        boolean success = DaHuaModule.netsdkApi.CLIENT_FindRecord(new NetSDKLib.LLong(loginHandle), findRecordIn, findRecordOut, 5000);
        findRecordOut.read();
        findRecordIn.read();
        queryCondition.read();

        if (!success) {
            log.error("Can Not Find This Record: {%s}", String.format("0x%x", DaHuaModule.netsdkApi.CLIENT_GetLastError()));
            return daHuaPunchDTOList;
        }

        log.info("FindRecord Succeed! FindHandle : {}", findRecordOut.lFindeHandle);

        final int nRecordCount = 10;  // 每次查询的最大个数

        NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARDREC[] records = new NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARDREC[nRecordCount];
        for (int i = 0; i < nRecordCount; i++) {
            records[i] = new NetSDKLib.NET_RECORDSET_ACCESS_CTL_CARDREC();
        }

        NetSDKLib.NET_IN_FIND_NEXT_RECORD_PARAM findNextRecordIn = new NetSDKLib.NET_IN_FIND_NEXT_RECORD_PARAM();
        findNextRecordIn.lFindeHandle = findRecordOut.lFindeHandle;
        findNextRecordIn.nFileCount = nRecordCount;  //想查询的记录条数

        NetSDKLib.NET_OUT_FIND_NEXT_RECORD_PARAM findNextRecordOut = new NetSDKLib.NET_OUT_FIND_NEXT_RECORD_PARAM();
        findNextRecordOut.nMaxRecordNum = nRecordCount;
        findNextRecordOut.pRecordList = new Memory((long) records[0].dwSize * nRecordCount); // 申请内存
        findNextRecordOut.pRecordList.clear((long) records[0].dwSize * nRecordCount);

        // 将  native 数据初始化
        ToolKits.SetStructArrToPointerData(records, findNextRecordOut.pRecordList);

        int count = 0;  //循环的次数
        int recordIndex = 0;
        while (true) {  //循环查询

            if (!DaHuaModule.netsdkApi.CLIENT_FindNextRecord(findNextRecordIn, findNextRecordOut, 5000)) {
                log.error("FindNextRecord Failed {}", ToolKits.getErrorCode());
                break;
            }

            /// 将 native 数据转为 java 数据
            ToolKits.GetPointerDataToStructArr(findNextRecordOut.pRecordList, records);


            for (int i = 0; i < findNextRecordOut.nRetRecordNum; i++) {
                recordIndex = i + count * nRecordCount;
                try {
                    if (records[i].bStatus == 1) {
                        DaHuaPunchDTO daHuaPunchDTO = new DaHuaPunchDTO();
                        //跳过异常时间

                        daHuaPunchDTO.setPunchTime(LocalDateTime.parse(records[i].stuTime.toStringTime(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                        daHuaPunchDTO.setRecordId(records[i].nRecNo);
                        daHuaPunchDTO.setCardId(new String(records[i].szCardNo).trim());
                        daHuaPunchDTO.setUserId(new String(records[i].szUserID).trim());
                        daHuaPunchDTO.setUserName(new String(records[i].szCardName, encode).trim());
                        daHuaPunchDTO.setDeviceId(records[i].nDoor);
                        daHuaPunchDTOList.add(daHuaPunchDTO);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            if (findNextRecordOut.nRetRecordNum < nRecordCount) {
                break;
            } else {
                count++;
            }
        }
        success = DaHuaModule.netsdkApi.CLIENT_FindRecordClose(findRecordOut.lFindeHandle);
        if (!success) {
            log.error("Failed to Close: {}", ToolKits.getErrorCode());
        }

        return daHuaPunchDTOList;
    }
}
