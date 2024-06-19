package com.datang.konus.core.service.impl;

import com.datang.konus.core.service.SystemUserService;
import com.datang.konus.core.dto.SystemUserDTO;
import com.datang.konus.mapper.dao.SystemUserMapper;
import com.datang.konus.mapper.entity.SystemUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SystemUserServImpl implements SystemUserService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    public  List<SystemUserDTO> obtainAllSystemUser(){

        List<SystemUserEntity> userEntities = systemUserMapper.selectList(null);

        if (ObjectUtils.isEmpty(userEntities)){
            return new ArrayList<>();
        }else {
            List<SystemUserDTO> userDTOList = new ArrayList<>();
            userDTOList.addAll( userEntities.stream().map(
                    userEntity -> {
                        SystemUserDTO systemUserDTO = new SystemUserDTO();
                        BeanUtils.copyProperties(userEntity, systemUserDTO);
                        return systemUserDTO;
                    }
            ).collect(Collectors.toList()));
            return userDTOList;
        }
    }
}
