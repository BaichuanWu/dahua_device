package com.datang.konus.comm;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.datang.konus.core.dto.PunchDeviceDTO;
import com.datang.konus.core.service.DaHuaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Order(9)
@Slf4j
public class KonusRunner implements ApplicationRunner {

    @Autowired
    DaHuaService daHuaService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        /**
         *
         *它支持使用@Order或是实现Ordered接口来支持优先级执行。Order值越小越先执行
         */
        log.info("读取设备配置，并完成登录");
        List<PunchDeviceDTO> punchDeviceDTOList = daHuaService.getPunchDevices();
        if (CollectionUtils.isEmpty(punchDeviceDTOList)) {
            return;
        }

        punchDeviceDTOList.forEach(
                punchDeviceDTO -> daHuaService.doLogin(punchDeviceDTO)
        );
        //注册回调函数
        punchDeviceDTOList.forEach(
                punchDeviceDTO -> daHuaService.realLoadPicture(punchDeviceDTO)
        );

    }
}
